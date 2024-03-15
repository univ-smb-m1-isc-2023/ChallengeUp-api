package fr.usmb.challengeup.services;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.repositories.ChallengeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChallengeService {
    private ChallengeRepository challengeRepository;

    public Challenge createChallenge(Challenge challenge) {
        return challengeRepository.save(challenge);
    }

    public List<Challenge> getAllChallenge() {
        return challengeRepository.findAll();
    }

    public Optional<Challenge> getChallengeById(long id) {
        return challengeRepository.findById(id);
    }
}
