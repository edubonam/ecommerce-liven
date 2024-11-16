package com.bonam.ecommerce.domain

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="product")
class Product {

    @Id
    String Sku

    @NotNull
    String name

    @NotNull
    BigDecimal price

    @NotNull
    Long quantity

}