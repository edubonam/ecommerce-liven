package com.bonam.ecommerce.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.bonam.ecommerce.domain.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class TokenService {

    @Value('${api.security.token.secret}')
    String secret

    String generatedToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret)
            String token = JWT.create()
                .withIssuer("auth-api")
                .withSubject(user.email)
                .withExpiresAt(genExpirationDate())
                .sign(algorithm)

            token
        }
        catch (JWTCreationException exception) {
            throw new RuntimeException("Error token generating:", exception)
        }
    }

    String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret)

            JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject()
        }
        catch (JWTVerificationException ignored) {
            ""
        }
    }

    private static Instant genExpirationDate(){
        LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of("-03:00"))
    }
}
