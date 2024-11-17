package com.bonam.ecommerce.domain

import com.bonam.ecommerce.enums.OrderStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType

@Entity
@Table(name = "orders")
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @OneToOne
    @JoinColumn(name = "cart_id", nullable = false)
    Cart cart

    @Column(nullable = false)
    BigDecimal totalPrice

    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt = new Date()

    @Enumerated(EnumType.STRING)
    OrderStatus status = OrderStatus.PENDING
}