package com.bonam.ecommerce.repository

import com.bonam.ecommerce.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository extends JpaRepository<Product, String> {
}