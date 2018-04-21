package auction.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import auction.model.Ponuda;
import auction.model.ZahtevZaNabavku;

public interface PonudaRepository extends JpaRepository<Ponuda, Long>{
	
	Collection<Ponuda> findByZahtevZaNabavku(ZahtevZaNabavku zahtevZaNabavku);
	
	Collection<Ponuda> findByZahtevZaNabavkuAndStatus(ZahtevZaNabavku zahtevZaNabavku, String status);
	
	Collection<Ponuda> findByZahtevZaNabavkuAndStatusIn(ZahtevZaNabavku zahtevZaNabavku, Collection<String> statusi);
	
	Collection<Ponuda> findByZahtevZaNabavkuAndStatusOrderByPoeniDesc(ZahtevZaNabavku zahtevZaNabavku, String status);

}
