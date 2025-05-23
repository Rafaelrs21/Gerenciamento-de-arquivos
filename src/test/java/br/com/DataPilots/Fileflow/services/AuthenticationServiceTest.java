package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.repositories.UserRepository;
import br.com.DataPilots.Fileflow.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService service;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = Factory.createUser("usuario", "password");
    }

    @Test
    public void loadUserByUsernameShouldReturnUserWhenFound() {
        when(userRepository.findByUsername("usuario")).thenReturn(Optional.of(user));

        UserDetails userDetails = service.loadUserByUsername("usuario");
        assertNotNull(userDetails);
        assertEquals("usuario", userDetails.getUsername());
    }

    @Test
    public void loadUserByUsernameShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("naoexiste")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> service.loadUserByUsername("naoexiste")
        );
        assertEquals("Usuário não encontrado.", exception.getMessage());
    }

    @Test
    public void passwordEncoderShouldReturnBCryptPasswordEncoder() {
        PasswordEncoder encoder = service.passwordEncoder();
        assertInstanceOf(BCryptPasswordEncoder.class, encoder, "O encoder deve ser uma instância de BCryptPasswordEncoder");
    }

    @Test
    public void passwordEncoderShouldEncodeAndMatchPassword() {
        PasswordEncoder encoder = service.passwordEncoder();
        String rawPassword = "minhaSenhaSecreta";
        String encodedPassword = encoder.encode(rawPassword);

        assertNotEquals(rawPassword, encodedPassword, "A senha codificada não deve ser igual à senha original");

        assertTrue(encoder.matches(rawPassword, encodedPassword),
            "O encoder deve confirmar que a senha original corresponde à codificada");
    }
}