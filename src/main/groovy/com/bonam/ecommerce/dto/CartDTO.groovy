package com.bonam.ecommerce.dto

import com.bonam.ecommerce.domain.Cart

class CartDTO {
    Long id
    UserCartDTO user
    List<CartProductDTO> products
    BigDecimal totalPrice

    CartDTO fromDomain(Cart cart) {

        def total = cart.cartProducts.sum { it.product.price * it.quantity }

        return new CartDTO(
                id: cart.id,
                user: new UserCartDTO(id: cart.user.id, name: cart.user.name),
                products: cart.cartProducts.collect {
                    new CartProductDTO(
                            productId: it.product.id,
                            name: it.product.name,
                            unitPrice: it.product.price,
                            quantity: it.quantity,
                            totalPrice: it.product.price * it.quantity
                    )
                },
                totalPrice: total
        )
    }
}