package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.User
import com.bonam.ecommerce.dto.UserDTO

interface UserService {
    User createUser(UserDTO userDTO)
}