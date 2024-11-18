package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.Cart
import com.bonam.ecommerce.domain.CartProduct
import com.bonam.ecommerce.domain.Order
import com.bonam.ecommerce.domain.Product
import com.bonam.ecommerce.domain.User
import com.bonam.ecommerce.enums.OrderStatus
import com.bonam.ecommerce.exception.CustomException
import com.bonam.ecommerce.repository.CartRepository
import com.bonam.ecommerce.repository.OrderRepository
import com.bonam.ecommerce.repository.ProductRepository
import org.junit.jupiter.api.Test

import java.time.Instant

import static org.junit.jupiter.api.Assertions.assertThrows
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.verifyNoInteractions
import static org.mockito.Mockito.when

class CheckoutServiceImplTest {

    private final CartRepository cartRepository = mock(CartRepository)
    private final ProductRepository productRepository = mock(ProductRepository)
    private final OrderRepository orderRepository = mock(OrderRepository)

    private final CheckoutServiceImpl checkoutService = new CheckoutServiceImpl(
            cartRepository: cartRepository,
            productRepository: productRepository,
            orderRepository: orderRepository
    )

    @Test
    void checkout() {
        def user = new User(id: 1L, name: "username")

        def product1 = new Product(id: 1, price: BigDecimal.TEN, quantity: 10L)
        def product2 = new Product(id: 2, price: BigDecimal.valueOf(20L), quantity: 20L)

        def cartProduct1 = new CartProduct(id: 1L, product: product1, quantity: 1L)
        def cartProduct2 = new CartProduct(id: 2L, product: product2, quantity: 2L)

        def cart = new Cart(id: 1L, user: user, cartProducts: [cartProduct1, cartProduct2])

        def order = new Order(
                id: 1L,
                cart: cart,
                totalPrice: BigDecimal.valueOf(50L),
                createdAt: Date.from(Instant.now()),
                status: OrderStatus.PENDING
        )

        when(cartRepository.findById(cart.id)).thenReturn(Optional.of(cart))
        when(productRepository.save(product1)).thenReturn(product1)
        when(productRepository.save(product2)).thenReturn(product2)
        when(orderRepository.save(any())).thenReturn(order)

        def result = checkoutService.checkout(cart.id, user.id)

        assert result != null : "Result should not be null"
        assert result.id == 1L : "Order ID should match"
        assert result.cartDTO.products.size() == 2: "Cart should have 2 products"
        assert result.totalPrice == BigDecimal.valueOf(50L) : "Order TotalPrice should match"

        verify(productRepository).save(product1)
        verify(productRepository).save(product2)
        assert product1.quantity == 9L : "Product1 stock should be reduced"
        assert product2.quantity == 18L : "Product2 stock should be reduced"

        verify(cartRepository, times(1)).findById(cart.id)
        verify(orderRepository, times(1)).save(any())

    }

    @Test
    void checkoutInvalidCart() {
        def user = new User(id: 1L, name: "username")

        when(cartRepository.findById(1L)).thenReturn(Optional.empty())

        def exception = assertThrows(CustomException) {
            checkoutService.checkout(1L, user.id)
        }

        assert exception.message == "Invalid Cart!"

        verify(cartRepository, times(1)).findById(1L)
        verifyNoInteractions(orderRepository)
    }

    @Test
    void checkoutUserUnauthorized() {
        def user = new User(id: 1L, name: "username")
        def userWithoutAccess = new User(id: 2L, name: "username2")

        def product1 = new Product(id: 1, price: BigDecimal.TEN, quantity: 10L)
        def product2 = new Product(id: 2, price: BigDecimal.valueOf(20L), quantity: 20L)

        def cartProduct1 = new CartProduct(id: 1L, product: product1, quantity: 1L)
        def cartProduct2 = new CartProduct(id: 2L, product: product2, quantity: 2L)

        def cart = new Cart(id: 1L, user: user, cartProducts: [cartProduct1, cartProduct2])

        when(cartRepository.findById(cart.id)).thenReturn(Optional.of(cart))

        def exception = assertThrows(CustomException) {
            checkoutService.checkout(cart.id, userWithoutAccess.id)
        }

        assert exception.message == "This is not your cart!"

        verify(cartRepository, times(1)).findById(cart.id)
        verifyNoInteractions(orderRepository)
    }

    @Test
    void checkoutInvalidQuantity() {
        def user = new User(id: 1L, name: "username")

        def product1 = new Product(id: 1, name: "product one", price: BigDecimal.TEN, quantity: 10L)
        def product2 = new Product(id: 2, name: "product two", price: BigDecimal.valueOf(20L), quantity: 20L)

        def cartProduct1 = new CartProduct(id: 1L, product: product1, quantity: 99L)
        def cartProduct2 = new CartProduct(id: 2L, product: product2, quantity: 99L)

        def cart = new Cart(id: 1L, user: user, cartProducts: [cartProduct1, cartProduct2])

        when(cartRepository.findById(cart.id)).thenReturn(Optional.of(cart))

        def exception = assertThrows(CustomException) {
            checkoutService.checkout(cart.id, user.id)
        }

        assert exception.message == "Quantity of product 'product one' exceeds available stock (10)."

        verify(cartRepository, times(1)).findById(cart.id)
        verifyNoInteractions(orderRepository)

    }
}
