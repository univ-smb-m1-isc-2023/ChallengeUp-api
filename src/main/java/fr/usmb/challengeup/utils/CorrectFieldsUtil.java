package fr.usmb.challengeup.utils;

/**
 * Classe de validation des champs entrés par l'utilisateur
 */
public interface CorrectFieldsUtil {
    static boolean isPasswordValid(String password) {
        return password.length() > 8;
    }

    static boolean isEmailValid(String email) {
        return email.contains("@")
                && email.contains(".")
                && email.length() >= 5;
    }

    static boolean isUsernameValid(String username) {
        return !username.isEmpty();
    }
}
