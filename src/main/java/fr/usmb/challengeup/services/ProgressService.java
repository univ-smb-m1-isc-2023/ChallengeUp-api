package fr.usmb.challengeup.services;

import fr.usmb.challengeup.entities.Progress;
import fr.usmb.challengeup.repositories.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProgressService {
    @Autowired
    private ProgressRepository progressRepository;

    public Progress createProgress(Progress progress) {
        return progressRepository.save(progress);
    }

    public List<Progress> getAllProgress() {
        return progressRepository.findAll();
    }

    public Optional<Progress> getProgressById(Long id) {
        return progressRepository.findById(id);
    }

    public List<Progress> getProgressesByUserId(long uid) { return progressRepository.findByUserId(uid); }

    public List<Progress> getProgressesByChallengeId(long cid) { return progressRepository.findByChallengeId(cid); }

    /**
     * Récupère le progrès associé à un utilisateur sur un challenge spécifique
     */
    public Progress getProgressByUserIdAndChallengeId(long uid, long cid) {
        return progressRepository.findByUserIdAndChallengeId(uid, cid);
    }

    public Progress setIsCompleted(Progress progress, boolean isCompleted) {
        progress.setCompleted(isCompleted);
        return progressRepository.save(progress);
    }
}
