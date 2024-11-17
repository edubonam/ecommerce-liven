package com.bonam.ecommerce.controller

import com.bonam.ecommerce.domain.Cart
import com.bonam.ecommerce.dto.ProductToCardRequestDTO
import com.bonam.ecommerce.service.CartService
import com.bonam.ecommerce.service.CheckoutService
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cart")
class CartController {

    @Autowired
    CartService cartService

    @Autowired
    CheckoutService checkoutService

    @PostMapping
    ResponseEntity<?> saveCart(@RequestBody Cart cart) {
        new ResponseEntity<>(cartService.saveCart(cart), HttpStatus.OK)
    }

    @PostMapping("/{id}/products")
    ResponseEntity<?> addProductsToCart(
            @PathVariable("id") Long id,
            @RequestBody ProductToCardRequestDTO request
    ) {
        def updatedCart = cartService.addProductsToCart(id, request)
        return ResponseEntity.ok(updatedCart)
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getCartById(@PathVariable("id") Long id, @RequestParam(name="userId") Long userId) {
        try {
            def cart = cartService.getCartById(id, userId)
            return ResponseEntity.ok(cart)
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body([error: e.message])
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body([error: e.message])
        }
    }

    @PostMapping("/{id}/checkout")
    ResponseEntity<?> checkout(@PathVariable("id") Long id, @RequestParam(name="userId") Long userId) {
        try {
            def order = checkoutService.checkout(id, userId)
            return ResponseEntity.ok(order)
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body([error: e.message])
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body([error: e.message])
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body([error: e.message])
        }
    }

}
