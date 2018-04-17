package auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import auction.model.Korisnik;

public interface KorisnikRepository extends JpaRepository<Korisnik, String> {

}
