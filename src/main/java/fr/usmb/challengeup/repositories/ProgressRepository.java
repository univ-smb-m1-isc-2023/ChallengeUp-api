package fr.usmb.challengeup.repositories;

import fr.usmb.challengeup.entities.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
}
