package fr.usmb.challengeup.bot;

import fr.usmb.challengeup.entities.Challenge;
import fr.usmb.challengeup.entities.Progress;
import fr.usmb.challengeup.services.ProgressService;
import fr.usmb.challengeup.services.UserService;
import fr.usmb.challengeup.services.ChallengeService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.ArrayList;
import java.util.Comparator;

@Component
public class DiscordBot extends ListenerAdapter {

    private boolean waitingForConfirmation = false;
    private Map<Long, Integer> tupleSpace = new HashMap<>();
    private JDA jda;
    @Autowired
    private UserService userService;
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private ProgressService progressService;
    private static ArrayList<String> oui = new ArrayList<>();
    private static ArrayList<String> non = new ArrayList<>();
    private static ArrayList<Challenge> challenges = new ArrayList<Challenge>();
    private static String bottoken = "";
    private List<Progress> userProgresses = null;

    // Autres méthodes de classe

    public List<Challenge> getChallengeById(long userId) {
        return challengeService.getChallengesByUserId(userId);
    }

    public static void initializeSomeVariables(){
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
        // retourne la liste des d'un User
        ArrayList<String> res = new ArrayList<>();
        return res;
    }

    public Integer indiceChallengeInProgressesList(List<Progress> uP, Integer indiceChallenge){
        int i = 0;
        for (int j = 0; j < uP.size(); j++){
            if(!uP.get(j).isCompleted()){
                i++;
                if (i == indiceChallenge){
                    return j;
                }
            }
        }
        return -1;
    }

    public void sendPrivateMessage(String userId, String content) {
        jda.retrieveUserById(userId).queue((user) -> {
            user.openPrivateChannel().queue((channel) -> {
                channel.sendMessage(content).queue();
            });
        });
    }

    public List<Challenge> getChallengesNotCompletedByUserId(long uid) {
        List<Progress> progressList = progressService.getProgressesByUserId(uid);
        List<Challenge> challengesNotCompleted = new ArrayList<>();
        for (Progress progress : progressList) {
            if (!progress.isCompleted()) // simulation de !isChallengeCompleted de ProgressChecker
                challengesNotCompleted.add(progress.getChallenge());
        }
        return challengesNotCompleted;
    }

    public Progress getMostRecentProgress(List<Progress> progressList) {
        return progressList.stream()
                           .filter(p -> !p.isCompleted())
                           .max(Comparator.comparing(Progress::getDate))
                           .orElse(null);
    }

