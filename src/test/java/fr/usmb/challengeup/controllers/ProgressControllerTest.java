package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.services.ProgressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ProgressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgressService progressService;

    /*@Test
    public void testController throws Exception {
        mockMvc.perform(
                get("/progress/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Bienvenue dans le controller des Progrès</h1>"));
    }*/
}
