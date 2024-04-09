package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.services.ChallengeService;
import fr.usmb.challengeup.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private final UserService userService;

    @Autowired
    private final ChallengeService challengeService;

    public UserController(UserService userService, ChallengeService challengeService) {
        this.userService = userService;
        this.challengeService = challengeService;
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

    @PutMapping("/public/{id}")
    public void toggleUserPublic(@PathVariable long id) { userService.toggleUserPublic(id); }

    @PutMapping("/{uid}/subscribe/{cid}")
    public ResponseEntity<?> subscribeTo(@PathVariable long uid, @PathVariable long cid) {
        User user = userService.getUserById(uid);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
        Set<Challenge> challenges = user.getChallenges();
        Optional<Challenge> challengeOptional = challengeService.getChallengeById(cid);
        if (challengeOptional.isPresent()) {
            challenges.add(challengeOptional.get());
            user.setChallenges(challenges);
            return ResponseEntity.ok(userService.editUser(user));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le challenge n'existe pas.");
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
            content += "<li>" + u.getUsername() + " - " + u.getEmail()
                    + " - ID = " + u.getId()
                    + " - public = " + u.isPublic()
                    + "</li>";
        }

        content += "</ul>";

        return content;
    }
}
