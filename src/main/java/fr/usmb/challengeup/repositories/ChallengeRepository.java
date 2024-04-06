package fr.usmb.challengeup.repositories;

import fr.usmb.challengeup.entities.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query("SELECT p.challenge FROM Progress p WHERE p.isCompleted = true GROUP BY p.challenge ORDER BY COUNT(p) DESC")
    List<Challenge> findChallengesWithHighestProgress();

    List<Challenge> findByUserId(Long id);
}
