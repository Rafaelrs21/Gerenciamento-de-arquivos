package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.dtos.DefaultErrorResponse;
import br.com.DataPilots.Fileflow.dtos.DefaultResponseDTO;
import br.com.DataPilots.Fileflow.dtos.RecoverPasswordRequestDTO;
import br.com.DataPilots.Fileflow.dtos.UpdatePasswordRequestDTO;
import br.com.DataPilots.Fileflow.exceptions.EmailIntegrationException;
import br.com.DataPilots.Fileflow.exceptions.PasswordRecoveryInvalidOrExpiredException;
import br.com.DataPilots.Fileflow.exceptions.TokenGenerateException;
import br.com.DataPilots.Fileflow.services.PasswordRecoveryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password_recovery")
public class PasswordRecoveryController {
    private final PasswordRecoveryService passwordRecoveryService;

    public PasswordRecoveryController(PasswordRecoveryService passwordRecoveryService) {
        this.passwordRecoveryService = passwordRecoveryService;
    }

    @PostMapping
    public ResponseEntity<DefaultResponseDTO> sendRecoverPassword(@Valid @RequestBody RecoverPasswordRequestDTO request) throws TokenGenerateException, EmailIntegrationException {
        passwordRecoveryService.recoverPassword(request.email());
        return ResponseEntity.ok().body(new DefaultResponseDTO("Recuperação de senha enviada para seu email."));
    }

    @PutMapping
    public ResponseEntity updatePassword(@Valid @RequestBody UpdatePasswordRequestDTO request) {
        try {
            passwordRecoveryService.updatePassword(request.recoveryToken(), request.newPassword());
            return ResponseEntity.ok().body(new DefaultResponseDTO("Senha atualizada."));
        } catch (PasswordRecoveryInvalidOrExpiredException exception) {
            return ResponseEntity.status(400).body(new DefaultErrorResponse(exception.getMessage()));
        }
    }
}
