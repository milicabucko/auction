package auction.controller;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
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
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping("/zahtevZaNabavku/save/{taskId}/{korisnikId}")
	public ResponseEntity<Map<String,Object>> saveZZN(@PathVariable String taskId, @PathVariable Long korisnikId ,@RequestBody ZahtevZaNabavku zzn){
		
		Korisnik korisnik = korisnikService.findOne(korisnikId);
		zzn.setKorisnik(korisnik);
		//TODO: Datume takodje isparsiraj!
		ZahtevZaNabavku zahtevZaNabavku= nabavkaService.save(zzn);
		
		//TODO: Proveri ime taska da znas tacno u kod momentu si gde!
		//Da znas koji je task u pitanju
		Task task = taskService.createTaskQuery().active().taskId(taskId).singleResult();

		//Varijable koje se koriste u toku procesa
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(task.getProcessInstanceId());
		variables.put("zahtev", zahtevZaNabavku);
	
		//Zavrsi taj trenutni task (prva forma za korisnika) kako bi se mogla prebaciti na sledeci
		taskService.complete(taskId, variables);
		
		return new ResponseEntity<Map<String,Object>>(variables, HttpStatus.OK);
	}
	

}
