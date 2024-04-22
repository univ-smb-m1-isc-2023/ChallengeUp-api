package fr.usmb.challengeup;

import fr.usmb.challengeup.bot.DiscordBot;
import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;

@SpringBootApplication
@RestController
public class ChallengeUpApplication {
	@Value("${DISCORD_TOKEN:defaultValue}")
	private String discordToken;
	@Autowired
	private DiscordBot discordBot;

	public static void main(String[] args) {
		SpringApplication.run(ChallengeUpApplication.class, args);
	}

	@PostConstruct
	public void startBot() throws LoginException {
		if (!discordToken.equals("defaultValue")) {
			JDABuilder.createDefault(discordToken)
					.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.DIRECT_MESSAGES)
					.addEventListeners(discordBot)
					.build();
		}
	}

	@GetMapping("/hello")
	public String sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return "Hello " + name + " !";
	}

}
