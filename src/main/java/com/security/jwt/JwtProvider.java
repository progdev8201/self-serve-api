package com.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.model.entity.Admin;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Data
@Component
public final class JwtProvider {

    private final Algorithm algorithm;

    private final JWTVerifier verifier;

    private final long duration;


    public JwtProvider(@Value("${security.jwt.duration}") long durationHours,@Value("${security.jwt.secret}") String secret) {
        algorithm = Algorithm.HMAC256(secret);
        verifier = JWT.require(algorithm).build();
        duration = TimeUnit.HOURS.toMillis(durationHours);
    }

    public String generate(final Admin user) {
        final long time = System.currentTimeMillis();

        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("role", user.getRole())
                .withIssuedAt(new Date(time))
                .withExpiresAt(new Date(time + duration))
                .sign(algorithm);
    }

    public DecodedJWT verify(String token) throws JWTVerificationException {
        if (token == null)
            throw new JWTVerificationException("Token cannot be null");

        if (token.startsWith("Bearer "))
            token = token.replace("Bearer ","");

        return verifier.verify(token.trim());
    }

}
