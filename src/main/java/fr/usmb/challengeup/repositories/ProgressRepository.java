package fr.usmb.challengeup.repositories;

import fr.usmb.challengeup.entities.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByUserId(Long userId);
    List<Progress> findByChallengeId(Long challengeId);
    Progress findByUserIdAndChallengeId(Long userId, Long challengeId);
}
