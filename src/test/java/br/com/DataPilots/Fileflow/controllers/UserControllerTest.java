package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.exceptions.UserAlreadyExistsException;
import br.com.DataPilots.Fileflow.repositories.UserRepository;
import br.com.DataPilots.Fileflow.services.TokenService;
import br.com.DataPilots.Fileflow.services.UserService;
import br.com.DataPilots.Fileflow.tests.Factory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService usersService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository usersRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void createValidUserShouldReturnCreatedStatus() throws Exception {
        String username = "test_username";
        String password = "test_password";

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("Usuário criado."));
    }

    @Test
    public void createInvalidUserShouldReturnBadRequestStatus() throws Exception {
        String username = "test_username";
        String password = "short";

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserThatAlreadyExistShouldReturnBadRequestStatus() throws Exception {
        String username = "test_username";
        String password = "test_password";
        doThrow(UserAlreadyExistsException.class).when(usersService).create(username, password);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Esse usuário já existe."));
    }

    @Test
    public void deleteValidUserShouldReturnOkStatus() throws Exception {
        String username = "test_username";
        String password = "test_password";
        User user = Factory.createUser(username, passwordEncoder.encode(password));
        String userJwtToken = tokenService.generateToken(user);
        mockMvc.perform(delete("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + userJwtToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Usuário deletado."));
    }
}
