package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.exceptions.InvalidTokenException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import br.com.DataPilots.Fileflow.entities.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {
    @Value("${spring.application.name}")
    private String issuer;
    @Value("${spring.application.security.token.secret}")
    private String secret;
    private final TimeConfig timeConfig;

    public String generateToken(User user) {
        var algorithm = Algorithm.HMAC256(secret);

        return JWT
            .create()
            .withIssuer(issuer)
            .withSubject(user.getUsername())
            .withExpiresAt(this.getExpiresAT())
            .sign(algorithm);
    }

    private Instant getExpiresAT() {
        return LocalDateTime.now().plusHours(2).toInstant(timeConfig.zoneOffset());
    }

    public DecodedJWT decodeToken(String jwtToken) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(jwtToken);
        } catch (JWTVerificationException exception) {
            throw new InvalidTokenException();
        }
    }
}