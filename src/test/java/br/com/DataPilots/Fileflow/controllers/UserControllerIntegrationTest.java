package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.repositories.UserRepository;
import br.com.DataPilots.Fileflow.services.TokenService;
import br.com.DataPilots.Fileflow.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @BeforeEach
    public void setUp() {
        usersRepository.deleteAll();
    }

    @Test
    public void createValidUser() throws Exception {
        String json = "{\"username\":\"test_username\", \"password\":\"test_password\"}";
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("Usuário criado."));
    }

    @Test
    public void createInvalidUserWithInvalidPasswordLength() throws Exception {
        String json = "{\"username\":\"test_username\", \"password\":\"short\"}";
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserThatAlreadyExists() throws Exception {
        User user = Factory.createUser("test_username", passwordEncoder.encode("test_password"));
        usersRepository.save(user);
        String json = "{\"username\":\"test_username\", \"password\":\"test_password\"}";
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Esse usuário já existe."));
    }

    @Test
    public void deleteValidUser() throws Exception {
        User user = Factory.createUser("test_username", passwordEncoder.encode("test_password"));
        usersRepository.save(user);
        String userJwtToken = tokenService.generateToken(user);
        mockMvc.perform(delete("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + userJwtToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Usuário deletado."));
    }
}