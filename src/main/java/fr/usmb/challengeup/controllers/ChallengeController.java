package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.services.ChallengeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Challenge> getAllChallenges() {
        return challengeService.getAllChallenges();
    }

    @GetMapping("/{id}")
    public Optional<Challenge> getChallengeById(@PathVariable long id) {
        return challengeService.getChallengeById(id);
    }

    @GetMapping("/user/{id}")
    public List<Challenge> getChallengesByUserId(@PathVariable long id) { return challengeService.getChallengesByUserId(id); }

    @GetMapping("/highestProgress")
    public List<Challenge> findChallengesWithHighestProgress() {
        return challengeService.findChallengesWithHighestProgress();
    }

    @PutMapping("/report/{id}")
    public ResponseEntity<?> reportChallenge(@PathVariable long id) {
        try {
            Challenge updatedChallenge = challengeService.updateIsReportedStatus(id, true);
            return ResponseEntity.ok(updatedChallenge);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping(value = {"", "/", "/test"})
    public String test() {
        return "<h1>Bienvenue dans le controller des challenges </h1>";
    }

    @DeleteMapping("/delete/all")
    public String deleteAll() {
        challengeService.deleteAllChallenges();
        return "Tous vos challenges ont été supprimés.";
    }
}
