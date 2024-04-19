package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.services.ChallengeService;
import fr.usmb.challengeup.services.UserService;
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
    @Autowired
    private final UserService userService;

    public ChallengeController(ChallengeService challengeService, UserService userService) {
        this.challengeService = challengeService;
        this.userService = userService;
    }

    @PostMapping("/create/{uid}")
    @ResponseStatus(HttpStatus.CREATED)
    public Challenge createChallenge(@RequestBody Challenge challenge, @PathVariable long uid) {
        User user = userService.getUserById(uid);
        challenge.setUser(user);
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

    @PutMapping("/unreport/{id}")
    public ResponseEntity<?> unreportChallenge(@PathVariable long id) {
        try {
            Challenge updatedChallenge = challengeService.updateIsReportedStatus(id, false);
            return ResponseEntity.ok(updatedChallenge);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
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
