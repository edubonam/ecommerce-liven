package com.bonam.ecommerce.config.security

import com.bonam.ecommerce.repository.UserRepository
import com.bonam.ecommerce.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SecurityFilter extends OncePerRequestFilter{

    @Autowired
    TokenService tokenService

    @Autowired
    UserRepository userRepository

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        def token = recoverToken(request)
        if (token != null) {
            UserDetails user = userRepository.findByEmail(tokenService.validateToken(token))

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null, user.authorities)
            )
        }

        filterChain.doFilter(request, response)
    }

    private static String recoverToken(HttpServletRequest request){
        def authHeader = request.getHeader("Authorization");
        if (authHeader == null) null
        else authHeader.replace("Bearer ", "")
    }
}
