package auction.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import auction.service.KorisnikService;
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
		return new ResponseEntity<Ponuda>(savedOffer, HttpStatus.OK);
	}
	
}
