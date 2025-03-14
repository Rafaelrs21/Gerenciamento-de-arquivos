package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.dtos.CreateUserRequestDTO;
import br.com.DataPilots.Fileflow.dtos.DefaultResponseDTO;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.exceptions.InvalidUserException;
import br.com.DataPilots.Fileflow.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {
    @Autowired
    private UserService userService;

    public ResponseEntity<DefaultResponseDTO> createUser(CreateUserRequestDTO request) {
        try {
            userService.create(request.username(), request.password());
            return ResponseEntity.status(201).body(new DefaultResponseDTO("Usuário criado."));
        } catch (Exception exception) {
            return ResponseEntity.status(400).body(new DefaultResponseDTO(exception.getMessage()));
        }
    }

    public ResponseEntity<DefaultResponseDTO> deleteUser(User user) {
        userService.delete(user);
        return ResponseEntity.ok(new DefaultResponseDTO("Usuário deletado."));
    }
}