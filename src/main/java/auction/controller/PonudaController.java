package auction.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import auction.model.Constants;
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
	
	@GetMapping("/getAllActiveUserDodatneInfoTasks/{korisnikId}")
	public ArrayList<String> getAllActiveUserDodatneInfoTasks(@PathVariable Long korisnikId) {
		
		Korisnik korisnik = korisnikService.findOne(korisnikId);
		
		List<Task> activeUserTasks = taskService.createTaskQuery().taskAssignee(korisnik.getKorisnickoIme()).list();
		
		ArrayList<String> tasks = new ArrayList<>();
		for (Task task : activeUserTasks) {
			System.out.println(task.getName());
			tasks.add(task.getId());	
		}
		
		return tasks;
		
	}
	
	@GetMapping("/getTasks/{korisnikId}")
	public Map<String, ArrayList<String>> getTasks(@PathVariable Long korisnikId) {
		
		Korisnik korisnik = korisnikService.findOne(korisnikId);
		
		List<Task> activeUserTasks = taskService.createTaskQuery().taskAssignee(korisnik.getKorisnickoIme()).list();
		Map<String, ArrayList<String>> mapa = new HashMap<String, ArrayList<String>>();
		for (Task task : activeUserTasks) {
			if (mapa.get(task.getName()) == null) {
				mapa.put(task.getName(), new ArrayList<String>());
			}
			String key = task.getName();
			ArrayList<String> value = mapa.get(key);
			value.add(task.getId());
			mapa.put(key, value);
			
		}
		return mapa;
		
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
	public ResponseEntity<Map<String, Object>> getAllSentOffersRanked(@PathVariable Long zahtevId){
		
		Task task = taskService.createTaskQuery().active().list().get(taskService.createTaskQuery().active().list().size()-1);
		Map<String, Object> mapa = new HashMap<>();
		Collection<Ponuda> ponude = ponudaService.rangirajPrikupljenePonude(nabavkaService.findOne(zahtevId));
		mapa.put("ponude", ponude);
		mapa.put("taskID", task.getId());
		return new ResponseEntity<Map<String, Object>>(mapa, HttpStatus.OK);
	}
	
	@PostMapping("/odaberi/{taskId}")
	public ResponseEntity<Ponuda> odaberi(@RequestBody Ponuda ponuda, @PathVariable String taskId) {
		
		System.out.println("Odabrana ponuda firme: " + ponuda.getFirma().getImeFirme());
		ponuda.setStatus(Constants.PONUDA_ODABRANA);
		Ponuda odabranaPonuda = ponudaService.save(ponuda);
		//Da znas koji je task u pitanju
		Task task = taskService.createTaskQuery().active().taskId(taskId).singleResult();
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(task.getProcessInstanceId());
		variables.put("odluka", "odabrana");
		variables.put("odabranaPonuda", ponuda);
		variables.put("odabranaFirma", odabranaPonuda.getFirma().getKorisnickoIme());
		taskService.complete(taskId, variables);
		
		return new ResponseEntity<Ponuda>(ponuda, HttpStatus.OK);
	}
	
	@PostMapping("/traziDodatneInfo/{zahtevZaPojasnjenje}/{taskId}")
	public ResponseEntity<Ponuda> traziDodatneInfo(@RequestBody Ponuda ponuda, @PathVariable String zahtevZaPojasnjenje, @PathVariable String taskId) {
		
		System.out.println("Zahtev za pojasnjenje: " + zahtevZaPojasnjenje);
		System.out.println("Firma koja treba da posalje posasnjenje: " + ponuda.getFirma().getImeFirme());
		ponuda.setZahtevZaPojasnjenje(zahtevZaPojasnjenje);
		Ponuda savedPonuda = ponudaService.save(ponuda);
		//Da znas koji je task u pitanju
		Task task = taskService.createTaskQuery().active().taskId(taskId).singleResult();
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(task.getProcessInstanceId());
		variables.put("odluka", "dodatneInfo");
		variables.put("ponudaZaPojasnjenje", ponuda);
		variables.put("firmaZaDodatneInfo", ponuda.getFirma().getKorisnickoIme());
		taskService.complete(taskId, variables);
		
		return new ResponseEntity<Ponuda>(savedPonuda, HttpStatus.OK);
	}
	
	@PostMapping("/ponoviPostupak/{zahtevId}/{noviRok}/{taskId}")
	public ResponseEntity<ZahtevZaNabavku> ponoviPostupak(@PathVariable Long zahtevId, @PathVariable String noviRok, @PathVariable String taskId) {
		
		System.out.println("Ponovi postupak");
		ZahtevZaNabavku zzn = nabavkaService.findOne(zahtevId);
		zzn.setRokZaPonude(noviRok);
		ZahtevZaNabavku zahtevZaNabavku = nabavkaService.save(zzn);
		
		//Da znas koji je task u pitanju
		Task task = taskService.createTaskQuery().active().taskId(taskId).singleResult();
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(task.getProcessInstanceId());
		/*variables.remove("ponuda");
		Collection<Ponuda> ponude = ponudaService.findByZahtevZaNabavku(zzn);
		for (Ponuda ponuda : ponude) {
			ponudaService.delete(ponuda);
		}*/
		variables.put("odluka", "ponoviPostupak");
		
		if (variables.get("ponovljen")==null){
			variables.put("ponovljen", 1);
		}
		else {
			int kolikoPutaJePonovljen = (int) variables.get("ponovljen");
			++kolikoPutaJePonovljen;
			variables.put("ponovljen", kolikoPutaJePonovljen);
		}
		
		variables.put("zahtev", zahtevZaNabavku);
		taskService.complete(taskId, variables);
		
		return new ResponseEntity<ZahtevZaNabavku>(zahtevZaNabavku, HttpStatus.OK);
	}
	
	@PostMapping("/otkaziZahtev/{zahtevId}/{taskId}")
	public ResponseEntity<ZahtevZaNabavku> otkaziZahtev(@PathVariable Long zahtevId, @PathVariable String taskId) {
		
		System.out.println("Otkazujem zahtev");
		ZahtevZaNabavku zzn = nabavkaService.findOne(zahtevId);

		//Da znas koji je task u pitanju
		Task task = taskService.createTaskQuery().active().taskId(taskId).singleResult();
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(task.getProcessInstanceId());
		variables.remove("ponuda");
		variables.remove("zahtev");
		Collection<Ponuda> ponude = ponudaService.findByZahtevZaNabavku(zzn);
		for (Ponuda ponuda : ponude) {
			ponudaService.delete(ponuda);
		}
		nabavkaService.delete(zzn);
		
		variables.put("odluka", "otkaziZahtev");
		taskService.complete(taskId, variables);
		
		return new ResponseEntity<ZahtevZaNabavku>(zzn, HttpStatus.OK);
	}
	
	@PostMapping("/saveDodatneInfo/{dodatneInfo}/{korisnikID}/{taskId}")
	public ResponseEntity<String> saveDodatneInfo(@PathVariable String dodatneInfo, @PathVariable Long korisnikID, @PathVariable String taskId) {
		
		System.out.println("Dodatne info");

		//Da znas koji je task u pitanju
		Task task = taskService.createTaskQuery().active().taskId(taskId).singleResult();
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(task.getProcessInstanceId());
		variables.put("pojasnjenje", dodatneInfo);
		taskService.complete(taskId, variables);
		
		return new ResponseEntity<String>(dodatneInfo, HttpStatus.OK);
	}
	
	
	@PostMapping("/terminPocetkaIzvrsavanja/{terminPocetkaIzvrsavanja}/{taskId}")
	public ResponseEntity<String> terminPocetkaIzvrsavanja(@PathVariable String terminPocetkaIzvrsavanja, @PathVariable String taskId) {
		
		System.out.println("terminPocetkaIzvrsavanja post");
		

		//Da znas koji je task u pitanju
		Task task = taskService.createTaskQuery().active().taskId(taskId).singleResult();
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(task.getProcessInstanceId());
		
		variables.put("terminPocetkaIzvrsavanja", terminPocetkaIzvrsavanja);
		taskService.complete(taskId, variables);
		
		return new ResponseEntity<String>(terminPocetkaIzvrsavanja, HttpStatus.OK);
	}
	
	@PostMapping("/ocena/{ocena}/{taskId}/{klijent}")
	public ResponseEntity<String> terminPocetkaIzvrsavanja(@PathVariable Integer ocena, @PathVariable String taskId, @PathVariable Boolean klijent) {
		
		System.out.println("Ocena post");
		
		
		//Da znas koji je task u pitanju
		Task task = taskService.createTaskQuery().active().taskId(taskId).singleResult();
		HashMap<String, Object> variables = (HashMap<String, Object>) runtimeService.getVariables(task.getProcessInstanceId());
		if (klijent) {
			variables.put("ocenaKlijenta", ocena);
		}
		else {
			variables.put("ocenaFirme", ocena);
		}
		
		taskService.complete(taskId, variables);
		
		return new ResponseEntity<String>(ocena.toString(), HttpStatus.OK);
	}
	
	
	
}
