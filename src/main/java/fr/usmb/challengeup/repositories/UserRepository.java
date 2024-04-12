package fr.usmb.challengeup.repositories;


import fr.usmb.challengeup.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isPublic = CASE u.isPublic WHEN true THEN false ELSE true END WHERE u.id = ?1")
    void toggleUserPublic(Long id);
}
