package fr.usmb.challengeup.services;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.entities.Progress;
import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.repositories.ChallengeRepository;
import fr.usmb.challengeup.repositories.ProgressRepository;
import fr.usmb.challengeup.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ChallengeServiceTest {

    @InjectMocks
    ChallengeService challengeService;
    @Mock
    ChallengeRepository challengeRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ProgressRepository progressRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
    public void createChallenge() {
        User user = new User("Toto", "toto@mail.com", "passwordCool*");
        Challenge challenge = new Challenge("Manger", "Sport", Challenge.Periodicity.MENSUEL, "blabla", user);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(challengeRepository.save(challenge)).thenReturn(challenge);

        Challenge createdChallenge = new Challenge(challenge.getTitle(), challenge.getTag(), challenge.getPeriodicity(), challenge.getDescription(), user);
        when(challengeService.createChallenge(challenge)).thenReturn(createdChallenge);

        verify(challengeRepository, times((1))).save(challenge);
        verify(progressRepository, times(1)).save(any(Progress.class));
        assertEquals(challenge.getTitle(), createdChallenge.getTitle());
        assertEquals(challenge.getTag(), createdChallenge.getTag());
        assertEquals(challenge.getPeriodicity(), createdChallenge.getPeriodicity());
        assertEquals(challenge.getDescription(), createdChallenge.getDescription());
        assertEquals(challenge.getUser(), createdChallenge.getUser());
    }*/
}