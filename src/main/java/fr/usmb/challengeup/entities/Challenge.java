package fr.usmb.challengeup.entities;
import jakarta.persistence.*;

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
//     private Goal goal; // l'objectif du challenge (entier, booléen ...)

     public Challenge(String title, String tag, Periodicity periodicity, String description) {
          this.title = title;
          this.tag = tag;
          this.periodicity = periodicity;
          this.description = description;
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
}
