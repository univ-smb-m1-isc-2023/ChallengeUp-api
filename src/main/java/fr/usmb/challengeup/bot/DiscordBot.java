package fr.usmb.challengeup.bot;

import fr.usmb.challengeup.entities.Challenge;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.entities.User;
/* import net.dv8tion.jda.api.entities.Message;*/

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
/* import java.util.concurrent.TimeUnit; */

public class DiscordBot extends ListenerAdapter {

    private boolean waitingForConfirmation = false;
    private Map<Long, Integer> tupleSpace = new HashMap<>();
    private ArrayList<String> oui = new ArrayList<>();
    private ArrayList<String> non = new ArrayList<>();

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
        non.add("false");

    }

    public void sendPrivateMessage(JDA jda, String userId, String content) {
        jda.retrieveUserById(userId).queue((user) -> {
            user.openPrivateChannel().queue((channel) -> {
                channel.sendMessage(content).queue();
            });
        });
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        initializeSomeVariables();
        if (event.getAuthor().isBot()) return; // Ignore les messages provenant des bots

        String message = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        User author = event.getAuthor();



        ArrayList<String> challenges = new ArrayList<>();
        challenges.add("Faire les courses");
        challenges.add("Faire le menage");
        challenges.add("Faire a manger");

        if (!tupleSpace.containsKey(Long.valueOf(author.getId()))){
            if (message.equalsIgnoreCase("!start")) {
                waitingForConfirmation = true;
                author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Voici vos challenges en attente et votre ID : " + author.getId())).queue();
                for (int i = 0; i < challenges.size(); i++){
                    int finalI = i;
                    author.openPrivateChannel().flatMap(teste -> teste.sendMessage((finalI+1) + " : " + challenges.get(finalI) )).queue();
                }
                author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Avez vous des challenges à valider ?")).queue();
                tupleSpace.put(Long.valueOf(author.getId()), 1);
            }
        }
        else {
            if(message.equalsIgnoreCase("annuler") || message.equalsIgnoreCase("cancel")){
                System.out.println("cancel detecté");
                tupleSpace.remove(Long.valueOf(author.getId()));
            }
            else {
                tupleSpace.put(Long.valueOf(author.getId()), 2);
                /* System.out.println("J'ai atteint la 2e etape et le message est : " + message.toLowerCase()); */
                if (non.contains(message.toLowerCase())) {
                    System.out.println("non detecté");
                    tupleSpace.remove(Long.valueOf(author.getId()));
                    author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Bah qu'est ce que tu attends ? Tu as encore " + challenges.size() + " challenges à faire !!!")).queue();
                }
                else if (oui.contains(message.toLowerCase())){
                    System.out.println("oui detecté");
                    tupleSpace.put(Long.valueOf(author.getId()), 3);
                    author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Très bien, lequel voulez vous valider ?")).queue();
                    for (int i = 0; i < challenges.size(); i++){
                        int finalI = i;
                        author.openPrivateChannel().flatMap(teste -> teste.sendMessage((finalI+1) + " : " + challenges.get(finalI) )).queue();
                    }
                }
                else {
                    author.openPrivateChannel().flatMap(teste -> teste.sendMessage("Je n'ai pas compris. Avez vous des challenges à valider ?")).queue();
                }
            }
        }
        System.out.println(tupleSpace);
    }

}