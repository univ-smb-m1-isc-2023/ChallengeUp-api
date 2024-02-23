package fr.usmb.challengeup.services;

import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.exceptions.UserNotFoundException;
import fr.usmb.challengeup.repositories.UserRepository;
import fr.usmb.challengeup.utils.CorrectFieldsUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User id " + id + " not found.")
        );
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(String username, String email, String password) {
        if (CorrectFieldsUtil.isUsernameValid(username)
                && CorrectFieldsUtil.isEmailValid(email)
                && CorrectFieldsUtil.isPasswordValid(password)) {
            userRepository.save(new User(username, email, passwordEncoder.encode(password)));
        }
    }
}
