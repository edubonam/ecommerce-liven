package com.bonam.ecommerce.service

import com.bonam.ecommerce.dto.OrderDTO

interface CheckoutService {

    OrderDTO checkout(Long cartId, Long userId)

}