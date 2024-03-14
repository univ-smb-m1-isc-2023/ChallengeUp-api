package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    //public record LoginRequest(String username, String password){}

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    public ResponseEntity<?> login(@RequestBody User user) {
        User foundUser = userService.getUserByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
            String token = "FOURNIR UN VRAI TOKEN";
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Mauvais nom d'utilisateur ou mot de passe");
    }
}
