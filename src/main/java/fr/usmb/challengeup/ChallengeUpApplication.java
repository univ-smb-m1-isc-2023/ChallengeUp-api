package fr.usmb.challengeup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ChallengeUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeUpApplication.class, args);
	}

	@GetMapping("/hello")
	public String sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return "Hello " + name + " !";
	}

}
