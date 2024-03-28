package fr.usmb.challengeup.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String username;

    private String email;

    private String password;

    private double regularity;

    private boolean isPublic;

    @OneToMany(mappedBy = "user")
    private Set<Challenge> challenges = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Progress> progresses = new HashSet<>();

    protected User() {}
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.regularity = 0.0;
        this.isPublic = false;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getRegularity() { return  regularity; }

    public void setRegularity(double regularity) {
        this.regularity = regularity;
    }

    public Set<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(Set<Challenge> challenges) {
        this.challenges = challenges;
    }

    public Set<Progress> getProgresses() {
        return progresses;
    }

    public void setProgresses(Set<Progress> progresses) {
        this.progresses = progresses;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
