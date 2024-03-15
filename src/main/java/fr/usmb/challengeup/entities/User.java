package fr.usmb.challengeup.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    private String email;

    private String password;

    private double regularity;

    @OneToMany(mappedBy = "user")
    private Set<Challenge> challenges = new HashSet<>();

    protected User() {}
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.regularity = 0.0;
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
}
