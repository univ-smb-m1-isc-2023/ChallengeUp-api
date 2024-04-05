package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void passwordEncoderTest() {
        String plainTextPassword = "oui";
        String hashedPassword = passwordEncoder.encode(plainTextPassword);

        assertTrue(passwordEncoder.matches(plainTextPassword, hashedPassword));
    }

    @Test
    public void signUp() throws Exception{
        String username = "Toto";
        String email = "toto@mail.com";
        String password = "password";

        // Si l'utilisateur n'existe pas
        when(userService.getUserByUsernameOrEmail(username, email)).thenReturn(null);
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Utilisateur créé avec succès."));

        username = "titi";
        email = "titi@mail.com";
        password = "mot_de_passe";
        User newUser = new User(username, email, password);
        // Si l'utilisateur existe
        when(userService.getUserByUsernameOrEmail(username, email)).thenReturn(newUser);
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Un utilisateur avec ce nom d'utilisateur ou cet email existe déjà"));
    }

}