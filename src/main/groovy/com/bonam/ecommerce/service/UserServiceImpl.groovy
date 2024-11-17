package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.User
import com.bonam.ecommerce.dto.UserDTO
import com.bonam.ecommerce.enums.UserRole
import com.bonam.ecommerce.exception.CustomException
import com.bonam.ecommerce.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository

    @Override
    User createUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.email()) != null) {
            new CustomException("Invalid User, email already exists!")
        }

        User newUser = new User()
        newUser.setEmail(userDTO.email())
        newUser.setName(userDTO.name())
        newUser.setPassword(new BCryptPasswordEncoder().encode(userDTO.password()))
        newUser.setRole(UserRole.fromString(userDTO.role()))

        userRepository.save(newUser)
    }
}
