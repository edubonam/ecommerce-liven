package com.bonam.ecommerce.dto

import com.bonam.ecommerce.domain.Order
import com.bonam.ecommerce.enums.OrderStatus

class OrderDTO {
    Long id
    CartDTO cartDTO
    BigDecimal totalPrice
    Date createdAt
    OrderStatus status

    OrderDTO fromDomain(Order order) {

        return new OrderDTO(
                id: order.id,
                cartDTO: new CartDTO().fromDomain(order.cart),
                totalPrice: order.totalPrice,
                createdAt: order.createdAt,
                status: order.status
        )
    }
}
