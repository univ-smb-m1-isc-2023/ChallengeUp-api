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

    public List<Progress> getAllProgress() {
        return progressRepository.findAll();
    }

    public Optional<Progress> getProgressById(Long id) {
        return progressRepository.findById(id);
    }
}
