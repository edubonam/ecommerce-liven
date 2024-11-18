package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.User
import com.bonam.ecommerce.repository.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.security.core.userdetails.UserDetails

import static org.junit.jupiter.api.Assertions.*
import static org.mockito.Mockito.*

class AuthorizationServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository)
    private final AuthorizationServiceImpl authorizationService = new AuthorizationServiceImpl(userRepository: userRepository)

    @Test
    void loadUserByUsername() {
        def username = "test@example.com"
        def mockUser = new User(email: username, password: "password")

        when(userRepository.findByEmail(username)).thenReturn(mockUser)

        UserDetails userDetails = authorizationService.loadUserByUsername(username)

        assertNotNull(userDetails)
        assertEquals(username, userDetails.username)
        assertEquals("password", userDetails.password)
        verify(userRepository, times(1)).findByEmail(username)
    }
}
