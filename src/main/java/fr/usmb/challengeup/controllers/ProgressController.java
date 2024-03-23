package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.entities.Progress;
import fr.usmb.challengeup.services.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Progress createProgress(@RequestBody Progress newProgress) {
        return progressService.createProgress(newProgress);
    }
}
