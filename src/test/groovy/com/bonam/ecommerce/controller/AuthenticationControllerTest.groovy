package com.bonam.ecommerce.controller

import com.bonam.ecommerce.config.security.SecurityFilter
import com.bonam.ecommerce.domain.User
import com.bonam.ecommerce.dto.AuthenticationDTO
import com.bonam.ecommerce.dto.UserDTO
import com.bonam.ecommerce.enums.UserRole
import com.bonam.ecommerce.service.TokenService
import com.bonam.ecommerce.service.UserService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.MockMvc

import static org.mockito.ArgumentMatchers.any
import static org.mockito.BDDMockito.given
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(AuthenticationController)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private AuthenticationManager authenticationManager

    @MockBean
    private TokenService tokenService

    @MockBean
    private UserService userService

    @MockBean
    private SecurityFilter securityFilter

    @Test
    void login() {
        def dto = new AuthenticationDTO("test@example.com", "password")
        def principal = new User()
        def authToken = new UsernamePasswordAuthenticationToken(principal, dto.password())

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken))).willReturn(authToken)
        given(tokenService.generatedToken(any())).willReturn("mock-token")

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{
                    "email": "test@example.com",
                    "password": "password"
                }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.token').value("mock-token"))

        Mockito.verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken))
        Mockito.verify(tokenService).generatedToken(any())
        Mockito.verifyNoMoreInteractions(authenticationManager, tokenService)
    }

    @Test
    void createUser() {
        def userDTO = new UserDTO("userName", "user@email.com", "userPass", "ADMIN")
        def user = new User(email: userDTO.email(), name: userDTO.name(), password: userDTO.password(), role: UserRole.fromString(userDTO.role()))

        given(userService.createUser(userDTO)).willReturn(user)

        mockMvc.perform(post("/auth/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{
                    "name": "userName",
                    "password": "userPass",
                    "email": "user@email.com",
                    "role": "ADMIN"
                }"""))
                .andExpect(status().isOk())

        Mockito.verify(userService).createUser(userDTO)
        Mockito.verifyNoMoreInteractions(userService)
    }
}
