package auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import auction.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
