package com.bonam.ecommerce.service;

import com.bonam.ecommerce.domain.Cart;
import com.bonam.ecommerce.domain.CartProduct;
import com.bonam.ecommerce.domain.Product;
import com.bonam.ecommerce.domain.User;
import com.bonam.ecommerce.dto.CartDTO;
import com.bonam.ecommerce.dto.ProductDTO;
import com.bonam.ecommerce.dto.ProductToCardRequestDTO;
import com.bonam.ecommerce.exception.CustomException;
import com.bonam.ecommerce.repository.CartRepository;
import com.bonam.ecommerce.repository.ProductRepository;
import com.bonam.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    private final CartRepository cartRepository = Mockito.mock(CartRepository.class);
    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final CartServiceImpl cartService = new CartServiceImpl();

    CartServiceImplTest() {
        cartService.setCartRepository(cartRepository);
        cartService.setProductRepository(productRepository);
        cartService.setUserRepository(userRepository);
    }

    @Test
    void saveCart() {

        Cart cart = new Cart();
        cart.setId(1L);

        when(cartRepository.save(cart)).thenReturn(cart);

        // Act
        CartDTO result = cartService.saveCart(cart);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartRepository, times(1)).save(cart);

        assertEquals("saveCart", "");
    }

    @Test
    void addProductsToCart() {
        assertEquals("addProductsToCart", "");
    }

    @Test
    void addProductsToCartInvalidUser() {
        assertEquals("addProductsToCartInvalidUser", "");
    }

    @Test
    void addProductsToCartInvalidCart() {
        assertEquals("addProductsToCartInvalidCart", "");
    }

    @Test
    void addProductsToCartUserUnauthorized() {
        assertEquals("addProductsToCartUserUnauthorized", "");
    }

    @Test
    void addProductsToCartInvalidProduct() {
        assertEquals("addProductsToCartInvalidProduct", "");
    }

    @Test
    void getCartById() throws Exception {
        assertEquals("getCartById", "");

    }

    @Test
    void getCartByIdInvalidCart() {
        assertEquals("getCartByIdInvalidCart", "");
    }

    @Test
    void getCartByIdUserUnauthorized() {
        assertEquals("getCartByIdUserUnauthorized", "");
    }
}
