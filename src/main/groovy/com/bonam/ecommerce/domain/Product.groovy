package com.bonam.ecommerce.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.validation.constraints.DecimalMin

@Entity
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    String sku
    String name
    @DecimalMin(value = "0.0", inclusive = false, message = "Invalid price!")
    BigDecimal price
    @DecimalMin(value = "0.0", inclusive = true, message = "Invalid quantity!")
    Long quantity
    @OneToMany(mappedBy = "product")
    List<CartProduct> cartProducts = new ArrayList<>()

}