package com.bonam.ecommerce.controller

import com.bonam.ecommerce.dto.AuthenticationDTO
import com.bonam.ecommerce.dto.TokenDTO
import com.bonam.ecommerce.dto.UserDTO

import com.bonam.ecommerce.service.TokenService
import com.bonam.ecommerce.service.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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
    UserService userService

    @Autowired
    TokenService tokenService

    @PostMapping("/login")
    ResponseEntity login(@RequestBody @Valid AuthenticationDTO dto) {
        def auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.password()))
        def token = tokenService.generatedToken(auth.principal);

        ResponseEntity.ok(new TokenDTO(token))
    }

    @PostMapping("/create-user")
    ResponseEntity createUser(@RequestBody @Valid UserDTO dto) {
        userService.createUser(dto)

        return ResponseEntity.ok().build()
    }

}
