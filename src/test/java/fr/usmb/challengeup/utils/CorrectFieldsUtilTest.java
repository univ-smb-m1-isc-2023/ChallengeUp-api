package fr.usmb.challengeup.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static fr.usmb.challengeup.utils.CorrectFieldsUtil.*;

@SpringBootTest
class CorrectFieldsUtilTest {

    @Test
    public void isPasswordValidTest() {
        assertFalse(isPasswordValid(""));
        assertFalse(isPasswordValid("pomme"));
        assertTrue(isPasswordValid("mot_de_passe"));
    }

    @Test
    public void isEmailValidTest() {
        assertFalse(isEmailValid("pomme_de_terre.mail.com"));
        assertFalse(isEmailValid("fraise@mailcom"));
        assertFalse(isEmailValid(""));
        assertTrue(isEmailValid("paTaTE@mail.fr"));
    }

    @Test
    public void isUsernameValidTest() {
        assertFalse(isUsernameValid(""));
        assertTrue(isUsernameValid("a"));
        assertTrue(isUsernameValid("kfjdskfnj"));
    }

}