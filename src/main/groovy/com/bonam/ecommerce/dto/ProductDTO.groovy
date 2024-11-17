package com.bonam.ecommerce.dto

import com.bonam.ecommerce.domain.CartProduct
import com.bonam.ecommerce.domain.Product

class ProductDTO {
    Long id
    String sku
    String name
    BigDecimal price
    Long quantity
    List<CartProduct> cartProducts

    ProductDTO fromDomain(Product product) {

        return new ProductDTO(
                id: product.id,
                sku: product.sku,
                name: product.name,
                price: product.price,
                quantity: product.quantity,
                cartProducts: product.cartProducts
        )
    }
}
