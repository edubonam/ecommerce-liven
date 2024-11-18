package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.Cart
import com.bonam.ecommerce.domain.CartProduct
import com.bonam.ecommerce.domain.Product
import com.bonam.ecommerce.domain.User
import com.bonam.ecommerce.dto.ProductToCardRequestDTO
import com.bonam.ecommerce.dto.ProductToCartDTO
import com.bonam.ecommerce.exception.CustomException
import com.bonam.ecommerce.repository.CartRepository
import com.bonam.ecommerce.repository.ProductRepository
import com.bonam.ecommerce.repository.UserRepository
import org.junit.jupiter.api.Test

import static org.mockito.Mockito.*
import static org.junit.jupiter.api.Assertions.assertThrows


class CartServiceImplTest {

    private final CartRepository cartRepository = mock(CartRepository)
    private final ProductRepository productRepository = mock(ProductRepository)
    private final UserRepository userRepository = mock(UserRepository)

    private final CartServiceImpl cartService = new CartServiceImpl(
            cartRepository: cartRepository,
            productRepository: productRepository,
            userRepository: userRepository
    )

    @Test
    void saveCart() {
        def user = new User(id: 1L, name: "username")

        def product1 = new Product(id: 1, price: BigDecimal.TEN, quantity: 10L)
        def product2 = new Product(id: 2, price: BigDecimal.valueOf(20L), quantity: 20L)

        def cartProduct1 = new CartProduct(id: 1L, product: product1, quantity: 1L)
        def cartProduct2 = new CartProduct(id: 2L, product: product2, quantity: 2L)
        def cartProducts = [cartProduct1, cartProduct2]

        def cart = new Cart(id: 1L, user:user, cartProducts: cartProducts)

        when(cartRepository.save(cart)).thenReturn(cart)

        def result = cartService.saveCart(cart)

        assert result != null : "Resulting CartDTO should not be null"
        assert result.id == 1L : "Resulting CartDTO should have ID 1"
        assert result.user.id == 1L : "Resulting CartDTO should have User ID 1"
        assert result.products.size() == 2 : "Resulting CartDTO should have 2 cart products"

        def resultProduct1 = result.products.find { it.productId == 1 }
        def resultProduct2 = result.products.find { it.productId == 2 }

        assert resultProduct1 != null : "CartDTO should include product with ID 1"
        assert resultProduct1.productId == 1 : "First product ID should match"
        assert resultProduct1.quantity == 1L : "First product quantity should be 1"

        assert resultProduct2 != null : "CartDTO should include product with ID 2"
        assert resultProduct2.productId == 2 : "Second product ID should match"
        assert resultProduct2.quantity == 2L : "Second product quantity should be 2"

        verify(cartRepository, times(1)).save(cart)
    }

