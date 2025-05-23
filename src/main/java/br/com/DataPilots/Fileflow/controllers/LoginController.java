package br.com.DataPilots.Fileflow.controllers;


import br.com.DataPilots.Fileflow.dtos.LoginRequestDTO;
import br.com.DataPilots.Fileflow.dtos.TokenResponseDTO;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.services.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            var token = new UsernamePasswordAuthenticationToken(request.username(), request.password());
            var authentication = this.authenticationManager.authenticate(token);

            String jwtToken = this.tokenService.generateToken((User) authentication.getPrincipal());
            return ResponseEntity.ok(new TokenResponseDTO(jwtToken));
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(403).body(new TokenResponseDTO(exception.getMessage()));
        }
    }
}