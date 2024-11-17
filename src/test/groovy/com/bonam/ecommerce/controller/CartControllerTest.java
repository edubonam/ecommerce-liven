package com.bonam.ecommerce.controller;

import com.bonam.ecommerce.config.security.SecurityFilter;
import com.bonam.ecommerce.domain.Cart;
import com.bonam.ecommerce.dto.CartDTO;
import com.bonam.ecommerce.dto.OrderDTO;
import com.bonam.ecommerce.dto.ProductToCardRequestDTO;
import com.bonam.ecommerce.service.CartService;
import com.bonam.ecommerce.service.CheckoutService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private CheckoutService checkoutService;

    @MockBean
    private SecurityFilter securityFilter;

    @Test
    void save() throws Exception {
        CartDTO mockCart = new CartDTO();
        mockCart.setId(1L);

        Mockito.when(cartService.saveCart(any(Cart.class))).thenReturn(mockCart);

        mockMvc.perform(MockMvcRequestBuilders.post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void addProductsToCart() throws Exception {
        CartDTO mockCart = new CartDTO();
        mockCart.setId(1L);

        Mockito.when(cartService.addProductsToCart(eq(1L), any(ProductToCardRequestDTO.class))).thenReturn(mockCart);

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"quantity\":2}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getCartById() throws Exception {
        CartDTO mockCart = new CartDTO();
        mockCart.setId(1L);

        Mockito.when(cartService.getCartById(1L, 10L)).thenReturn(mockCart);

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/1")
                        .param("userId", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getCartByIdNotFound() throws Exception {
        Mockito.when(cartService.getCartById(1L, 10L)).thenThrow(new EntityNotFoundException("Cart not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/1")
                        .param("userId", "10")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cart not found"));
    }

    @Test
    void checkout() throws Exception {
        OrderDTO mockOrder = new OrderDTO();

        Mockito.when(checkoutService.checkout(1L, 10L)).thenReturn(mockOrder);

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/1/checkout")
                        .param("userId", "10")
                )
                .andExpect(status().isOk());
    }

    @Test
    void checkoutOrderNotFound() throws Exception {
        Mockito.when(checkoutService.checkout(1L, 10L)).thenThrow(new EntityNotFoundException("Order not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/1/checkout")
                        .param("userId", "10")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Order not found"));
    }

    @Test
    void checkoutInvalidCart() throws Exception {
        Mockito.when(checkoutService.checkout(1L, 10L)).thenThrow(new IllegalArgumentException("Invalid cart"));

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/1/checkout")
                        .param("userId", "10")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid cart"));
    }
}
