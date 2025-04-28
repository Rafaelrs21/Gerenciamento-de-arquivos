package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.dtos.DefaultErrorResponse;
import br.com.DataPilots.Fileflow.dtos.DefaultResponseDTO;
import br.com.DataPilots.Fileflow.dtos.RecoverPasswordRequestDTO;
import br.com.DataPilots.Fileflow.dtos.UpdatePasswordRequestDTO;
import br.com.DataPilots.Fileflow.exceptions.EmailIntegrationException;
import br.com.DataPilots.Fileflow.exceptions.PasswordRecoveryInvalidOrExpiredException;
import br.com.DataPilots.Fileflow.exceptions.TokenGenerateException;
import br.com.DataPilots.Fileflow.services.PasswordRecoveryService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordRecoveryControllerTest {
    private PasswordRecoveryService passwordRecoveryService;
    private PasswordRecoveryController controller;

    @BeforeEach
    public void setUp() {
        passwordRecoveryService = Mockito.mock(PasswordRecoveryService.class);

        controller = new PasswordRecoveryController(passwordRecoveryService);
    }

    @Test
    public void sendRecoverPassword_whenSuccess() throws TokenGenerateException, EmailIntegrationException {
        String email = Faker.instance().internet().emailAddress();

        var response = controller.sendRecoverPassword(new RecoverPasswordRequestDTO(email));

        Mockito.verify(passwordRecoveryService, Mockito.times(1)).recoverPassword(email);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Recuperação de senha enviada para seu email.", response.getBody().message());
    }

    @Test
    public void updatePassword_whenSuccess() throws TokenGenerateException, EmailIntegrationException, PasswordRecoveryInvalidOrExpiredException {
        String token = "123456";
        String newPassword = Faker.instance().internet().password();

        var response = controller.updatePassword(new UpdatePasswordRequestDTO(token, newPassword));

        Mockito.verify(passwordRecoveryService, Mockito.times(1)).updatePassword(token, newPassword);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Senha atualizada.", ((DefaultResponseDTO) response.getBody()).message());
    }

    @Test
    public void updatePassword_whenError() throws TokenGenerateException, EmailIntegrationException, PasswordRecoveryInvalidOrExpiredException {
        String token = "123456";
        String newPassword = Faker.instance().internet().password();

        Mockito.doThrow(PasswordRecoveryInvalidOrExpiredException.class).when(passwordRecoveryService).updatePassword(token, newPassword);

        var response = controller.updatePassword(new UpdatePasswordRequestDTO(token, newPassword));

        Mockito.verify(passwordRecoveryService, Mockito.times(1)).updatePassword(token, newPassword);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Recuração de senha inválida ou expirada.", ((DefaultErrorResponse) response.getBody()).message());
    }
}
