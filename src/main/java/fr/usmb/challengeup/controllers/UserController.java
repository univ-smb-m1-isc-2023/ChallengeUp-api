package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.entities.Progress;
import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.services.ChallengeService;
import fr.usmb.challengeup.services.ProgressService;
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
    @Autowired
    private  final ProgressService progressService;

    public UserController(UserService userService, ChallengeService challengeService, ProgressService progressService) {
        this.userService = userService;
        this.challengeService = challengeService;
        this.progressService = progressService;
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
        // Souscrire à un challenge
        User user = userService.getUserById(uid);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
        Optional<Challenge> challengeOptional = challengeService.getChallengeById(cid);
        if (challengeOptional.isPresent()) {
            // Création d'un progrès pour l'utilisateur sur ce challenge
            Progress progress = new Progress(challengeOptional.get(), user);
            progressService.createProgress(progress);
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le challenge n'existe pas.");
    }

    @PutMapping("/{uid}/unsubscribe/{cid}")
    public ResponseEntity<?> unsubscribeTo(@PathVariable long uid, @PathVariable long cid) {
        User user = userService.getUserById(uid);
        if (user == null)
            return new ResponseEntity<>("Utilisateur non trouvé", HttpStatus.NOT_FOUND);
        else {
            Optional<Challenge> challengeOptional = challengeService.getChallengeById(cid);
            if (challengeOptional.isPresent()) {
                // Suppression du progrès associé au challenge
                Challenge challenge = challengeOptional.get();
                Progress progressToDelete = progressService.getProgressByUserIdAndChallengeId(user.getId(), challenge.getId());
                if (progressToDelete != null) {
                    progressService.deleteProgressById(progressToDelete.getId());
                }
                return ResponseEntity.ok(user);
            }
            else return new ResponseEntity<>("Challenge non trouvé", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<?> watchProfile(@PathVariable String username) {
        // Consulter le profil de quelqu'un en ne renvoyant que certaines infos
        User user = userService.getUserByUsernameOrEmail(username, null);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
        else if (!user.isPublic()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ce profil n'est pas public.");
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "L'utilisateur " + id + " a été supprimé";
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
                    + " - régularité = " + u.getRegularity()
                    + "</li>";
        }

        content += "</ul>";

        if (challengeService.getAllChallenges().isEmpty()) {
            List<Challenge> challengeList = new ArrayList<>();
            User u = userService.getUserByUsernameOrEmail("titi", null);
            challengeList.add(new Challenge("Faire marcher le serveur","Sport", Challenge.Periodicity.QUOTIDIEN, "Faire le tour du pâté de maison...", u));
            challengeList.add(new Challenge("Lire 50 pages par jour","Culture", Challenge.Periodicity.QUOTIDIEN, "Choisissez un livre et lisez 50 pages chaque jour.", u));
            challengeList.add(new Challenge("Méditer pendant 15 minutes","Sport", Challenge.Periodicity.QUOTIDIEN, "Trouvez un endroit calme et méditez pendant 15 minutes.", u));
            challengeList.add(new Challenge("Apprendre une nouvelle recette","Cuisine", Challenge.Periodicity.HEBDOMADAIRE, "Choisissez une recette que vous n'avez jamais essayée et apprenez à la cuisiner.", u));
            challengeList.add(new Challenge("Écrire un journal","Culture", Challenge.Periodicity.QUOTIDIEN, "Prenez le temps de réfléchir à votre journée et écrivez vos pensées dans un journal.", u));
            challengeList.add(new Challenge("Faire 10000 pas","Sport", Challenge.Periodicity.QUOTIDIEN, "Marchez ou courez jusqu'à atteindre 10000 pas.", u));
            challengeList.add(new Challenge("Apprendre une nouvelle langue","Culture", Challenge.Periodicity.MENSUEL, "Choisissez une langue que vous aimeriez apprendre et consacrez du temps chaque jour à son apprentissage.", u));

            for (Challenge c : challengeList) {
                challengeService.createChallenge(c);
            }
        }

        return content;
    }
}
