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

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

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
    public void getProgressById() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Progress progress = new Progress(challenge, user);

        when(progressService.getProgressById(anyLong())).thenReturn(Optional.of(progress));
        mockMvc.perform(get("/progress/" + progress.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed", is(false)))
                .andExpect(jsonPath("$.user.username", is("Toto")))
                .andExpect(jsonPath("$.challenge.title", is("Manger")));
    }

    @Test
    public void getProgressesByUserId() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Challenge challenge2 = new Challenge("Boire", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Challenge challenge3 = new Challenge("Dormir", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Progress p = new Progress(challenge, user);
        Progress p2 = new Progress(challenge2, user);
        Progress p3 = new Progress(challenge3, user);

        when(progressService.getProgressesByUserId(user.getId())).thenReturn(List.of(p, p2, p3));
        mockMvc.perform(get("/progress/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].completed", is(false)))
                .andExpect(jsonPath("$[1].completed", is(false)))
                .andExpect(jsonPath("$[2].completed", is(false)))
                .andExpect(jsonPath("$[0].challenge.title", is("Manger")))
                .andExpect(jsonPath("$[1].challenge.title", is("Boire")))
                .andExpect(jsonPath("$[2].challenge.title", is("Dormir")))
                .andExpect(jsonPath("$[0].user.username", is("Toto")))
                .andExpect(jsonPath("$[1].user.username", is("Toto")))
                .andExpect(jsonPath("$[2].user.username", is("Toto")));

        // Aucun progrès pour l'utilisateur
        when(progressService.getProgressesByUserId(user.getId())).thenReturn(emptyList());
        mockMvc.perform(get("/progress/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(emptyList())));
    }

    @Test
    public void getProgressesByChallengeId() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        User user2 = new User("Jean-Eudes", "Jean-Eudes@mail.com", "passwordCool*");
        User user3 = new User("Moi", "moi@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Progress p = new Progress(challenge, user);
        Progress p2 = new Progress(challenge, user2);
        Progress p3 = new Progress(challenge, user3);
        p3.setCompleted(true);

        when(progressService.getProgressesByChallengeId(challenge.getId())).thenReturn(List.of(p, p2, p3));
        mockMvc.perform(get("/progress/challenge/" + challenge.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].completed", is(false)))
                .andExpect(jsonPath("$[1].completed", is(false)))
                .andExpect(jsonPath("$[2].completed", is(true)))
                .andExpect(jsonPath("$[0].challenge.title", is("Manger")))
                .andExpect(jsonPath("$[1].challenge.title", is("Manger")))
                .andExpect(jsonPath("$[2].challenge.title", is("Manger")))
                .andExpect(jsonPath("$[0].user.username", is("Toto")))
                .andExpect(jsonPath("$[1].user.username", is("Jean-Eudes")))
                .andExpect(jsonPath("$[2].user.username", is("Moi")));

        when(progressService.getProgressesByChallengeId(challenge.getId())).thenReturn(emptyList());
        mockMvc.perform(get("/progress/challenge/" + challenge.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(emptyList())));
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

    @Test
    public void updateProgressCompleted() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Progress progress = new Progress(challenge, user);
        long pid = progress.getId();
        assertFalse(progress.isCompleted());

        when(progressService.getProgressById(pid)).thenReturn(Optional.of(progress));
        when(progressService.setIsCompleted(progress, true)).thenReturn(progress);
        progress.setCompleted(true);
        mockMvc.perform(put("/progress/complete/" + pid + "/" + true))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.completed", is(true)));
    }

    @Test
    public void updateProgressCompleted_ProgressNotFound() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Progress progress = new Progress(challenge, user);
        long pid = progress.getId();

        when(progressService.getProgressById(pid)).thenReturn(Optional.empty());
        mockMvc.perform(put("/progress/complete/" + pid + "/" + true))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Aucun progrès pour ce challenge."));
    }

    @Test
    public void updateProgressCompleted_EditionFailed() throws Exception {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);
        Progress progress = new Progress(challenge, user);
        long pid = progress.getId();

        when(progressService.getProgressById(pid)).thenReturn(Optional.of(progress));
        when(progressService.setIsCompleted(progress, true)).thenReturn(null);
        mockMvc.perform(put("/progress/complete/" + pid + "/" + true))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("L'édition du progrès a échoué."));
    }
}