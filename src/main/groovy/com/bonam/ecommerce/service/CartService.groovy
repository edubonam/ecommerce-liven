package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.Cart
import com.bonam.ecommerce.dto.CartDTO
import com.bonam.ecommerce.dto.ProductToCardRequestDTO

interface CartService {

    CartDTO saveCart(Cart cart)

    CartDTO addProductsToCart(Long cartId, ProductToCardRequestDTO request)

    CartDTO getCartById(Long cartId, Long userId)

}