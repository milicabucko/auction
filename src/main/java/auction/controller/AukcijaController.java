package auction.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import auction.model.Constants;
import auction.model.Kategorija;
import auction.model.Korisnik;
import auction.model.ZahtevZaNabavku;
import auction.service.KorisnikService;
import auction.service.NabavkaService;

@RestController
@RequestMapping(value = "/aukcija")
@CrossOrigin(origins = "http://localhost:4200")
public class AukcijaController {
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private KorisnikService korisnikService;
	
	@Autowired
	private NabavkaService nabavkaService;
	
	
	@GetMapping("/activateProcess")
	public ResponseEntity<Map<String,Object>> getTask() {
		
		runtimeService.startProcessInstanceByKey("aukcija");
		Task task = taskService.createTaskQuery().active().list().get(taskService.createTaskQuery().active().list().size()-1);
		Map<String, Object> taskMap = new HashMap<>();
		taskMap.put("ime", task.getName());
		taskMap.put("id", task.getId());
		System.out.println(taskMap.get("ime")+ " i id" + taskMap.get("id"));
		return new ResponseEntity<Map<String,Object>>(taskMap, HttpStatus.OK);
	}
	
	//Unos podataka za zahtev za nabavku
	@PostMapping("/zahtevZaNabavku/save/{taskId}/{korisnikId}")
	public ResponseEntity<Map<String,Object>> saveZZN(@PathVariable String taskId, @PathVariable Long korisnikId ,@RequestBody ZahtevZaNabavku zzn) throws ParseException{
		
		Korisnik korisnik = korisnikService.findOne(korisnikId);
		zzn.setKorisnik(korisnik);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
		Date date = format1.parse(zzn.getRokZaPonude());
		zzn.setRokZaPonude(format2.format(date));
		date = format1.parse(zzn.getRokZaNabavku());
		zzn.setRokZaNabavku(format2.format(date));
		ZahtevZaNabavku zahtevZaNabavku= nabavkaService.save(zzn);
		
		//Da znas koji je task u pitanju
		Task task = taskService.createTaskQuery().active().taskId(taskId).singleResult();

		//Varijable koje se koriste u toku procesa
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(task.getProcessInstanceId());
		variables.put("zahtev", zahtevZaNabavku);
		variables.put("korisnik", korisnik);
	
		//Zavrsi taj trenutni task (prva forma za korisnika) kako bi se mogla prebaciti na sledeci
		taskService.complete(taskId, variables);
			
		return new ResponseEntity<Map<String,Object>>(variables, HttpStatus.OK);
	}
	
	
	@GetMapping("/kategorijePosla")
	public ResponseEntity<Collection<Kategorija>> getAllKategorije() {
		Collection<Kategorija> kategorije = korisnikService.getAllKategorijePosla();
		return new ResponseEntity<Collection<Kategorija>>(kategorije, HttpStatus.OK);
	}
	
	@GetMapping("/lackOfFirmsDecision")
	public RedirectView lackOfFirmsDecision(@RequestParam("decision") String decision, @RequestParam("task") String task) {
		
		if (decision.equals(Constants.YES)) {
			Execution execution = runtimeService.createExecutionQuery().processInstanceId(task).signalEventSubscriptionName("posaljiNaManjeFirmi").singleResult();
			runtimeService.signalEventReceived("posaljiNaManjeFirmi", execution.getId());
		}
		else {
			Execution execution = runtimeService.createExecutionQuery().processInstanceId(task).signalEventSubscriptionName("nemojSlatiNaManjeFirmi").singleResult();
			runtimeService.signalEventReceived("nemojSlatiNaManjeFirmi", execution.getId());
		}
		
		return new RedirectView("http://localhost:4200/");
	}
	
	
	@GetMapping("/nemaDovoljnoPonuda")
	public RedirectView nemaDovoljnoPonuda(@RequestParam("decision") String decision, @RequestParam("task") String task) {
		
		if (decision.equals(Constants.saljiNovimFirmama)) {
			Execution execution = runtimeService.createExecutionQuery().processInstanceId(task).signalEventSubscriptionName("saljiNovimFirmama").singleResult();
			runtimeService.signalEventReceived("saljiNovimFirmama", execution.getId());
		}
		else if (decision.equals(Constants.neSaljiNovimFirmama)){
			Execution execution = runtimeService.createExecutionQuery().processInstanceId(task).signalEventSubscriptionName("neSaljiNovimFirmama").singleResult();
			runtimeService.signalEventReceived("neSaljiNovimFirmama", execution.getId());
		}
		else {
			Execution execution = runtimeService.createExecutionQuery().processInstanceId(task).signalEventSubscriptionName("odluciNaOsnovuPostojecihFirmi").singleResult();
			runtimeService.signalEventReceived("odluciNaOsnovuPostojecihFirmi", execution.getId());
		}
		
		return new RedirectView("http://localhost:4200/");
	}
	
	
	

}
