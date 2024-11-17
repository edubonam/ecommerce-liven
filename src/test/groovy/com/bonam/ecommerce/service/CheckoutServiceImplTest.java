package com.bonam.ecommerce.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckoutServiceImplTest {

    @Test
    void checkout() {
        assertEquals("checkout", "");
    }

    @Test
    void checkoutInvalidCart() {
        assertEquals("checkoutInvalidCart", "");
    }

    @Test
    void checkoutUserUnauthorized() {
        assertEquals("checkoutUserUnauthorized", "");
    }

    @Test
    void checkoutInvalidQuantity() {
        assertEquals("checkoutInvalidQuantity", "");
    }
}
