package com.bonam.ecommerce.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @ManyToOne
    @JoinColumn(name = "cart_id")
    Cart cart

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product

    @Column(nullable = false)
    Integer quantity
}
