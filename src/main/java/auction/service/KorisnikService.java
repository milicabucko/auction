package auction.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auction.model.Korisnik;
import auction.repository.KorisnikRepository;

@Service
public class KorisnikService {
	
	@Autowired
	public KorisnikRepository korisnikRepository;
	
	public Korisnik save(Korisnik user){
		return korisnikRepository.save(user);
	}
	
	public void delete(Korisnik korisnik) {
		korisnikRepository.delete(korisnik);
		System.out.println("Korisnik je obrisan jer nije aktivirao nalog na vreme!");
	}
	
	public void activate(Korisnik korisnik) {
		korisnik.setPotvrdjenMail(true);
		korisnikRepository.save(korisnik);
		System.out.println("Uspesno registrovan korisnik!");
	}

	public void deleteAll() {
		korisnikRepository.deleteAll();
	}
	
	public Collection<Korisnik> findByKategorija(String kategorija) {
		return korisnikRepository.findByKategorija(kategorija);
	}

	public Korisnik findOne(Long korisnikID) {
		return korisnikRepository.findOne(korisnikID);
	}

}
