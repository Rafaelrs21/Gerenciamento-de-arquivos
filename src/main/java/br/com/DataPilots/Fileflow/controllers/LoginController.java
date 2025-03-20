package br.com.DataPilots.Fileflow.controllers;


import br.com.DataPilots.Fileflow.dtos.LoginRequestDTO;
import br.com.DataPilots.Fileflow.dtos.TokenResponseDTO;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    public ResponseEntity<TokenResponseDTO> login(LoginRequestDTO request) {
        var token = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        Authentication authentication = this.authenticationManager.authenticate(token);
        String jwtToken = this.tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenResponseDTO(jwtToken));
    }
}