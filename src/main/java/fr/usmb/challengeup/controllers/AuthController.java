package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    public ResponseEntity<?> login(@RequestBody User user) {
        User foundUser = userService.getUserByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            String token = String.valueOf(foundUser.getId());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mauvais nom d'utilisateur ou mot de passe");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User newUser) {
        User userExists = userService.getUserByUsernameOrEmail(newUser.getUsername(), newUser.getEmail());
        if (userExists != null)
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Un utilisateur avec ce nom d'utilisateur ou cet email existe déjà");

        User u = userService.createUser(newUser.getUsername(), newUser.getEmail(), newUser.getPassword());
        if (u == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Les informations fournies ne sont pas valides");

        return ResponseEntity.ok(String.valueOf(u.getId()));
    }
}
