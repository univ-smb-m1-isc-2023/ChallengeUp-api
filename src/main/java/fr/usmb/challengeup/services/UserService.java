package fr.usmb.challengeup.services;

import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.exceptions.UserNotFoundException;
import fr.usmb.challengeup.repositories.UserRepository;
import fr.usmb.challengeup.utils.CorrectFieldsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User id " + id + " not found.")
        );
    }

    public User getUserByUsernameOrEmail(String username, String email) {
        if (username != null)
            return userRepository.findByUsername(username);
        else
            return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Sauvegarde un utilisateur et encode le mot de passe
     */
    public User createUser(String username, String email, String password) {
        if (CorrectFieldsUtil.isUsernameValid(username)
                && CorrectFieldsUtil.isEmailValid(email)
                && CorrectFieldsUtil.isPasswordValid(password)) {
            return userRepository.save(new User(username, email, passwordEncoder.encode(password)));
        } else return null;
    }

    /**
     * Sauvegarde l'objet utilisateur en base. Cela suppose que l'utilisateur existe déjà et qu'on a
     * modifié quelque chose sur lui.
     */
    public User editUser(User user) {
        return userRepository.save(user);
    }

    public void toggleUserPublic(Long id) {
        userRepository.toggleUserPublic(id);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
