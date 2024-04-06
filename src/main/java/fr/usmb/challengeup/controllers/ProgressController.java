package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.entities.Progress;
import fr.usmb.challengeup.services.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/progress")
public class ProgressController {
    @Autowired
    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping(value = {"", "/", "/test"})
    public String test() {
        return "<h1>Bienvenue dans le controller des Progrès</h1>";
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Progress createProgress(@RequestBody Progress newProgress) {
        return progressService.createProgress(newProgress);
    }

    @GetMapping("/{id}")
    public Optional<Progress> getChallengeById(@PathVariable long id) {
        return progressService.getProgressById(id);
    }

    @GetMapping("/user/{id}")
    public List<Progress> getProgressesByUserId(@PathVariable long id) {
        return progressService.getProgressesByUserId(id);
    }

    @GetMapping("/challenge/{id}")
    public List<Progress> getProgressesByChallengeId(@PathVariable long id) {
        return progressService.getProgressesByChallengeId(id);
    }

    @GetMapping("/user/{uid}/challenge/{cid}")
    public Progress getProgressByUserIdAndChallengeId(@PathVariable long uid, @PathVariable long cid) {
        return progressService.getProgressByUserIdAndChallengeId(uid, cid);
    }

}
