package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.services.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/challenge")
public class ChallengeController {
    @Autowired
    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Challenge createChallenge(@RequestBody Challenge challenge) {
        return challengeService.createChallenge(challenge);
    }

    @GetMapping("/all")
    public List<Challenge> getAllChalenge() {
        return challengeService.getAllChallenge();
    }

    @GetMapping("/{id}")
    public Optional<Challenge> getChallengeById(@PathVariable long id) {
        return challengeService.getChallengeById(id);
    }
}