    public boolean needNotification(List<Progress> pl, Integer heure){
        Progress p = getMostRecentProgress(pl);
        Date dateProgress = p.getDate();
        Date dateActuelle = new Date();
        long differenceEnMillisecondes = dateActuelle.getTime() - dateProgress.getTime();
        long differenceEnHeures = differenceEnMillisecondes / (60 * 60 * 1000);
        return differenceEnHeures >= heure;
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
        if (event.getAuthor().isBot()) return; // Ignore les messages provenant des bots

        String message = (String) event.getMessage().getContentRaw().toLowerCase().replaceAll("\\s", "");
        MessageChannel channel = event.getChannel();
        User author = event.getAuthor();
        String authorId = author.getId();
        jda = event.getJDA();
        List<Progress> userProgresses = progressService.getProgressesByUserId(/* Mettre ici l'id d'un user existant (pas discord id) */ 1);
        challenges.clear();
        for (int i = 0; i < userProgresses.size(); i++){
            if(!userProgresses.get(i).isCompleted()){
                challenges.add(userProgresses.get(i).getChallenge());
            }
        }

        if (!tupleSpace.containsKey(Long.valueOf(author.getId()))){
            if (message.equalsIgnoreCase("!start")) {
                // List<fr.usmb.challengeup.entities.User> listUser = userService.getAllUsers();
                // author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Voici les " + listUser.size() + " utilisateurs présents dans la base : ")).queue();
                // for (int i = 0; i<listUser.size(); i++){
                //     //author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Voici les utilisateurs présents dans la base : ")).queue();
                //     System.out.println(listUser.get(i));
                // }
                waitingForConfirmation = true;
                if (!challenges.isEmpty()){
                    author.openPrivateChannel().flatMap(chat -> chat.sendMessage("Voici vos challenges en attente et votre ID : " + author.getId())).queue();
                    for (int i = 0; i < challenges.size(); i++){
                        int finalI = i;
                        author.openPrivateChannel().flatMap(chat -> chat.sendMessage((finalI+1) + " : " + challenges.get(finalI).getTitle() )).queue();
                    }
                    author.openPrivateChannel().flatMap(chat -> chat.sendMessage("Avez vous des challenges à valider ?")).queue();
                    tupleSpace.put(Long.valueOf(author.getId()), 1);
                }
                else{
                    sendPrivateMessage(authorId, "Voici vos challenges non complétés : ");
                    long fakeUserId = 1;
                    List<Challenge> challengesNotCompleted = getChallengesNotCompletedByUserId(fakeUserId);
                    for (int i = 0; i < challengesNotCompleted.size(); i++) {
                        Challenge challenge = challengesNotCompleted.get(i);
                        sendPrivateMessage(author.getId(), (i + 1) + " - " + challenge.getTitle());
                    }
                    author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Vous êtes actuellement à jour sur vos challenges. Bravo :thumbsup: !!!")).queue();
                    tupleSpace.remove(Long.valueOf(author.getId()));
                }

            }
        }
        else {
            // System.out.println("Auteur : " + author + " Message : " + message);
            if(message.equalsIgnoreCase("annuler") || message.equalsIgnoreCase("cancel")){
                System.out.println("cancel detecté");
                author.openPrivateChannel().flatMap(chat -> chat.sendMessage("*conversation annulée*")).queue();
                tupleSpace.remove(Long.valueOf(author.getId()));
            }
            else {
                if (tupleSpace.get(Long.valueOf(author.getId()))==1){ // Etape de detection de la reponse de l'utilisateur
                    //System.out.println("J'ai atteint la 2e etape et le message est : " + message .toLowerCase() + "de type : " + message.getClass().getName());
                    if (non.contains(message.toLowerCase())) {
                        // System.out.println("non detecté");
                        tupleSpace.remove(Long.valueOf(author.getId()));
                        if (challenges.size()>2){
                            author.openPrivateChannel().flatMap(chat -> chat.sendMessage("Bah qu'est ce que tu attends ? Tu as encore " + challenges.size() + " challenges à faire !!!")).queue();
                        }
                        else{
                            author.openPrivateChannel().flatMap(chat -> chat.sendMessage("Pas de soucis, ne négligez pas le travail à faire.")).queue();
                        }
                    }
                    else if (oui.contains(message.toLowerCase())){
                        tupleSpace.put(Long.valueOf(author.getId()), 2);
                        author.openPrivateChannel().flatMap(chat -> chat.sendMessage("Très bien, lequel voulez vous valider ? (tapez 0 si vous n'en avez pas)")).queue();
                        for (int i = 0; i < challenges.size(); i++){
                            int finalI = i;
                            author.openPrivateChannel().flatMap(chat -> chat.sendMessage((finalI+1) + " : " + challenges.get(finalI).getTitle() )).queue();
                        }
                    }
                    else {
                        author.openPrivateChannel().flatMap(chat -> chat.sendMessage("Je n'ai pas compris. Avez vous des challenges à valider ?")).queue();
                    }
                }
                else if (tupleSpace.get(Long.valueOf(author.getId()))==2){ // Etape de detection de challenges à retirer
                    // author.openPrivateChannel().flatMap(teste -> teste.sendMessage(message)).queue();
                    try {
                        int number = Integer.parseInt(message);
                        if (number==0){
                            author.openPrivateChannel().flatMap(chat -> chat.sendMessage("*conversation annulée*")).queue();
                            tupleSpace.remove(Long.valueOf(author.getId()));
                        }
                        else if (number <= challenges.size()){
                            author.openPrivateChannel().flatMap(chat -> chat.sendMessage("Le challenge " + number + " a bien été validé.")).queue();
                            challenges.remove(number-1);
                            progressService.setIsCompleted(userProgresses.get(indiceChallengeInProgressesList(userProgresses,number)), true);
                            if (challenges.size()==0){
                                author.openPrivateChannel().flatMap(chat -> chat.sendMessage("Vous n'avez plus de challenge à valider. Félicitations !!!")).queue();
                                tupleSpace.remove(Long.valueOf(author.getId()));
                            }
                            else{
                                author.openPrivateChannel().flatMap(chat -> chat.sendMessage("Souhaitez vous toujours valider des challenges ?")).queue();
                                tupleSpace.put(Long.valueOf(author.getId()), 1);
                            }

                        }
                        else {
                            author.openPrivateChannel().flatMap(chat -> chat.sendMessage("Veuillez rentrer un nombre compris entre 1 et " + challenges.size() + " (ou 0 si vous avez changer d'avis)")).queue();
                        }
                    } catch (NumberFormatException e) {
                        author.openPrivateChannel().flatMap(chat -> chat.sendMessage("Veuillez rentrer un nombre compris entre 1 et " + challenges.size() + " (ou 0 si vous avez changer d'avis)")).queue();
                    }
                }
            }
        }
        //System.out.println(tupleSpace);
    }

