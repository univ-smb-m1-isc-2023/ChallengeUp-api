package fr.usmb.challengeup.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.entities.Progress;
import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.services.ProgressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class ProgressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgressService progressService;

    @Test
    public void createProgress() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", null);
        Progress progress = new Progress(challenge, user);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(progress);

        mockMvc.perform(post("/progress/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void getProgressByUserIdAndChallengeId() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Progress progress = new Progress(challenge, user);

        when(progressService.getProgressByUserIdAndChallengeId(user.getId(), challenge.getId())).thenReturn(progress);
        mockMvc.perform(get("/progress/user/" + user.getId() + "/challenge/" + challenge.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) progress.getId())));
    }
}
