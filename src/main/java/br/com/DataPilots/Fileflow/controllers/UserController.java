package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.dtos.CreateUserRequestDTO;
import br.com.DataPilots.Fileflow.dtos.DefaultResponseDTO;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.exceptions.InvalidUserException;
import br.com.DataPilots.Fileflow.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<DefaultResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO request) {
        try {
            this.userService.create(request.username(), request.password());

            return this.userCreatedResponse();
        } catch (InvalidUserException exception) {
            return this.badRequestResponse(exception.getMessage());
        }
    }

    public ResponseEntity<DefaultResponseDTO> badRequestResponse(String message) {
        var response = new DefaultResponseDTO(message);

        return ResponseEntity.status(400).body(response);
    }

    public ResponseEntity<DefaultResponseDTO> userCreatedResponse() {
        var response = new DefaultResponseDTO("Usuário criado.");

        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping
    public ResponseEntity<DefaultResponseDTO> deleteUser(@AuthenticationPrincipal User user) {
        userService.delete(user);

        return this.usersDeletedResponse();
    }

    private ResponseEntity<DefaultResponseDTO> usersDeletedResponse() {
        return ResponseEntity.ok(new DefaultResponseDTO("Usuário deletado."));
    }
}