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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void signUp() throws Exception {
        String username = "Toto";
        String email = "toto@mail.com";
        String password = "password";
        User newUser = new User(username, email, password);

        // Si l'utilisateur n'existe pas
        when(userService.getUserByUsernameOrEmail(username, email)).thenReturn(null);
        when(userService.createUser(username, email, password)).thenReturn(newUser);
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(newUser.getId())));

    }

    @Test
    public void signUp_UserExists() throws Exception {
        String username = "titi";
        String email = "titi@mail.com";
        String password = "mot_de_passe";
        User newUser = new User(username, email, password);

        // Si l'utilisateur existe
        when(userService.getUserByUsernameOrEmail(username, email)).thenReturn(newUser);
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Un utilisateur avec ce nom d'utilisateur ou cet email existe déjà"));
    }

    @Test
    public void login() throws Exception {
        String username = "Toto";
        String email = "toto@mail.com";
        String password = "password";

        User user = new User(username, email, passwordEncoder.encode(password));

        when(userService.getUserByUsernameOrEmail(username, email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void login_IncorrectPassword() throws Exception {
        String username = "Toto";
        String email = "toto@mail.com";
        String password = "password";
        User user = new User(username, email, passwordEncoder.encode(password));

        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Mauvais nom d'utilisateur ou mot de passe"));
    }

    @Test
    public void login_UserDoesntExist() throws Exception {
        String username = "Toto";
        String email = "toto@mail.com";
        String password = "password";
        User user = new User(username, email, passwordEncoder.encode(password));

        when(userService.getUserByUsernameOrEmail(username, email)).thenReturn(null);
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Mauvais nom d'utilisateur ou mot de passe"));
    }
}