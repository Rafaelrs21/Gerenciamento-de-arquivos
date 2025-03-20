package br.com.DataPilots.Fileflow.api;

import br.com.DataPilots.Fileflow.controllers.LoginController;
import br.com.DataPilots.Fileflow.dtos.LoginRequestDTO;
import br.com.DataPilots.Fileflow.dtos.TokenResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginApi {
    @Autowired
    private LoginController loginController;

    @PostMapping
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return loginController.login(request);
    }
}
