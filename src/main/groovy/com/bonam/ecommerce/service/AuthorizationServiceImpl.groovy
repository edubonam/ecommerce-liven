package com.bonam.ecommerce.service

import com.bonam.ecommerce.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthorizationServiceImpl implements UserDetailsService{

    @Autowired
    UserRepository userRepository

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userRepository.findByEmail(username)
    }
}
