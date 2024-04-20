package fr.usmb.challengeup.bot;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.entities.Progress;
import fr.usmb.challengeup.entities.User;
import fr.usmb.challengeup.services.ProgressService;
import fr.usmb.challengeup.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class ProgressChecker {
    @Autowired
    private ProgressService progressService;
    @Autowired
    private UserService userService;

    private final long FIXED_RATE = 3600 * 1000; // une heure en millisecondes

    @Transactional
    @Scheduled(fixedRate = FIXED_RATE) // exécutée toutes les heures
    public void checkProgressCompletion() {
        List<User> userList = userService.getAllUsers();

        for (User user : userList) {
            Set<Progress> progressList = user.getProgresses();
            double progressCompleted = 0.0;
            for (Progress progress : progressList) {
                Date lastCompletion = progress.getDate();
                Challenge challenge = progress.getChallenge();

                if (isChallengeCompleted(challenge, lastCompletion))
                    progressCompleted++;
                else {
                    progress.setCompleted(false);
                    progressService.editProgress(progress);
                }
            }
            user.setRegularity((progressCompleted / progressList.size()) * 100);
            userService.editUser(user);
        }
    }

    private boolean isChallengeCompleted(Challenge challenge, Date lc) {
        Calendar now = Calendar.getInstance();
        Calendar lastCompletion = Calendar.getInstance();
        now.setTime(new Date());
        lastCompletion.setTime(lc);

        switch (challenge.getPeriodicity()) {
            // sur chaque cas, on ajoute à lastCompletion la date butoir théorique impliquée par la périodicité
            // donc, pour être dans les clous, il faut que la date d'aujourd'hui ne dépasse pas cette date butoir
            case QUOTIDIEN -> {
                lastCompletion.add(Calendar.DATE, 1);
                return lastCompletion.after(now);
            }
            case HEBDOMADAIRE -> {
                lastCompletion.add(Calendar.DATE, Calendar.DAY_OF_WEEK);
                return lastCompletion.after(now);
            }
            case MENSUEL -> {
                lastCompletion.add(Calendar.MONTH, 1);
                return lastCompletion.after(now);
            }
        }
        return false;
    }
}
