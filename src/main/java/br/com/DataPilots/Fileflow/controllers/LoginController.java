package br.com.DataPilots.Fileflow.controllers;


import br.com.DataPilots.Fileflow.dtos.LoginRequestDTO;
import br.com.DataPilots.Fileflow.dtos.TokenResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        var token = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        this.authenticationManager.authenticate(token);

        String jwtToken = "1234";
        return ResponseEntity.ok(new TokenResponseDTO(jwtToken));
    }
}
