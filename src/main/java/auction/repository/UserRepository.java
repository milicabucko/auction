package auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import auction.model.User;

public interface UserRepository extends JpaRepository<User, String> {

}
