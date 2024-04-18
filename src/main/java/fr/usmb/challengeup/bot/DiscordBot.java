package fr.usmb.challengeup.bot;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.services.UserService;
import fr.usmb.challengeup.services.ChallengeService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.entities.User;
/* import net.dv8tion.jda.api.entities.Message;*/

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// import java.util.Optional;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
/* import java.util.concurrent.TimeUnit; */

public class DiscordBot extends ListenerAdapter {

    private boolean waitingForConfirmation = false;
    private Map<Long, Integer> tupleSpace = new HashMap<>();
    @Autowired
    private UserService userService;
    @Autowired
    private ChallengeService challengeService; // Ajoutez cette ligne
    private ArrayList<String> oui = new ArrayList<>();
    private ArrayList<String> non = new ArrayList<>();



    // Autres méthodes de classe

    public List<Challenge> getChallengeById(long userId) {
        return challengeService.getChallengesByUserId(userId);
    }

    public static void main(String[] args) throws LoginException {
        JDABuilder.createDefault("")
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new DiscordBot())
                .build();
    }

    public static char getFirstCharacter(String str) {
        return str.charAt(0);
    }
    public static String removeFirstCharacter(String str) {
        if (str != null && !str.isEmpty()) {
            return str.substring(1); // Renvoie la sous-chaîne à partir du deuxième caractère jusqu'à la fin.
        } else {
            return ("");
        }
    }

    public void initializeSomeVariables(){
        oui.add("oui");
        oui.add("o");
        oui.add("ouais");
        oui.add("ou");
        oui.add("ui");
        oui.add("yes");
        oui.add("ye");
        oui.add("true");

        non.add("non");
        non.add("nope");
        non.add("noon");
        non.add("nn");
        non.add("no");
        non.add("n");
        non.add("false");

    }

    public ArrayList<String> getListChallenges (String idUser){
        // retourne la liste des Challenges d'un User
        ArrayList<String> res = new ArrayList<>();
        return res;
    }

    public void validateChallenges(String idUser, Integer indiceChallenge){
        // Va retirer le Challenge à l'indice indiceChallenge
    }

    public void sendPrivateMessage(JDA jda, String userId, String content) {
        jda.retrieveUserById(userId).queue((user) -> {
            user.openPrivateChannel().queue((channel) -> {
                channel.sendMessage(content).queue();
            });
        });
    }

    public static ArrayList<Challenge> setToArrayList(Set<Challenge> c) {
        // Créer une nouvelle ArrayList pour stocker les challenges
        ArrayList<Challenge> challengeList = new ArrayList<>(c.size());
        for (Challenge challenge : c) {
            challengeList.add(challenge);
        }
        return challengeList;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        initializeSomeVariables();
        if (event.getAuthor().isBot()) return; // Ignore les messages provenant des bots

        String message = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        User author = event.getAuthor();
        //System.out.println("Auteur : " + author + " Message : " + message);

        //User author2 = event.getJDA().getUserById(524296395306565653); //martin
        // User author2 = event.getJDA().getUserById(312898934869590016L); //julien
        // User a = (User) event.getJDA().retrieveUserById(524296395306565653L);
        // System.out.println(a);

        //sendPrivateMessage(event.getJDA(), "524296395306565653", "prout");
        //System.out.println(author2);

        /*
        event.getJDA().retrieveUserById(524296395306565653L).queue(martin -> {
            System.out.println(martin);
        });
        */

        Challenge challengetest = new Challenge("Le titre", "le tag", null, "Ceci est la description du challenge", null);

        // ArrayList<String> challenges = getListChallenges(author.getId());
        ArrayList<Challenge> challenges = setToArrayList(userService.getChallengesByUserId(Long.parseLong(author.getId())));
        challenges.add(challengetest);
        // challenges.add("Faire les courses");
        // challenges.add("Faire le menage");
        // challenges.add("Faire a manger");

        if (!tupleSpace.containsKey(Long.valueOf(author.getId()))){
            if (message.equalsIgnoreCase("!start")) {
                List<fr.usmb.challengeup.entities.User> listUser = userService.getAllUsers();
                author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Voici les " + listUser.size() + " utilisateurs présents dans la base : ")).queue();
                for (int i = 0; i<listUser.size(); i++){
                    //author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Voici les utilisateurs présents dans la base : ")).queue();
                    System.out.println(listUser.get(i));
                }
                waitingForConfirmation = true;
                author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Voici vos challenges en attente et votre ID : " + author.getId())).queue();
                for (int i = 0; i < challenges.size(); i++){
                    int finalI = i;
                    author.openPrivateChannel().flatMap(teste -> teste.sendMessage((finalI+1) + " : " + challenges.get(finalI).getTitle() )).queue();
                }
                author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Avez vous des challenges à valider ?")).queue();
                tupleSpace.put(Long.valueOf(author.getId()), 1);
            }
        }
        else {
            System.out.println("Auteur : " + author + " Message : " + message);
            if(message.equalsIgnoreCase("annuler") || message.equalsIgnoreCase("cancel")){
                System.out.println("cancel detecté");
                author.openPrivateChannel().flatMap(teste -> teste.sendMessage("*conversation annulée*")).queue();
                tupleSpace.remove(Long.valueOf(author.getId()));
            }
            else {
                if (tupleSpace.get(Long.valueOf(author.getId()))==1){ // Etape de detection de la reponse de l'utilisateur
                    /* System.out.println("J'ai atteint la 2e etape et le message est : " + message.toLowerCase()); */
                    if (non.contains(message.toLowerCase())) {
                        System.out.println("non detecté");
                        tupleSpace.remove(Long.valueOf(author.getId()));
                        if (challenges.size()>2){
                            author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Bah qu'est ce que tu attends ? Tu as encore " + challenges.size() + " challenges à faire !!!")).queue();
                        }
                        else{
                            author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Pas de soucis, ne négligez pas le travail à faire.")).queue();
                        }
                    }
                    else if (oui.contains(message.toLowerCase())){
                        System.out.println("oui detecté");
                        tupleSpace.put(Long.valueOf(author.getId()), 2);
                        author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Très bien, lequel voulez vous valider ? (tapez 0 si vous n'en avez pas)")).queue();
                        for (int i = 0; i < challenges.size(); i++){
                            int finalI = i;
                            author.openPrivateChannel().flatMap(teste -> teste.sendMessage((finalI+1) + " : " + challenges.get(finalI).getTitle() )).queue();
                        }
                    }
                    else {
                        author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Je n'ai pas compris. Avez vous des challenges à valider ?")).queue();
                    }
                }
                else if (tupleSpace.get(Long.valueOf(author.getId()))==2){ // Etape de detection de challenges à retirer
                    // author.openPrivateChannel().flatMap(teste -> teste.sendMessage(message)).queue();
                    try {
                        int number = Integer.parseInt(message);
                        if (number==0){
                            author.openPrivateChannel().flatMap(teste -> teste.sendMessage("*conversation annulée*")).queue();
                            tupleSpace.remove(Long.valueOf(author.getId()));
                        }
                        else if (number < challenges.size()){
                            author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Le challenge " + number + " a été validé, il vous reste encore :")).queue();
                            challenges.remove(number-1);
                            for (int i = 0; i < challenges.size(); i++){
                                int finalI = i;
                                author.openPrivateChannel().flatMap(teste -> teste.sendMessage((finalI+1) + " : " + challenges.get(finalI).getTitle() )).queue();
                            }
                            author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Souhaitez vous toujours valider des challenges ?")).queue();
                            tupleSpace.put(Long.valueOf(author.getId()), 1);
                            validateChallenges(author.getId(), number);
                        }
                        else {
                            author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Veuillez rentrer un nombre compris entre 1 et " + challenges.size() + " (ou 0 si vous avez changer d'avis)")).queue();
                        }
                    } catch (NumberFormatException e) {
                        author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Veuillez rentrer un nombre compris entre 1 et " + challenges.size() + " (ou 0 si vous avez changer d'avis)")).queue();
                    }
                }
            }
        }
        //System.out.println(tupleSpace);
    }

}