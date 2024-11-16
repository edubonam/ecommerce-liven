package com.bonam.ecommerce.controller

import com.bonam.ecommerce.domain.Product
import com.bonam.ecommerce.service.ProductService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
class ProductController {

    private ProductService productService

    @GetMapping
    List<Product> getAllProductList() {
        productService.findAll()
    }

    //APAGAR ESSE CARA BONAM
    @GetMapping("/ping")
    static String getPong() {
        "Pong"
    }

    @PostMapping
    Product saveProduct(@RequestBody Product product) {
        productService.saveProduct(product)
    }

    @GetMapping("/{sku}")
    Product findBySku(@PathVariable String sku) {
        productService.findBySku(sku)
    }
}
