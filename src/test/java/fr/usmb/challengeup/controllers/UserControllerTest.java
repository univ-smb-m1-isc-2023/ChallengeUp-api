package fr.usmb.challengeup.controllers;

import fr.usmb.challengeup.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

   /* @Test
    public void createUser() throws Exception {
        String username = "Toto";
        String email = "toto@mail.com";
        String password = "password";

        mockMvc.perform(post("/user/create")
                .param("username", username)
                .param("email", email)
                .param("password", password))
                .andExpect(status().isCreated());

        verify(userService, times(1))
                .createUser(username, email, password);
    }*/
}