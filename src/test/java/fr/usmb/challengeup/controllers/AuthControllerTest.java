package fr.usmb.challengeup.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthControllerTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    void passwordEncoderTest() {
        String plainTextPassword = "oui";
        String hashedPassword = passwordEncoder.encode(plainTextPassword);

        assertTrue(passwordEncoder.matches(plainTextPassword, hashedPassword));
    }

}