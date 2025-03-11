package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.exceptions.InvalidPasswordLengthException;
import br.com.DataPilots.Fileflow.exceptions.UserAlreadyExistsException;
import br.com.DataPilots.Fileflow.repositories.UsersRepository;
import br.com.DataPilots.Fileflow.tests.Factory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {
    @InjectMocks
    private UsersService service;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsersRepository repository;

    @Test
    public void shouldCreateAValidUser() {
        when(repository.existsByUsername("test_username")).thenReturn(false);
        when(passwordEncoder.encode("test_password")).thenReturn("encoded_test_password");

        assertDoesNotThrow(() -> service.create("test_username", "test_password"));
        verify(repository, times(1)).save(any(User.class));
        verify(repository, times(1)).existsByUsername("test_username");
        verify(passwordEncoder, times(1)).encode("test_password");
    }

    @Test
    public void createShouldThrowExceptionWhenUsernameIsAlreadyInUse() {
        when(repository.existsByUsername("test_username")).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> service.create("test_username", "test_password"));

        assertEquals("Esse usuário já existe.", exception.getMessage());
        verify(repository, times(0)).save(any(User.class));
        verify(passwordEncoder, times(0)).encode("test_password");
    }

    @Test
    public void createShouldThrowExceptionWhenPasswordIsInvalid() {
        InvalidPasswordLengthException exception = assertThrows(InvalidPasswordLengthException.class, () -> service.create("test_username", "pw"));

        assertEquals("Tamanho da senha inválido.", exception.getMessage());
        verify(repository, times(0)).save(any(User.class));
        verify(passwordEncoder, times(0)).encode("test_password");
    }

    @Test
    public void shouldDeleteUser() {
        User user = Factory.createUser("test_username", "test_password");
        assertDoesNotThrow(() -> service.delete(user));
        verify(repository, times(1)).delete(user);
    }
}
