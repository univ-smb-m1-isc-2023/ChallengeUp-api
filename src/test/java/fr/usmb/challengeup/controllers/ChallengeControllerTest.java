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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}
