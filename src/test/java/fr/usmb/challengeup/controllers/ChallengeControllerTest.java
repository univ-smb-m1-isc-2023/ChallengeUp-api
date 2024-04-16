package fr.usmb.challengeup.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.entities.Progress;
import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.services.ChallengeService;
import jakarta.persistence.EntityNotFoundException;
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
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ChallengeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChallengeService challengeService;

    @Test
    public void createChallenge() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(challenge);

        when(challengeService.createChallenge(challenge)).thenReturn(challenge);
        mockMvc.perform(post("/challenge/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        when(challengeService.createChallenge(challenge)).thenReturn(null);
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
                // .andExpect(jsonPath("$[1].user.username", is("Toto"))) // avant le @JsonIgnore
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
                // .andExpect(jsonPath("$[1].user.username", is("Toto"))) avant le @JsonIgnore
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

    @Test
    public void reportChallenge() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        challenge.setReported(true);
        long cid = challenge.getId();

        when(challengeService.updateIsReportedStatus(cid, true)).thenReturn(challenge);
        mockMvc.perform(put("/challenge/report/" + cid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reported", is(true)));
        verify(challengeService, times(1)).updateIsReportedStatus(cid, true);
    }

    @Test
    public void reportChallenge_ChallengeNotFound() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        challenge.setReported(true);
        long cid = challenge.getId();

        when(challengeService.updateIsReportedStatus(cid, true))
                .thenThrow(new EntityNotFoundException("Challenge " + cid + " inexistant."));
        mockMvc.perform(put("/challenge/report/" + cid))
                .andExpect(status().isNotFound());
        verify(challengeService, times(1)).updateIsReportedStatus(cid, true);
    }

    @Test
    public void deleteAll() throws Exception {
        mockMvc.perform(get("/challenge/delete/all"))
                .andExpect(status().isOk());

        verify(challengeService, times(1)).deleteAllChallenges();
    }
}
