package fr.usmb.challengeup.services;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.entities.Progress;
import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.repositories.ChallengeRepository;
import fr.usmb.challengeup.repositories.ProgressRepository;
import fr.usmb.challengeup.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ChallengeService {
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProgressRepository progressRepository;

    @Transactional
    public Challenge createChallenge(Challenge challenge) {
        User user = userRepository.findById(challenge.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur " + challenge.getUser().getId() + " introuvable."));
        Challenge newChallenge = new Challenge(challenge.getTitle(),
                challenge.getTag(),
                challenge.getPeriodicity(),
                challenge.getDescription(),
                user);

        Challenge savedChallenge = challengeRepository.save(newChallenge);
        progressRepository.save(new Progress(savedChallenge, user));

        return savedChallenge;
    }

    @Transactional
    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    @Transactional
    public Optional<Challenge> getChallengeById(long id) {
        return challengeRepository.findById(id);
    }

    public List<Challenge> findChallengesWithHighestProgress() { return challengeRepository.findChallengesWithHighestProgress(); }

    public List<Challenge> getChallengesByUserId(long uid) { return  challengeRepository.findByUserId(uid); }

    public void deleteAllChallenges() {
        challengeRepository.deleteAll();
    }

    @Transactional
    public Challenge updateIsReportedStatus(long id, boolean isReported) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Challenge " + id + " inexistant."));
        challenge.setReported(isReported);
        return challengeRepository.save(challenge);
    }
}
