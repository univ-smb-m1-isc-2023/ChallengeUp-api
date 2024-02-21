package fr.usmb.challengeup.repositories;


import fr.usmb.challengeup.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
//    @Override
//    List<User> findAll();
}
