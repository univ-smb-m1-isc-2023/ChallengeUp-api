package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.entities.Progress;
import fr.usmb.challengeup.services.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Optional<Progress> getProgressById(@PathVariable long id) {
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

    @PutMapping("/complete/{id}/{isCompleted}")
    public ResponseEntity<?> updateProgressCompleted(@PathVariable long id, @PathVariable boolean isCompleted) {
        Optional<Progress> optProgress = progressService.getProgressById(id);
        if (optProgress.isEmpty())
            return new ResponseEntity<>("Aucun progrès pour ce challenge.", HttpStatus.NOT_FOUND);
        else {
            Progress editedProgress = progressService.setIsCompleted(optProgress.get(), isCompleted);
            if (editedProgress == null)
                return new ResponseEntity<>("L'édition du progrès a échoué.", HttpStatus.INTERNAL_SERVER_ERROR);
            else return ResponseEntity.ok(editedProgress);
        }
    }

    @PutMapping("/complete/{uid}/{cid}/{isCompleted}")
    public ResponseEntity<?> updateProgressCompletedByUidAndCid(@PathVariable long uid, @PathVariable long cid, @PathVariable boolean isCompleted) {
        Progress progress = progressService.getProgressByUserIdAndChallengeId(uid, cid);
        if (progress == null)
            return new ResponseEntity<>("Aucun progrès pour ce challenge.", HttpStatus.NOT_FOUND);
        else {
            Progress editedProgress = progressService.setIsCompleted(progress, isCompleted);
            if (editedProgress == null)
                return new ResponseEntity<>("L'édition du progrès a échoué.", HttpStatus.INTERNAL_SERVER_ERROR);
            else return ResponseEntity.ok(editedProgress);
        }
    }

    @DeleteMapping("/delete/all")
    public String deleteAll() {
        progressService.deleteAllProgress();
        return "Tous vos progrès ont été supprimés";
    }
}
