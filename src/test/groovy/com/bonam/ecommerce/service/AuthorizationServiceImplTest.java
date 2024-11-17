package com.bonam.ecommerce.service;

import com.bonam.ecommerce.domain.User;
import com.bonam.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizationServiceImplTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final AuthorizationServiceImpl authorizationService = new AuthorizationServiceImpl();

    AuthorizationServiceImplTest() {
        authorizationService.setUserRepository(userRepository);
    }

    @Test
    void loadUserByUsername() {
        String username = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(username);
        mockUser.setPassword("password");

        when(userRepository.findByEmail(username)).thenReturn(mockUser);

        UserDetails userDetails = authorizationService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        verify(userRepository, times(1)).findByEmail(username);
    }

}
