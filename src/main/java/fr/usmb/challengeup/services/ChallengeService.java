package fr.usmb.challengeup.services;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.repositories.ChallengeRepository;
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

    public Challenge createChallenge(Challenge challenge) {
        return challengeRepository.save(challenge);
    }

    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    public Optional<Challenge> getChallengeById(long id) {
        return challengeRepository.findById(id);
    }

    public List<Challenge> findChallengesWithHighestProgress() { return challengeRepository.findChallengesWithHighestProgress(); }

    public List<Challenge> getChallengesByUserId(long uid) { return  challengeRepository.findByUserId(uid); }

    @Transactional
    public Challenge updateIsReportedStatus(long id, boolean isReported) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Challenge " + id + " inexistant."));
        challenge.setReported(isReported);
        return challengeRepository.save(challenge);
    }
}
