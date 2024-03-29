package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public void createUser(@PathVariable String username,
                           @PathVariable String email,
                           @PathVariable String password){
        userService.createUser(username, email, password);
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/challenges/{id}")
    public List<Challenge> getChallengesByUserId(@PathVariable long id) {
        User user = userService.getUserById(id);
        return new ArrayList<>(user.getChallenges());
    }

    @GetMapping(value = {"", "/", "/test"})
    public String test() {
        if (userService.getUserByUsernameOrEmail("titi", null) == null)
            userService.createUser("titi", "titi@mail.com", "mot_de_passe");
        return "<h1>Bienvenue dans le controller des utilisateurs </h1>" +
                "<p>Informations</p>" +
                "<ul><li> Utilisateurs : " + userService.getAllUsers().isEmpty() + "</li>" +
                "<li>" + userService.getAllUsers().get(0).getUsername() + "</li>" +
                "</ul>";
    }
}