    //////////////////
    /////// V1 ///////
    //////////////////

    // @Override
    // public void onReady(ReadyEvent event) {
    //     initializeSomeVariables();
    //     JDA jda = event.getJDA();
    //     User user = jda.retrieveUserById(692668155327152149L).complete(); // ID de l'utilisateur Théo
    //     // User user = jda.retrieveUserById(524296395306565653L).complete(); // ID de l'utilisateur Julien
    //     // List<Challenge> listOfChallengesNotCompleted = getChallengesNotCompletedByUserId(user.getIdLong());
    //     // List<Challenge> listOfChallengesNotCompleted = getChallengesNotCompletedByUserId(/* Mettre ici l'id d'un user existant (pas discord id) */ 1);
    //     int minutes = 5;

    //     Timer timer = new Timer();
    //     timer.scheduleAtFixedRate(new TimerTask() {
    //         @Override
    //         public void run() {
    //             List<fr.usmb.challengeup.entities.User> listUser = userService.getAllUsers();
    //             for (int i = 0; i<listUser.size(); i++){
    //                 fr.usmb.challengeup.entities.User userFromList = listUser.get(i);
    //                 if (userFromList != null && user != null) {
    //                     // // à décommenter si on a discordID dans la table User 
    //                     // user = jda.retrieveUserById(userFromList.discordID).complete();
    //                     user.openPrivateChannel().queue(privateChannel -> {
    //                         privateChannel.sendMessage("Bonjour " + user.getAsTag() + ", ceci est un message automatique qui vous rappel que vous avez toujours des challenges en cours. Souhaitez vous en valider quelques un ?").queue();
    //                         tupleSpace.put(Long.valueOf(user.getId()), 1);
    //                     System.out.println("Message envoyé à " + user.getAsTag() + " et son nom dans la base est : " + userFromList.getUsername());
    //                     });
    //                 } else {
    //                     System.out.println("Utilisateur non trouvé.");
    //                 }
    //             }
    //         }
    //     }, 0, minutes * 60 * 1000);
    // }

    //////////////////
    /////// V2 ///////
    //////////////////

    @Override
    public void onReady(ReadyEvent event) {
        initializeSomeVariables();
        JDA jda = event.getJDA();
        User user = jda.retrieveUserById(692668155327152149L).complete(); // ID de l'utilisateur Théo
        // User user = jda.retrieveUserById(524296395306565653L).complete(); // ID de l'utilisateur Julien
        int minutes = 5;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<fr.usmb.challengeup.entities.User> listUser = userService.getAllUsers();
                for (int i = 0; i<listUser.size(); i++){
                    fr.usmb.challengeup.entities.User userFromList = listUser.get(i);
                    userProgresses = progressService.getProgressesByUserId(userFromList.getId());
                    if (userFromList != null && user != null) {
                        // // à décommenter si on a discordID dans la table User 
                        // user = jda.retrieveUserById(userFromList.discordID).complete();
                        if (needNotification(userProgresses, 18)){
                            user.openPrivateChannel().queue(privateChannel -> {
                                privateChannel.sendMessage("Bonjour " + user.getAsTag() + ", ceci est un message automatique qui vous rappel que vous avez toujours des challenges en cours. Souhaitez vous en valider quelques un ?").queue();
                                tupleSpace.put(Long.valueOf(user.getId()), 1);
                            System.out.println("Message envoyé à " + user.getAsTag() + " et son nom dans la base est : " + userFromList.getUsername());
                            });
                        }

                    } else {
                        System.out.println("Utilisateur non trouvé.");
                    }
                }
            }
        }, 0, minutes * 60 * 1000);
    }

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(bottoken);
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
            .addEventListeners(new DiscordBot())
            .build();
    }

}


