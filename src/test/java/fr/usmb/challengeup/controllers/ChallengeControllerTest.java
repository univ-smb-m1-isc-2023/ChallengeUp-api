package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.services.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
public class ChallengeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChallengeService challengeService;
}
