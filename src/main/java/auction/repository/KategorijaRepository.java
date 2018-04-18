package auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import auction.model.Kategorija;

public interface KategorijaRepository extends JpaRepository<Kategorija, Long>{
	

}
