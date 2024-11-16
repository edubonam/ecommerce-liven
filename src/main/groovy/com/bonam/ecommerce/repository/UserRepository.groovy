package com.bonam.ecommerce.repository

import com.bonam.ecommerce.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends JpaRepository<User, Long>{

    UserDetails findByEmail(String email)
}