package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService userService;

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password){
        userService.createUser(username, email, password);
    }
}
