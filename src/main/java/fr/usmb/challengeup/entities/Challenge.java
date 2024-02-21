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
}
