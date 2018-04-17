package auction.controller;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
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

import auction.model.Kategorija;
import auction.model.Korisnik;
import auction.service.KategorijaService;
import auction.service.KorisnikService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/registration")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrationController {
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private KorisnikService korisnikService;
	
	@Autowired
	private KategorijaService kategorijaService;
	
	public static final String FIRMA = "firma";
	public static boolean groupsSetted = false;
	
	@GetMapping("/atp")
	public ResponseEntity<Map<String,Object>> atp() {
		
		runtimeService.startProcessInstanceByKey("test");
		Task task = taskService.createTaskQuery().active().list()
								.get(taskService.createTaskQuery()
								.active().list().size()-1);
		Map<String, Object> taskMap = new HashMap<>();
		taskMap.put("ime", task.getName());
		taskMap.put("id", task.getId());
		System.out.println(taskMap.get("ime")+ " i id" + taskMap.get("id"));
		return new ResponseEntity<Map<String,Object>>(taskMap, HttpStatus.OK);
	}
	
	
	@GetMapping("/activateProcess")
	public ResponseEntity<Map<String,Object>> getTask() {
		
		runtimeService.startProcessInstanceByKey("registracija");
		Task task = taskService.createTaskQuery().active().list().get(taskService.createTaskQuery().active().list().size()-1);
		Map<String, Object> taskMap = new HashMap<>();
		taskMap.put("ime", task.getName());
		taskMap.put("id", task.getId());
		System.out.println(taskMap.get("ime")+ " i id" + taskMap.get("id"));
		return new ResponseEntity<Map<String,Object>>(taskMap, HttpStatus.OK);
	}
	
	@PostMapping("/{taskId}")
	public ResponseEntity<Map<String,Object>> registration(@PathVariable String taskId, @RequestBody Korisnik korisnik){
		
		
		korisnik.setPotvrdjenMail(false);
		korisnikService.save(korisnik);
		
		//Da znas koji je task u pitanju
		Task task = taskService.createTaskQuery().active().taskId(taskId).singleResult();

		//Varijable koje se koriste u toku procesa
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(task.getProcessInstanceId());
		
		org.activiti.engine.identity.User newUser;
		
		newUser = identityService.newUser(korisnik.getKorisnickoIme());
		newUser.setFirstName(korisnik.getIme());
		newUser.setLastName(korisnik.getPrezime());
		newUser.setEmail(korisnik.getEmail());
		newUser.setPassword(korisnik.getLozinka());
		identityService.saveUser(newUser);
	
		identityService.createMembership(newUser.getId(), korisnik.getTipKorisnika());
		
		variables.put("korisnik", korisnik);
	
		//Zavrsi taj trenutni task (prva forma za korisnika) kako bi se mogla prebaciti na sledeci
		taskService.complete(taskId, variables);
		
		if(FIRMA.equals(korisnik.getTipKorisnika())){
			//Napravi novi task za firmu
			Task taskFirma = taskService.createTaskQuery().active().list().get(taskService.createTaskQuery().active().list().size() - 1);
			
			Map<String, Object> taskMap = new HashMap<String, Object>();
			//vrati task id kako bi znala kasnije koji task da izvrsis
			taskMap.put("taskId", taskFirma.getId());
			taskMap.put("korisnik", korisnik);
			
			return new ResponseEntity<Map<String,Object>>(taskMap, HttpStatus.OK);
		}
		
		return new ResponseEntity<Map<String,Object>>(variables, HttpStatus.OK);
	}
	
	@PostMapping("/firma/{taskId}")
	public ResponseEntity<Map<String,Object>> registrationFirm(@PathVariable String taskId, @RequestBody Korisnik k){
		
		Korisnik korisnik = korisnikService.save(k);
		
		//Uzmi task koji je vezan za firmu
		Task task = taskService.createTaskQuery().active().taskId(taskId).singleResult();
		
		//definisi izlazne varijable
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(task.getProcessInstanceId());
		variables.put("korisnik", korisnik);
		
		//Zavrsi task
		taskService.complete(taskId, variables);
		
		return new ResponseEntity<>(variables, HttpStatus.OK);
	}
	
	@GetMapping("/kategorije")
	public ResponseEntity<Collection<Kategorija>> getAllKategorije() {
		Collection<Kategorija> kategorije = kategorijaService.findAll();
		return new ResponseEntity<Collection<Kategorija>>(kategorije, HttpStatus.OK);
	}
	
	@GetMapping("/confirmRegistration")
	public RedirectView confirmRegistration(@RequestParam("korisnickoIme") String korisnickoIme, @RequestParam("task") String task) {
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(task).signalEventSubscriptionName("aktiviraj").singleResult();
		runtimeService.signalEventReceived("aktiviraj", execution.getId());
		return new RedirectView("http://localhost:4200/login");
		
	}
	

}
