package com.bonam.ecommerce.domain

import com.bonam.ecommerce.enums.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name="users")
class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    String email
    String name
    String password
    UserRole role

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        List.of(new SimpleGrantedAuthority(this.role.toString()))
    }

    @Override
    String getUsername() {
        email
    }

    @Override
    boolean isAccountNonExpired() {
        super.isAccountNonExpired()
    }

    @Override
    boolean isAccountNonLocked() {
        super.isAccountNonLocked()
    }

    @Override
    boolean isCredentialsNonExpired() {
        super.isCredentialsNonExpired()
    }

    @Override
    boolean isEnabled() {
        super.isEnabled()
    }
}
