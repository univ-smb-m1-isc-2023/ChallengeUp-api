package fr.usmb.challengeup.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    @GetMapping(value = {"", "/", "/test"})
    public String test() {
        return "<h1>Bienvenue dans le controller des Progrès</h1>";
    }
}
