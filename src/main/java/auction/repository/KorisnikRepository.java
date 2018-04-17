package auction.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import auction.model.Korisnik;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {
	
	Collection<Korisnik> findByKategorija(String kategorija);

}
