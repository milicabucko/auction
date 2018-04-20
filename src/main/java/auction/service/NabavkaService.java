package auction.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auction.model.Korisnik;
import auction.model.Ponuda;
import auction.model.ZahtevZaNabavku;
import auction.repository.ZahtevZaNabavkuRepository;

@Service
public class NabavkaService {

	@Autowired
	private ZahtevZaNabavkuRepository zahtevZaNabavkuRepository;
	
	@Autowired
	private KorisnikService korisnikService;
	
	@Autowired
	private PonudaService ponudaService;
	
	
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
	
	public ArrayList<String> odbacivanjeSuvisnih(ZahtevZaNabavku zahtev, Korisnik korisnik, ArrayList<Korisnik> firmee) {
		
		ArrayList<String> firme = new ArrayList<>();
		if (firmee.size() > zahtev.getMaxBrojPonuda()) {
			for (int i = 0; i < zahtev.getMaxBrojPonuda(); i++) {
				firme.add(firmee.get(i).getImeFirme());
			}
			return firme;
		}
		
		
		for (Korisnik firma : firmee) {
			firme.add(firma.getImeFirme());
		}
		return firme;
	}
	
	public ArrayList<String> posaljiNovimFirmama(ZahtevZaNabavku zahtev, Korisnik korisnik, ArrayList<String> firme, ArrayList<Long> ponude) {
		System.out.println("Posalji novim firmama");
		ArrayList<Long> firmeKojeSeNeUklapaju = new ArrayList<>();
		Collection<Ponuda> waitingOffers = ponudaService.getAllSentOrRefusedOffersForRequest(zahtev);
		for (Ponuda ponuda : waitingOffers) {
			firmeKojeSeNeUklapaju.add(ponuda.getFirma().getId());
		}
		
		Collection<Korisnik> firmsFromCategory = korisnikService.findByKategorijaAndIdNotIn(zahtev.getKategorija(), firmeKojeSeNeUklapaju);
		ArrayList<String> candidates = new ArrayList<>();
		
		double latitude = zahtev.getKorisnik().getLatitude();
		double longitude = zahtev.getKorisnik().getLongitude();
		
		for (Korisnik firma : firmsFromCategory) {
			Integer distance = (int) DistanceService.distance(latitude, longitude, firma.getLatitude(), firma.getLongitude(), 'K');
			if (distance > firma.getUdaljenost()) {
				continue;
			}
			candidates.add(firma.getImeFirme());
		}
		
		if (candidates.size() > zahtev.getMaxBrojPonuda()) {
			ArrayList<String> kandidati = new ArrayList<>();
			for (int i = 0; i < zahtev.getMaxBrojPonuda(); i++) {
				kandidati.add(candidates.get(i));
			}
			return kandidati;
		}
		
		return candidates;
	}
	
	public Collection<ZahtevZaNabavku> findByKorisnik(Korisnik korisnik){
		return zahtevZaNabavkuRepository.findByKorisnik(korisnik);
	}
	
	
}
