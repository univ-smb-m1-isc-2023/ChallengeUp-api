package fr.usmb.challengeup.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.usmb.challengeup.entities.Challenge;
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
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", null);
        Challenge challenge2 = new Challenge("Boire", "Sport", Challenge.Periodicity.MENSUEL, "blabla", null);
        Challenge challenge3 = new Challenge("Dormir", "Sport", Challenge.Periodicity.MENSUEL, "blabla", null);

        when(challengeService.getAllChallenges()).thenReturn(List.of(challenge, challenge2, challenge3));

        mockMvc.perform(get("/challenge/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Manger")))
                .andExpect(jsonPath("$[1].title", is("Boire")))
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
}
