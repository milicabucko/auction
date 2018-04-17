package auction.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auction.model.Korisnik;
import auction.model.ZahtevZaNabavku;
import auction.repository.ZahtevZaNabavkuRepository;

@Service
public class NabavkaService {

	@Autowired
	private ZahtevZaNabavkuRepository zahtevZaNabavkuRepository;
	
	@Autowired
	private KorisnikService korisnikService;
	
	
	public ZahtevZaNabavku save(ZahtevZaNabavku zahtevZaNabavku) {
		return zahtevZaNabavkuRepository.save(zahtevZaNabavku);
	}
	
	public ZahtevZaNabavku findOne(Long id) {
		return zahtevZaNabavkuRepository.findOne(id);
	}
	
	public void delete(Long id) {
		zahtevZaNabavkuRepository.delete(id);
	}
	
	public void delete(ZahtevZaNabavku zzn) {
		zahtevZaNabavkuRepository.delete(zzn);
	}
	
	public Collection<Korisnik> getFirmsForAuction(ZahtevZaNabavku zahtevZaNabavku, Long korisnikID){
		
		Collection<Korisnik> firmsFromCategory = korisnikService.findByKategorija(zahtevZaNabavku.getKategorija());
		ArrayList<Korisnik> candidates = new ArrayList<>();
		
		Korisnik korisnik = korisnikService.findOne(korisnikID);
		zahtevZaNabavku.setKorisnik(korisnik);
		
		double latitude = korisnik.getLatitude();
		double longitude = korisnik.getLongitude();
		
		for (Korisnik firma : firmsFromCategory) {
			Integer distance = (int) DistanceService.distance(latitude, longitude, firma.getLatitude(), firma.getLongitude(), 'K');
			if (distance > firma.getUdaljenost()) {
				continue;
			}
			candidates.add(firma);
		}
		
		return candidates;
		
	}
	
	
}
