package fr.usmb.challengeup.bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.entities.User;

import javax.security.auth.login.LoginException;

public class DiscordBot extends ListenerAdapter {

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

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return; // Ignore les messages provenant des bots

        MessageChannel channel = event.getChannel();
        String msg = event.getMessage().getContentRaw();
        User user = event.getAuthor();
        channel.sendMessage("Bonjour, je suis ChallengeUpBot, J'ai remarqué que vous n'étiez pas présent hier. Avez vous fait des challenges ?").queue();
        String msg2 = removeFirstCharacter(msg);
        if (msg.equalsIgnoreCase("Oui")) {
            channel.sendMessage("Très bien, je note çà").queue();
        }
        else if (msg.equalsIgnoreCase("Non")){
            channel.sendMessage("Aller au boulot !!!").queue();
        }
        else if (msg.equalsIgnoreCase("T'es moche !!!")){
            channel.sendMessage(":middle_finger:").queue();
        }
        else {
            user.openPrivateChannel()
                    .flatMap(canal -> canal.sendMessage(msg2))
                    .queue();
        }
    }
}