package com.bonam.ecommerce.repository

import com.bonam.ecommerce.domain.Cart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartRepository extends JpaRepository<Cart, Long>{
}