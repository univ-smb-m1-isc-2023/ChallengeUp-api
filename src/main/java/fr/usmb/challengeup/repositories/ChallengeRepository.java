package fr.usmb.challengeup.repositories;

import fr.usmb.challengeup.entities.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