    @Test
    void addProductsToCart() {
        def user = new User(id: 1L, name: "username")

        def product1 = new Product(id: 1, price: BigDecimal.TEN, quantity: 10L)
        def product2 = new Product(id: 2, price: BigDecimal.valueOf(20L), quantity: 20L)

        def cartProduct1 = new CartProduct(id: 1L, product: product1, quantity: 1L)
        def cartProduct2 = new CartProduct(id: 2L, product: product2, quantity: 2L)

        def cart = new Cart(id: 1L, user: user, cartProducts: [cartProduct1])
        def cartResult = new Cart(id: 1L, user: user, cartProducts: [cartProduct1, cartProduct2])

        def productToCartDTO1 = new ProductToCartDTO(productId: 1L, quantity: 1L)
        def productToCartDTO2 = new ProductToCartDTO(productId: 2L, quantity: 2L)

        def productToCardRequestDTO = new ProductToCardRequestDTO(userId: user.id, products: [productToCartDTO1, productToCartDTO2])

        when(userRepository.findById(user.id)).thenReturn(Optional.of(user))
        when(cartRepository.findById(cart.id)).thenReturn(Optional.of(cart))

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1))
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2))

        when(cartRepository.save(any())).thenReturn(cartResult)

        def result = cartService.addProductsToCart(1L, productToCardRequestDTO)

        assert result != null : "Result should not be null"
        assert result.id == 1L : "Cart ID should match"
        assert result.products.size() == 2 : "Cart should have 2 products"

        def resultProduct1 = result.products.find { it.productId == 1L }
        def resultProduct2 = result.products.find { it.productId == 2L }

        assert resultProduct1.quantity == 2L : "Product 1 quantity should be updated to 2"
        assert resultProduct2.quantity == 2L : "Product 2 should be added with quantity 2"

        verify(userRepository, times(1)).findById(user.id)
        verify(cartRepository, times(1)).findById(cart.id)
        verify(productRepository, times(1)).findById(1L)
        verify(productRepository, times(1)).findById(2L)
        verify(cartRepository, times(1)).save(any(Cart))
    }

    @Test
    void addProductsToCartInvalidUser() {
        def productToCartDTO1 = new ProductToCartDTO(productId: 1L, quantity: 1L)
        def productToCartDTO2 = new ProductToCartDTO(productId: 2L, quantity: 2L)

        def productToCardRequestDTO = new ProductToCardRequestDTO(userId: 1L, products: [productToCartDTO1, productToCartDTO2])

        when(userRepository.findById(1L)).thenReturn(Optional.empty())

        def exception = assertThrows(CustomException) {
            cartService.addProductsToCart(1L, productToCardRequestDTO)
        }

        assert exception.message == "Invalid User!"

        verify(userRepository, times(1)).findById(1L)
        verifyNoInteractions(cartRepository)
        verifyNoInteractions(productRepository)
    }

    @Test
    void addProductsToCartInvalidCart() {
        def user = new User(id: 1L, name: "username")

        def productToCartDTO1 = new ProductToCartDTO(productId: 1L, quantity: 1L)
        def productToCartDTO2 = new ProductToCartDTO(productId: 2L, quantity: 2L)

        def productToCardRequestDTO = new ProductToCardRequestDTO(userId: 1L, products: [productToCartDTO1, productToCartDTO2])

        when(userRepository.findById(user.id)).thenReturn(Optional.of(user))
        when(cartRepository.findById(1L)).thenReturn(Optional.empty())

        def exception = assertThrows(CustomException) {
            cartService.addProductsToCart(1L, productToCardRequestDTO)
        }

        assert exception.message == "Invalid Cart!"

        verify(userRepository, times(1)).findById(user.id)
        verify(cartRepository, times(1)).findById(1L)
        verifyNoInteractions(productRepository)
    }

    @Test
    void addProductsToCartUserUnauthorized() {
        def user = new User(id: 1L, name: "username1")
        def userWithoutAccess = new User(id: 2L, name: "username2")

        def cart = new Cart(id: 1L, user: user, cartProducts: [])

        def productToCartDTO1 = new ProductToCartDTO(productId: 1L, quantity: 1L)
        def productToCartDTO2 = new ProductToCartDTO(productId: 2L, quantity: 2L)

        def productToCardRequestDTO = new ProductToCardRequestDTO(userId: userWithoutAccess.id, products: [productToCartDTO1, productToCartDTO2])

        when(userRepository.findById(userWithoutAccess.id)).thenReturn(Optional.of(userWithoutAccess))
        when(cartRepository.findById(cart.id)).thenReturn(Optional.of(cart))

        def exception = assertThrows(CustomException) {
            cartService.addProductsToCart(cart.id, productToCardRequestDTO)
        }

        assert exception.message == "This is not your cart!"

        verify(userRepository, times(1)).findById(userWithoutAccess.id)
        verify(cartRepository, times(1)).findById(cart.id)
        verifyNoInteractions(productRepository)
    }

    @Test
    void addProductsToCartInvalidProduct() {
        def user = new User(id: 1L, name: "username")

        def cart = new Cart(id: 1L, user: user, cartProducts: [])

        def productToCartDTO1 = new ProductToCartDTO(productId: 1L, quantity: 1L)
        def productToCartDTO2 = new ProductToCartDTO(productId: 2L, quantity: 2L)

        def productToCardRequestDTO = new ProductToCardRequestDTO(userId: user.id, products: [productToCartDTO1, productToCartDTO2])

        when(userRepository.findById(user.id)).thenReturn(Optional.of(user))
        when(cartRepository.findById(cart.id)).thenReturn(Optional.of(cart))

        def exception = assertThrows(CustomException) {
            cartService.addProductsToCart(1L, productToCardRequestDTO)
        }

        assert exception.message == "Invalid product!"

        verify(userRepository, times(1)).findById(user.id)
        verify(cartRepository, times(1)).findById(cart.id)
        verify(productRepository, times(1)).findById(1L)
    }

    @Test
    void getCartById() {
        def user = new User(id: 1L, name: "username")

        def product1 = new Product(id: 1, price: BigDecimal.TEN, quantity: 10L)
        def product2 = new Product(id: 2, price: BigDecimal.valueOf(20L), quantity: 20L)

        def cartProduct1 = new CartProduct(id: 1L, product: product1, quantity: 1L)
        def cartProduct2 = new CartProduct(id: 2L, product: product2, quantity: 2L)

        def cart = new Cart(id: 1L, user: user, cartProducts: [cartProduct1, cartProduct2])

        when(cartRepository.findById(cart.id)).thenReturn(Optional.of(cart))

        def result = cartService.getCartById(cart.id, user.id)

        assert result != null : "Result should not be null"
        assert result.id == 1L : "Cart ID should match"
        assert result.products.size() == 2 : "Cart should have 2 products"

        verify(cartRepository, times(1)).findById(cart.id)
        verifyNoInteractions(userRepository)
        verifyNoInteractions(productRepository)
    }

    @Test
    void getCartByIdInvalidCart() {
        def user = new User(id: 1L, name: "username")

        when(cartRepository.findById(1L)).thenReturn(Optional.empty())

        def exception = assertThrows(CustomException) {
            cartService.getCartById(1L, user.id)
        }

        assert exception.message == "Invalid Cart!"

        verify(cartRepository, times(1)).findById(1L)
        verifyNoInteractions(userRepository)
        verifyNoInteractions(productRepository)
    }

    @Test
    void getCartByIdUserUnauthorized() {
        def user = new User(id: 1L, name: "username")
        def userWithoutAccess = new User(id: 2L, name: "username2")

        def product1 = new Product(id: 1, price: BigDecimal.TEN, quantity: 10L)
        def product2 = new Product(id: 2, price: BigDecimal.valueOf(20L), quantity: 20L)

        def cartProduct1 = new CartProduct(id: 1L, product: product1, quantity: 1L)
        def cartProduct2 = new CartProduct(id: 2L, product: product2, quantity: 2L)

        def cart = new Cart(id: 1L, user: user, cartProducts: [cartProduct1, cartProduct2])

        when(cartRepository.findById(cart.id)).thenReturn(Optional.of(cart))

        def exception = assertThrows(CustomException) {
            cartService.getCartById(cart.id, userWithoutAccess.id)
        }

        assert exception.message == "This is not your cart!"

        verify(cartRepository, times(1)).findById(cart.id)
        verifyNoInteractions(userRepository)
        verifyNoInteractions(productRepository)
    }
}
