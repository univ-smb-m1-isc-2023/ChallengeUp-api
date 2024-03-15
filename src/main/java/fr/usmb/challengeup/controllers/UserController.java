package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password){
        userService.createUser(username, email, password);
    }
}
