package fr.usmb.challengeup.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.entities.Progress;
import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.services.ChallengeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class ChallengeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChallengeService challengeService;

    @Test
    public void createChallenge() throws Exception {
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", null);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(challenge);

        mockMvc.perform(post("/challenge/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void getAllChallenges() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", null);
        Challenge challenge2 = new Challenge("Boire", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Challenge challenge3 = new Challenge("Dormir", "Sport", Challenge.Periodicity.MENSUEL, "blabla", null);

        when(challengeService.getAllChallenges()).thenReturn(List.of(challenge, challenge2, challenge3));
        mockMvc.perform(get("/challenge/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Manger")))
                .andExpect(jsonPath("$[1].title", is("Boire")))
                .andExpect(jsonPath("$[1].user.username", is("Toto")))
                .andExpect(jsonPath("$[2].title", is("Dormir")));
    }

    @Test
    public void getChallengeById() throws Exception {
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", null);

        when(challengeService.getChallengeById(anyLong())).thenReturn(Optional.of(challenge));
        mockMvc.perform(get("/challenge/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Manger")));
    }

    @Test
    public void getChallengesByUserId() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Challenge challenge2 = new Challenge("Boire", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Challenge challenge3 = new Challenge("Dormir", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);

        when(challengeService.getChallengesByUserId(user.getId())).thenReturn(List.of(challenge, challenge2, challenge3));
        mockMvc.perform(get("/challenge/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Manger")))
                .andExpect(jsonPath("$[1].title", is("Boire")))
                .andExpect(jsonPath("$[1].user.username", is("Toto")))
                .andExpect(jsonPath("$[2].title", is("Dormir")));
    }

    @Test
    public void getChallengesByUserId_UserDoesNotExist() throws Exception {
        long nonExistentUserId = 9999L;

        when(challengeService.getChallengesByUserId(nonExistentUserId)).thenReturn(emptyList());
        mockMvc.perform(get("/challenge/user/" + nonExistentUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(emptyList())));
    }

    @Test
    public void getChallengesByUserId_UserExistsButNoChallenges() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");

        when(challengeService.getChallengesByUserId(user.getId())).thenReturn(emptyList());
        mockMvc.perform(get("/challenge/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(emptyList())));
    }

    @Test
    public void findChallengesWithHighestProgress() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Challenge challenge2 = new Challenge("Boire", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Challenge challenge3 = new Challenge("Dormir", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Progress progress = new Progress(challenge, user);
        progress.setCompleted(true);
        Progress progress2 = new Progress(challenge2, user);
        progress2.setCompleted(false);
        Progress progress3 = new Progress(challenge3, user);
        progress3.setCompleted(true);

        when(challengeService.findChallengesWithHighestProgress()).thenReturn(List.of(challenge, challenge3));
        mockMvc.perform(get("/challenge/highestProgress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Manger")))
                .andExpect(jsonPath("$[1].title", is("Dormir")));

        // Aucun challenge correspondant
        when(challengeService.findChallengesWithHighestProgress()).thenReturn(emptyList());
        mockMvc.perform(get("/challenge/highestProgress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(emptyList())));
    }
}
