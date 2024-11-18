package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.User
import com.bonam.ecommerce.dto.UserDTO
import com.bonam.ecommerce.enums.UserRole
import com.bonam.ecommerce.exception.CustomException
import com.bonam.ecommerce.repository.UserRepository
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertThrows
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository)
    private final UserServiceImpl userService = new UserServiceImpl(
            userRepository: userRepository
    )

    @Test
    void createUser() {
        def user = new User(id: 1L, name: "username", email: "user@email.com", password: "userpass", role: UserRole.ADMIN)
        def userDTO = new UserDTO(user.name, user.email, user.password, user.role.toString())

        when(userRepository.findByEmail(userDTO.email())).thenReturn(null)
        when(userRepository.save(user)).thenReturn(user)

        userService.createUser(userDTO)

        verify(userRepository, times(1)).findByEmail(userDTO.email())
        verify(userRepository, times(1)).save(any())
    }

    @Test
    void createUserInvalidEmail() {
        def user = new User(id: 1L, name: "username", email: "user@email.com", password: "userpass", role: UserRole.ADMIN)
        def userDTO = new UserDTO(user.name, user.email, user.password, user.role.toString())

        when(userRepository.findByEmail(userDTO.email())).thenReturn(user)
        when(userRepository.save(user)).thenReturn(user)

        def exception = assertThrows(CustomException) {
            userService.createUser(userDTO)
        }

        assert exception.message == "Invalid User, email already exists!"

        verify(userRepository, times(1)).findByEmail(userDTO.email())
    }
}
