package com.bonam.ecommerce.config.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

        httpSecurity
                .csrf {csrf -> csrf.disable()}
                .sessionManagement {session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
                .authorizeHttpRequests {authorize -> authorize
                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/create-user").permitAll()
                    .anyRequest().authenticated()
                }
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build()
    }

    @Bean
    static AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationManager) throws Exception{
        authenticationManager.getAuthenticationManager()
    }

    @Bean
    static PasswordEncoder passwordEncoder(){
        new BCryptPasswordEncoder()
    }
}
