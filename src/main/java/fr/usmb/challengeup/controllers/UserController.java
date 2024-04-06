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
    public void createUser(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password){
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

    @GetMapping(value = {"", "/", "/test"})
    public String test() {
        String content = "";
        if (userService.getUserByUsernameOrEmail("titi", null) == null)
            userService.createUser("titi", "titi@mail.com", "mot_de_passe");
        content += "<h1>Bienvenue dans le controller des utilisateurs </h1>" +
                "<p>Informations</p>" + "<p>Utilisateurs</p>" ;

        List<User> userList = userService.getAllUsers();
        content += "<ul><li> Base utilisateur vide : " + userList.isEmpty() + "</li>";

        for (User u : userList) {
            content += "<li>" + u.getUsername() + " - " + u.getEmail() + "</li>";
        }

        content += "</ul>";

        return content;
    }
}
