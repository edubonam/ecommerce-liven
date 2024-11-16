package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.Product
import com.bonam.ecommerce.repository.ProductRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class ProductServiceImpl implements ProductService{

    ProductRepository productRepository

    @Override
    List<Product> findAll() {
        productRepository.findAll()
    }

    @Override
    Product findBySku(String sku) {
        productRepository.findById sku get()
    }

    @Override
    Product saveProduct(Product product) {
        productRepository.save(product)
    }
}
