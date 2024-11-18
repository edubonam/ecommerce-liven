package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.Cart
import com.bonam.ecommerce.domain.CartProduct
import com.bonam.ecommerce.dto.CartDTO
import com.bonam.ecommerce.dto.ProductToCardRequestDTO
import com.bonam.ecommerce.exception.CustomException
import com.bonam.ecommerce.repository.CartRepository
import com.bonam.ecommerce.repository.ProductRepository
import com.bonam.ecommerce.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CartServiceImpl implements CartService{

    @Autowired
    CartRepository cartRepository

    @Autowired
    ProductRepository productRepository

    @Autowired
    UserRepository userRepository

    @Override
    CartDTO saveCart(Cart cart) {
        return new CartDTO().fromDomain(cartRepository.save(cart))
    }

    @Override
    CartDTO addProductsToCart(Long cartId, ProductToCardRequestDTO request) {

        def user = userRepository.findById(request.userId)
                .orElseThrow { new CustomException("Invalid User!") }

        def cart = cartRepository.findById(cartId)
                .orElseThrow { new CustomException("Invalid Cart!") }

        if (cart.user.id != user.id) {
            throw new CustomException("This is not your cart!")
        }

        request.products.each { dto ->
            def product = productRepository.findById(dto.productId)
                    .orElseThrow { new CustomException("Invalid product!") }

            def existingCartProduct = cart.cartProducts.find { it.product.id == dto.productId }

            if (existingCartProduct) {
                existingCartProduct.quantity += dto.quantity
            } else {
                def cartProduct = new CartProduct(cart: cart, product: product, quantity: dto.quantity)
                cart.cartProducts.add(cartProduct)
            }
        }

        return new CartDTO().fromDomain(cartRepository.save(cart))
    }

    @Override
    CartDTO getCartById(Long cartId, Long userId) {

        def cart = cartRepository.findById(cartId)
                .orElseThrow { new CustomException("Invalid Cart!") }

        if (cart.user.id != userId) {
            throw new CustomException("This is not your cart!")
        }

        return new CartDTO().fromDomain(cart)
    }
}
