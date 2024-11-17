package com.bonam.ecommerce.controller

import com.bonam.ecommerce.domain.Product
import com.bonam.ecommerce.dto.ProductDTO
import com.bonam.ecommerce.service.ProductService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController {

    @Autowired
    ProductService productService

    @GetMapping
    ResponseEntity<List<Product>> getAllProductList() {

        new ResponseEntity<>(productService.findAll(), HttpStatus.OK)
    }

    @GetMapping("/page")
    ResponseEntity<Page<ProductDTO>> getPaginatedProducts(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        Page<Product> products = productService.getPaginatedProducts(page, size)
        return new ResponseEntity<>(products, HttpStatus.OK)
    }

    @PostMapping
    ResponseEntity<ProductDTO> saveProduct(@RequestBody @Valid Product product) {
        new ResponseEntity<>(productService.saveProduct(product), HttpStatus.OK)
    }
}
