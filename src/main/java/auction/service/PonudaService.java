package auction.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auction.model.Constants;
import auction.model.Ponuda;
import auction.model.ZahtevZaNabavku;
import auction.repository.PonudaRepository;

@Service
public class PonudaService {

	@Autowired
	private PonudaRepository ponudaRepository;
	
	@Autowired
	private PointsService pointsService;
	
	@Autowired
	private TaskService taskService;
	
	public Ponuda save(Ponuda ponuda) {
		return ponudaRepository.save(ponuda);
	}
	
	public Ponuda sacuvajPonuduZaZahtev(Ponuda ponuda, ZahtevZaNabavku zahtev) throws ParseException {
		Double points = pointsService.points(ponuda.getRokZaIzvrsavanje(), zahtev.getRokZaNabavku(), ponuda.getCena(), zahtev.getMaxVrednost());
		ponuda.setPoeni(points);
				
		ponuda.setZahtevZaNabavku(zahtev);
		return ponudaRepository.save(ponuda);
	}
	
	public ArrayList<Long> analizirajPrikupljenePonude(ZahtevZaNabavku zahtev) {
		System.out.println("Analiziraj prikupljene ponude!");
		ArrayList<Long> ponude = new ArrayList<>();
		Collection<Ponuda> poslatePonude = ponudaRepository.findByZahtevZaNabavkuAndStatus(zahtev, Constants.PONUDA_POSLATA);
		for (Ponuda ponuda : poslatePonude) {
			ponude.add(ponuda.getId());
		}
		return ponude;
	}
	
	public Collection<Ponuda> rangirajPrikupljenePonude(ZahtevZaNabavku zahtev) {
		System.out.println("Rangiraj sve prikupljene ponude");
		Collection<Ponuda> ponude = ponudaRepository.findByZahtevZaNabavkuAndStatusOrderByPoeniDesc(zahtev, Constants.PONUDA_POSLATA);
		return ponude;
	}
	
	public Collection<Ponuda> getAllSentOrRefusedOffersForRequest(ZahtevZaNabavku zahtevZaNabavku) {
		ArrayList<String> statusi = new ArrayList<>();
		statusi.add(Constants.PONUDA_POSLATA);
		statusi.add(Constants.PONUDA_ODBIJENA);
		return ponudaRepository.findByZahtevZaNabavkuAndStatusIn(zahtevZaNabavku, statusi);
	}
	
	public Collection<Ponuda> findByZahtevZaNabavku(ZahtevZaNabavku zahtevZaNabavku) {
		return ponudaRepository.findByZahtevZaNabavku(zahtevZaNabavku);
	}

	public void delete(Ponuda ponuda) {
		ponudaRepository.delete(ponuda);
	}
	
	public void sacuvajPonuduSaDodatnimObjasnjenjem(Ponuda ponudaZaPojasnjenje, String pojasnjenje) {
		System.out.println("Sacuvaj ponudu sa dodatnim pojasnjenjem!");
		ponudaZaPojasnjenje.setPojasnjenje(pojasnjenje);
		ponudaRepository.save(ponudaZaPojasnjenje);
	}
	
	
	
}
