package fr.usmb.challengeup.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "progress")
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date date;

    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name="challenge_id")
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Progress() {}

    public Progress(Date date, boolean isCompleted, Challenge challenge, User user) {
        this.date = date;
        this.isCompleted = isCompleted;
        this.challenge = challenge;
        this.user = user;
    }

    public Progress(Challenge challenge, User user) {
        this(new Date(), false, challenge, user);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }
}
