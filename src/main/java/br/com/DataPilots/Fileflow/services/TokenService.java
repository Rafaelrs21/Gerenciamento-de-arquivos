package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.exceptions.InvalidTokenException;
import br.com.DataPilots.Fileflow.exceptions.TokenGenerateException;
import br.com.DataPilots.Fileflow.infra.TimeConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import br.com.DataPilots.Fileflow.entities.User;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class TokenService {
    private final String issuer;
    private final String secret;
    private final TimeConfig timeConfig;

    public TokenService(Environment env, TimeConfig timeConfig) {
        this.issuer = env.getProperty("spring.application.name");
        this.secret = env.getProperty("spring.application.security.token.secret");
        this.timeConfig = timeConfig;
    }

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

    public String generateUniqueToken() throws TokenGenerateException {
        try {
            String uniqueData = UUID.randomUUID().toString();

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);

            byte[] hash = mac.doFinal(uniqueData.getBytes());

            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new TokenGenerateException();
        }
    }
}