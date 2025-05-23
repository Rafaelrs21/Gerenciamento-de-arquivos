package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.PasswordRecovery;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.exceptions.EmailIntegrationException;
import br.com.DataPilots.Fileflow.exceptions.PasswordRecoveryInvalidOrExpiredException;
import br.com.DataPilots.Fileflow.exceptions.TokenGenerateException;
import br.com.DataPilots.Fileflow.integrationInterfaces.EmailIntegrationInterface;
import br.com.DataPilots.Fileflow.repositories.PasswordRecoveryRepository;
import br.com.DataPilots.Fileflow.repositories.UserRepository;
import br.com.DataPilots.Fileflow.tests.Factory;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class PasswordRecoveryServiceTest {
    private UserRepository userRepository;
    private PasswordRecoveryRepository passwordRecoveryRepository;
    private TokenService tokenService;
    private EmailIntegrationInterface emailIntegration;
    private PasswordEncoder passwordEncoder;

    private PasswordRecoveryService service;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordRecoveryRepository = Mockito.mock(PasswordRecoveryRepository.class);
        tokenService = Mockito.mock(TokenService.class);
        emailIntegration = Mockito.mock(EmailIntegrationInterface.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);

        service = new PasswordRecoveryService(userRepository, passwordRecoveryRepository, tokenService, emailIntegration, passwordEncoder);
    }


    @Test
    public void recoverPassword_whenSuccess() throws TokenGenerateException, EmailIntegrationException {
        String email = Faker.instance().internet().emailAddress();
        User user = Mockito.mock(User.class);
        String token = "123456";

        Mockito.when(user.getUsername()).thenReturn(email);
        Mockito.when(userRepository.findByUsername(email)).thenReturn(Optional.of(user));
        Mockito.when(tokenService.generateUniqueToken()).thenReturn(token);

        service.recoverPassword(email);

        Mockito.verify(emailIntegration, Mockito.times(1)).sendRecovery(email, token);
        Mockito.verify(passwordRecoveryRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void recoverPassword_whenFail() throws TokenGenerateException, EmailIntegrationException {
        String email = Faker.instance().internet().emailAddress();
        User user = Mockito.mock(User.class);
        String token = "123456";

        Mockito.when(user.getUsername()).thenReturn(email);
        Mockito.when(userRepository.findByUsername(email)).thenReturn(Optional.empty());
        Mockito.when(tokenService.generateUniqueToken()).thenReturn(token);

        service.recoverPassword(email);

        Mockito.verify(emailIntegration, Mockito.never()).sendRecovery(email, token);
        Mockito.verify(passwordRecoveryRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void updatePassword_whenSuccess() throws PasswordRecoveryInvalidOrExpiredException {
        String token = "123456";
        String newPassword = Faker.instance().internet().password();
        PasswordRecovery recovery = Mockito.mock(PasswordRecovery.class);
        User user = Factory.createFakeUser();

        Mockito.when(passwordRecoveryRepository.findByToken(token)).thenReturn(Optional.of(recovery));
        Mockito.when(recovery.isExpired()).thenReturn(false);
        Mockito.when(recovery.getUser()).thenReturn(user);

        service.updatePassword(token, newPassword);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(passwordRecoveryRepository, Mockito.times(1)).save(Mockito.any(PasswordRecovery.class));
    }

    @Test
    public void updatePassword_whenRecoveryNotFound() throws PasswordRecoveryInvalidOrExpiredException {
        String token = "123456";
        String newPassword = Faker.instance().internet().password();
        PasswordRecovery recovery = Mockito.mock(PasswordRecovery.class);
        User user = Factory.createFakeUser();

        Mockito.when(passwordRecoveryRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(PasswordRecoveryInvalidOrExpiredException.class, () -> service.updatePassword(token, newPassword));

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
        Mockito.verify(passwordRecoveryRepository, Mockito.never()).save(Mockito.any(PasswordRecovery.class));
    }

    @Test
    public void updatePassword_whenExpired() throws PasswordRecoveryInvalidOrExpiredException {
        String token = "123456";
        String newPassword = Faker.instance().internet().password();
        PasswordRecovery recovery = Mockito.mock(PasswordRecovery.class);
        User user = Factory.createFakeUser();

        Mockito.when(passwordRecoveryRepository.findByToken(token)).thenReturn(Optional.of(recovery));
        Mockito.when(recovery.isExpired()).thenReturn(true);

        assertThrows(PasswordRecoveryInvalidOrExpiredException.class, () -> service.updatePassword(token, newPassword));

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
        Mockito.verify(passwordRecoveryRepository, Mockito.never()).save(Mockito.any(PasswordRecovery.class));
    }
}
