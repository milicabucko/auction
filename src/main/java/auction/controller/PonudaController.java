package auction.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import auction.model.Ponuda;
import auction.model.ZahtevZaNabavku;
import auction.service.KorisnikService;
import auction.service.NabavkaService;
import auction.service.PointsService;
import auction.service.PonudaService;

@RestController
@RequestMapping(value = "/ponude")
@CrossOrigin(origins = "http://localhost:4200")
public class PonudaController {

	@Autowired
	private PointsService pointsService;
	
	@Autowired
	private PonudaService ponudaService;
	
	@Autowired
	private KorisnikService korisnikService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private NabavkaService nabavkaService;
	
	@GetMapping("/getAllActiveUserTasks/{korisnikId}")
	public ArrayList<String> getAllActiveUserTasks(@PathVariable Long korisnikId) {
		
		Korisnik korisnik = korisnikService.findOne(korisnikId);
		
		List<Task> activeUserTasks = taskService.createTaskQuery().taskAssignee(korisnik.getKorisnickoIme()).list();
		
		ArrayList<String> tasks = new ArrayList<>();
		for (Task task : activeUserTasks) {
			System.out.println(task.getName());
			tasks.add(task.getId());	
		}
		
		return tasks;
		
	}
	
	
	@PostMapping("/sacuvajPonudu/{korisnikId}/{taskId}")
	public ResponseEntity<Ponuda> saveOffer(@RequestBody Ponuda ponuda, @PathVariable Long korisnikId, @PathVariable String taskId) throws ParseException{
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
		Date date = format1.parse(ponuda.getRokZaIzvrsavanje());
		ponuda.setRokZaIzvrsavanje(format2.format(date));
		//Double points = pointsService.points(ponuda.getRokZaIzvrsavanje(), ponuda.getZahtevZaNabavku().getRokZaNabavku(), ponuda.getCena(), ponuda.getZahtevZaNabavku().getMaxVrednost());
		//ponuda.setPoeni(points);
		Korisnik korisnik = korisnikService.findOne(korisnikId);
		ponuda.setFirma(korisnik);
		Ponuda savedOffer = ponudaService.save(ponuda);
		//Da znas koji je task u pitanju
		Task task = taskService.createTaskQuery().active().taskId(taskId).singleResult();
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(task.getProcessInstanceId());
		variables.put("ponuda", ponuda);
		taskService.complete(taskId, variables);
		
		return new ResponseEntity<Ponuda>(savedOffer, HttpStatus.OK);
	}
	
	
	
	@GetMapping("/ponude/getSviKorisnikoviZahtevi/{korisnikId}")
	public ResponseEntity<Collection<ZahtevZaNabavku>> getSviKorisnikoviZahtevi(@PathVariable Long korisnikId){
		Collection<ZahtevZaNabavku> zahtevi = nabavkaService.findByKorisnik(korisnikService.findOne(korisnikId));
		return new ResponseEntity<Collection<ZahtevZaNabavku>>(zahtevi, HttpStatus.OK);
	}
	
	
	@GetMapping("/zahtev/getSvePonude/{zahtevId}")
	public ResponseEntity<Collection<Ponuda>> getAllSentOffersRanked(@PathVariable Long zahtevId){
		
		Task task = taskService.createTaskQuery().active().list().get(taskService.createTaskQuery().active().list().size()-1);
		
		Collection<Ponuda> ponude = ponudaService.rangirajPrikupljenePonude(nabavkaService.findOne(zahtevId));
		return new ResponseEntity<Collection<Ponuda>>(ponude, HttpStatus.OK);
	}
	
	
	
}
