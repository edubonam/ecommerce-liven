package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.User
import org.junit.jupiter.api.Test

class TokenServiceTest {

    private final TokenService tokenService = new TokenService(
            secret: "secret-test"
    )

    @Test
    void generatedToken() {
        def user = new User(
                id: 1,
                name: "username",
                email: "user@email.com",
                password: "userpass"
        )

        def token = tokenService.generatedToken(user)

        assert token != null
    }

    @Test
    void validateToken() {
        def user = new User(
                id: 1,
                name: "username",
                email: "user@email.com",
                password: "userpass"
        )

        def token = tokenService.generatedToken(user)

        assert token != null

        def result = tokenService.validateToken(token)

        assert result != null : "Result should not be null"
        assert result == "user@email.com" : "Token should match"
    }
}
