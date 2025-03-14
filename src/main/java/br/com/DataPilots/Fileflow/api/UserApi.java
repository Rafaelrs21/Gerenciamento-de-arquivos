package br.com.DataPilots.Fileflow.api;

import br.com.DataPilots.Fileflow.controllers.UserController;
import br.com.DataPilots.Fileflow.dtos.CreateUserRequestDTO;
import br.com.DataPilots.Fileflow.dtos.DefaultResponseDTO;
import br.com.DataPilots.Fileflow.entities.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserApi {
    @Autowired
    private UserController userController;

    @PostMapping
    public ResponseEntity<DefaultResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO request) {
        return userController.createUser(request);
    }

    @DeleteMapping
    public ResponseEntity<DefaultResponseDTO> deleteUser(@AuthenticationPrincipal User user) {
        return userController.deleteUser(user);
    }
}