package fr.usmb.challengeup.services;

import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.exceptions.UserNotFoundException;
import fr.usmb.challengeup.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User id " + id + " not found.")
        );
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(String username, String email, String password) {
        // TODO Vérifier validité mail, username ET encoder le mot de passe avec la dépendance Spring Security
        userRepository.save(new User(username, email, password));
    }
}
