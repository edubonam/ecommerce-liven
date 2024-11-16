package com.bonam.ecommerce.controller

import com.bonam.ecommerce.domain.User
import com.bonam.ecommerce.dto.AuthenticationDTO
import com.bonam.ecommerce.dto.TokenDTO
import com.bonam.ecommerce.dto.UserDTO
import com.bonam.ecommerce.enums.UserRole
import com.bonam.ecommerce.repository.UserRepository
import com.bonam.ecommerce.service.TokenService
import org.antlr.v4.runtime.Token
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    UserRepository userRepository

    @Autowired
    TokenService tokenService

    @PostMapping("/login")
    ResponseEntity login(@RequestBody AuthenticationDTO dto) {
        def auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.password()))
        def token = tokenService.generatedToken(auth.principal);

        ResponseEntity.ok(new TokenDTO(token))
    }

    @PostMapping("/create-user")
    ResponseEntity createUser(@RequestBody UserDTO dto) {
        //service.createUser bonam

        if (userRepository.findByEmail(dto.email()) != null) {
           ResponseEntity.badRequest().build()
        }

        User newUser = new User()
        newUser.setEmail(dto.email())
        newUser.setName(dto.name())
        newUser.setPassword(new BCryptPasswordEncoder().encode(dto.password()))
        newUser.setRole(UserRole.fromString(dto.role()))

        userRepository.save(newUser)

        ResponseEntity.ok().build()
    }

}
