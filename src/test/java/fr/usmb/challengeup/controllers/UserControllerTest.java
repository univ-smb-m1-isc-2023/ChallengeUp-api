package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.services.ChallengeService;
import fr.usmb.challengeup.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private ChallengeService challengeService;

   @Test
    public void createUser() throws Exception {
        String username = "Toto";
        String email = "toto@mail.com";
        String password = "password";

        mockMvc.perform(post("/user/create")
                .param("username", username)
                .param("email", email)
                .param("password", password))
                .andExpect(status().isCreated());

        verify(userService, times(1))
                .createUser(username, email, password);
    }

    @Test
    public void getAllUsers() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        User user2 = new User("Jean-Eudes", "Jean-Eudes@mail.com", "passwordCool*");
        User user3 = new User("Moi", "moi@mail.com", "passwordCool*");

        when(userService.getAllUsers()).thenReturn(List.of(user, user2, user3));
        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is("Toto")))
                .andExpect(jsonPath("$[1].username", is("Jean-Eudes")))
                .andExpect(jsonPath("$[2].username", is("Moi")));

        // Aucun utilisateur dans la base
        when(userService.getAllUsers()).thenReturn(emptyList());
        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(emptyList())));
    }

    @Test
    public void getUserById() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");

        when(userService.getUserById(user.getId())).thenReturn(user);
        mockMvc.perform(get("/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("Toto")));

        // Utilisateur non trouvé
        long nonExistentUserId = 9999L;
        when(userService.getUserById(nonExistentUserId)).thenReturn(null);
        mockMvc.perform(get("/user/" + nonExistentUserId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void toggleUserPublic() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        long uid = user.getId();

        assertFalse(user.isPublic());

        mockMvc.perform(put("/user/public/" + uid))
                .andExpect(status().isOk());

        verify(userService, times(1)).toggleUserPublic(uid);
    }

    @Test
    public void subscribeTo() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        long uid = user.getId();
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        long cid = challenge.getId();

        when(userService.getUserById(uid)).thenReturn(user);
        when(challengeService.getChallengeById(cid)).thenReturn(Optional.of(challenge));
        mockMvc.perform(put("/user/" + uid + "/subscribe/" + cid))
                .andExpect(status().isOk());
    }

    @Test
    public void subscribeTo_ChallengeNotFound() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        long uid = user.getId();
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        long cid = challenge.getId();

        when(userService.getUserById(uid)).thenReturn(user);
        when(challengeService.getChallengeById(cid)).thenReturn(Optional.empty());
        mockMvc.perform(put("/user/" + uid + "/subscribe/" + cid))
                .andExpect(status().isNotFound());
    }

    @Test
    public void subscribeTo_UserNotFound() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        long uid = user.getId();
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        long cid = challenge.getId();

        when(userService.getUserById(uid)).thenReturn(null);
        when(challengeService.getChallengeById(cid)).thenReturn(Optional.of(challenge));
        mockMvc.perform(put("/user/" + uid + "/subscribe/" + cid))
                .andExpect(status().isNotFound());
    }

    @Test
    public void watchProfile() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        user.setPublic(true);

        when(userService.getUserByUsernameOrEmail(user.getUsername(), null)).thenReturn(user);
        mockMvc.perform(get("/user/profile/" + user.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("Toto")));
    }

    @Test
    public void watchProfile_UserNotFound() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");

        when(userService.getUserByUsernameOrEmail(user.getUsername(), null)).thenReturn(null);
        mockMvc.perform(get("/user/profile/" + user.getUsername()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Utilisateur non trouvé"));
    }

    @Test
    public void watchProfile_UserNotPublic() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");

        when(userService.getUserByUsernameOrEmail(user.getUsername(), null)).thenReturn(user);
        mockMvc.perform(get("/user/profile/" + user.getUsername()))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Ce profil n'est pas public."));
    }
}