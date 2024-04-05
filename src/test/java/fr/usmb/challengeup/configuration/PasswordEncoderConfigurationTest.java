package fr.usmb.challengeup.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordEncoderConfigurationTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void passwordEncoderTest() {
        String plainTextPassword = "oui";
        String hashedPassword = passwordEncoder.encode(plainTextPassword);

        assertTrue(passwordEncoder.matches(plainTextPassword, hashedPassword));
    }

}