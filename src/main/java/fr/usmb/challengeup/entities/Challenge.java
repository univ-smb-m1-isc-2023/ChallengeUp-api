package fr.usmb.challengeup.entities;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Challenge {
     public enum Periodicity {
          QUOTIDIEN,
          HEBDOMADAIRE,
          MENSUEL
     }
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private long id;

     private String title;
     private String tag;
     private Periodicity periodicity;
     private String description;
     private boolean isReported;

     @ManyToOne
     @JoinColumn(name = "user_id")
     private User user;

     @OneToMany(mappedBy = "challenge")
     private Set<Progress> progresses = new HashSet<>();
//     private Goal goal; // l'objectif du challenge (entier, booléen ...)

     public Challenge() {}

     public Challenge(String title, String tag, Periodicity periodicity, String description) {
          this.title = title;
          this.tag = tag;
          this.periodicity = periodicity;
          this.description = description;
          this.isReported = false;
     }

     public String getTitle() {
          return title;
     }

     public void setTitle(String title) {
          this.title = title;
     }

     public String getTag() {
          return tag;
     }

     public void setTag(String tag) {
          this.tag = tag;
     }

     public Periodicity getPeriodicity() {
          return periodicity;
     }

     public void setPeriodicity(Periodicity periodicity) {
          this.periodicity = periodicity;
     }

     public String getDescription() {
          return description;
     }

     public void setDescription(String description) {
          this.description = description;
     }

     public boolean isReported() {
          return isReported;
     }

     public void setReported(boolean reported) {
          isReported = reported;
     }

     public User getUser() {
          return user;
     }

     public void setUser(User user) {
          this.user = user;
     }

     public Set<Progress> getProgresses() {
          return progresses;
     }

     public void setProgresses(Set<Progress> progresses) {
          this.progresses = progresses;
     }
}
