package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.PasswordRecovery;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.exceptions.EmailIntegrationException;
import br.com.DataPilots.Fileflow.exceptions.PasswordRecoveryInvalidOrExpiredException;
import br.com.DataPilots.Fileflow.exceptions.TokenGenerateException;
import br.com.DataPilots.Fileflow.infra.TimeConfig;
import br.com.DataPilots.Fileflow.integrationInterfaces.EmailIntegrationInterface;
import br.com.DataPilots.Fileflow.repositories.PasswordRecoveryRepository;
import br.com.DataPilots.Fileflow.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class PasswordRecoveryService {
    private final UserRepository userRepository;
    private final PasswordRecoveryRepository passwordRecoveryRepository;
    private final TokenService tokenService;
    private final EmailIntegrationInterface emailIntegration;
    private final PasswordEncoder passwordEncoder;

    public PasswordRecoveryService(UserRepository userRepository, PasswordRecoveryRepository passwordRecoveryRepository, TokenService tokenService, EmailIntegrationInterface emailIntegration, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordRecoveryRepository = passwordRecoveryRepository;
        this.tokenService = tokenService;
        this.emailIntegration = emailIntegration;
        this.passwordEncoder = passwordEncoder;
    }

    public void recoverPassword(String email) throws TokenGenerateException, EmailIntegrationException {
        var userQuery = userRepository.findByUsername(email);

        if (userQuery.isEmpty()) return;

        User user = userQuery.get();

        PasswordRecovery passwordRecovery = createRecoveryFor(user);

        sendPasswordRecoveryToEmail(user.getUsername(), passwordRecovery);

        passwordRecoveryRepository.save(passwordRecovery);
    }

    private PasswordRecovery createRecoveryFor(User user) throws TokenGenerateException {
        String token = tokenService.generateUniqueToken();

        return new PasswordRecovery(token, user, getExpiresAT());
    }

    private LocalDateTime getExpiresAT() {
        return LocalDateTime.now().plusHours(2);
    }

    private void sendPasswordRecoveryToEmail(String email, PasswordRecovery passwordRecovery) throws EmailIntegrationException {
        emailIntegration.sendRecovery(email, passwordRecovery.getToken());
    }

    public void updatePassword(String recoveryToken, String newPassword) throws PasswordRecoveryInvalidOrExpiredException {
        var passwordRecoveryQuery = passwordRecoveryRepository.findByToken(recoveryToken);

        if (passwordRecoveryQuery.isEmpty()) throw new PasswordRecoveryInvalidOrExpiredException();

        PasswordRecovery passwordRecovery = passwordRecoveryQuery.get();

        if (passwordRecovery.isExpired()) throw new PasswordRecoveryInvalidOrExpiredException();

        User user = passwordRecovery.getUser();

        user.setPassword(encodePassword(newPassword));
        passwordRecovery.setUsed(true);

        userRepository.save(user);
        passwordRecoveryRepository.save(passwordRecovery);
    }

    private String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }
}
