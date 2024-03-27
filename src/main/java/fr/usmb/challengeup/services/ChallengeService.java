package fr.usmb.challengeup.services;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.repositories.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
