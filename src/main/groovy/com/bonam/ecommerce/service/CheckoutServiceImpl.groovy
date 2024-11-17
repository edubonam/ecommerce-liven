package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.Order
import com.bonam.ecommerce.dto.OrderDTO
import com.bonam.ecommerce.exception.CustomException
import com.bonam.ecommerce.repository.CartRepository
import com.bonam.ecommerce.repository.OrderRepository
import com.bonam.ecommerce.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CheckoutServiceImpl implements CheckoutService{

    @Autowired
    CartRepository cartRepository

    @Autowired
    ProductRepository productRepository

    @Autowired
    OrderRepository orderRepository

    @Override
    @Transactional
    OrderDTO checkout(Long cartId, Long userId) {

        def cart = cartRepository.findById(cartId)
                .orElseThrow { new CustomException("Invalid Cart!") }

        if (cart.user.id != userId) {
            throw new IllegalArgumentException("This is not your cart!")
        }

        BigDecimal totalPrice = 0
        cart.cartProducts.each { cartProduct ->
            def product = cartProduct.product

            if (cartProduct.quantity > product.quantity) {
                throw new IllegalArgumentException("Quantity of product '${product.name}' exceeds available stock (${product.quantity}).")
            }

            product.quantity -= cartProduct.quantity
            productRepository.save(product)

            totalPrice += product.price * cartProduct.quantity
        }

        def order = new Order(
                cart: cart,
                totalPrice: totalPrice
        )
        return new OrderDTO().fromDomain(orderRepository.save(order))
    }
}
