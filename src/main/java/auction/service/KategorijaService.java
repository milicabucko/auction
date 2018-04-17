package auction.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auction.model.Kategorija;
import auction.model.Korisnik;
import auction.repository.KategorijaRepository;

@Service
public class KategorijaService {

	@Autowired
	private KategorijaRepository kategorijaRepository;
	
	public Collection<Kategorija> findAll() {
		return kategorijaRepository.findAll();
	}
	
	public void deleteAll() {
		kategorijaRepository.deleteAll();
	}
	
	public Kategorija save(Kategorija kategorija) {
		return kategorijaRepository.save(kategorija);
	}
	
}
