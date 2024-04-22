package fr.usmb.challengeup.bot;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.entities.Progress;
import fr.usmb.challengeup.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProgressCheckerTest {
    @InjectMocks
    private ProgressChecker progressChecker;
    @Mock
    private Challenge challenge;
    @Mock
    private Progress progress;
    @Mock
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void isChallengeCompleted() {
        Set<Progress> progressSet = new HashSet<>();
        progressSet.add(progress);
        when(user.getProgresses()).thenReturn(progressSet);
        when(progress.getChallenge()).thenReturn(challenge);
        when(progress.getDate()).thenReturn(new Date());

        // Test QUOTIDIEN
        when(challenge.getPeriodicity()).thenReturn(Challenge.Periodicity.QUOTIDIEN);
        assertTrue(progressChecker.isChallengeCompleted(challenge, progress.getDate()));

        // Test HEBDOMADAIRE
        when(challenge.getPeriodicity()).thenReturn(Challenge.Periodicity.HEBDOMADAIRE);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -8); // 8 jours en arrière
        when(progress.getDate()).thenReturn(cal.getTime());
        assertFalse(progressChecker.isChallengeCompleted(challenge, progress.getDate()));

        // Test MENSUEL
        when(challenge.getPeriodicity()).thenReturn(Challenge.Periodicity.MENSUEL);
        cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1); // 1 mois en arrière
        when(progress.getDate()).thenReturn(cal.getTime());
        assertFalse(progressChecker.isChallengeCompleted(challenge, progress.getDate()));
    }
}