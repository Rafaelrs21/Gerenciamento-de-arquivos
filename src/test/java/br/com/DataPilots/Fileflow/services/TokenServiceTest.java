package br.com.DataPilots.Fileflow.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.exceptions.InvalidTokenException;
import java.time.Instant;
import java.time.ZoneOffset;

import br.com.DataPilots.Fileflow.infra.TimeConfig;
import br.com.DataPilots.Fileflow.tests.Factory;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TimeConfig timeConfig;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(tokenService, "issuer", "FileFlow");
        ReflectionTestUtils.setField(tokenService, "secret", "user1");
        lenient().when(timeConfig.zoneOffset()).thenReturn(ZoneOffset.of("-3"));
    }

    @Test
    public void testGenerateTokenAndDecode() {
        User user = Factory.createUser("user1", "password");

        String token = tokenService.generateToken(user);

        assertNotNull(token, "O token não deve ser nulo");
        assertTrue(!token.isEmpty(), "O token não deve ser vazio");

        DecodedJWT decodedJWT = tokenService.decodeToken(token);
        assertEquals("FileFlow", decodedJWT.getIssuer(), "O issuer deve ser 'FileFlow'");
        assertEquals("user1", decodedJWT.getSubject(), "O subject deve ser o username 'user1'");

        Instant expiresAt = decodedJWT.getExpiresAt().toInstant();
        Instant now = Instant.now();
        assertTrue(expiresAt.isAfter(now.plusSeconds(7100)) && expiresAt.isBefore(now.plusSeconds(7300)),
            "O token deve expirar aproximadamente em 2 horas");
    }

    @Test
    public void testDecodeInvalidTokenShouldThrowInvalidTokenException() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";

        InvalidTokenException exception = assertThrows(
            InvalidTokenException.class,
            () -> tokenService.decodeToken(invalidToken)
        );
        assertEquals("Token inválido ou expirado.", exception.getMessage());
    }
}