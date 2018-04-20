package auction.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import auction.model.Korisnik;
import auction.model.ZahtevZaNabavku;

public interface ZahtevZaNabavkuRepository extends JpaRepository<ZahtevZaNabavku, Long>{

	Collection<ZahtevZaNabavku> findByKorisnik(Korisnik korisnik);
	
}
