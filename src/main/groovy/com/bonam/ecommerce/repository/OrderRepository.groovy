package com.bonam.ecommerce.repository

import com.bonam.ecommerce.domain.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository extends JpaRepository<Order, Long>{
}