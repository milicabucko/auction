package auction.controller;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
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

import auction.model.User;
import auction.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/registration")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrationController {
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	IdentityService identityService;
	
	public static final String FIRMA = "firma";
	public static boolean groupsSetted = false;
	
	@GetMapping("/activateProcess")
	public ResponseEntity<Map<String,Object>> getTask() {
		System.out.println("Hellooo");
		runtimeService.startProcessInstanceByKey("registracija");
		Task task = taskService.createTaskQuery().active().list()
								.get(taskService.createTaskQuery()
								.active().list().size()-1);
		Map<String, Object> taskMap = new HashMap<>();
		taskMap.put("ime", task.getName());
		taskMap.put("id", task.getId());
		System.out.println(taskMap.get("ime")+ "i id" + taskMap.get("id"));
		return new ResponseEntity<Map<String,Object>>(taskMap, HttpStatus.OK);
	}
	
	@PostMapping("/{taskId}")
	public ResponseEntity<Map<String,Object>> registration(@PathVariable String taskId, @RequestBody User korisnik){
		
		
		if (groupsSetted == false) {
			Group korisnikG;
			
			korisnikG = identityService.newGroup("korisnik");
			korisnikG.setName("korisnik");
			korisnikG.setType("assigment");
			identityService.saveGroup(korisnikG);
			
			Group firmaG;
			
			firmaG = identityService.newGroup("firma");
			firmaG.setName("firma");
			firmaG.setType("assigment");
			identityService.saveGroup(firmaG);
			
			groupsSetted = true;
		}
		
		System.out.println("Hellooo");
		
		userService.saveUser(korisnik);
		Task task = taskService.createTaskQuery().active().taskId(taskId).singleResult();
		System.out.println("aaaaa " + task.getProcessInstanceId());
		
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService
				.getVariables(task.getProcessInstanceId());
		
		org.activiti.engine.identity.User newUser;
		
		newUser = identityService.newUser(korisnik.getKorisnickoIme());
		newUser.setFirstName(korisnik.getIme());
		newUser.setLastName(korisnik.getPrezime());
		newUser.setEmail(korisnik.getEmail());
		newUser.setPassword(korisnik.getLozinka());
		identityService.saveUser(newUser);
	
		identityService.createMembership(newUser.getId(), korisnik.getTipKorisnika());
		
		variables.put("korisnik", korisnik);
		System.out.println(variables + "   variables");
		taskService.complete(taskId, variables);
		
		if(FIRMA.equals(korisnik.getTipKorisnika())){
			Task taskFirma = taskService.createTaskQuery().active().list()
					.get(taskService.createTaskQuery().active().list().size() - 1);
			
			Map<String, Object> taskMap = new HashMap<String, Object>();
			taskMap.put("taskId", taskFirma.getId());
			taskMap.put("name", taskFirma.getName());
			taskMap.put("username", korisnik.getKorisnickoIme());
			System.out.println("firma task id " + taskFirma.getId() + " task name " + taskFirma.getName());
			return new ResponseEntity<Map<String,Object>>(taskMap, HttpStatus.OK);
		}
		
		return new ResponseEntity<Map<String,Object>>(variables, HttpStatus.OK);
	}
	

}
