package auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import auction.model.Ponuda;

public interface PonudaRepository extends JpaRepository<Ponuda, Long>{

}
