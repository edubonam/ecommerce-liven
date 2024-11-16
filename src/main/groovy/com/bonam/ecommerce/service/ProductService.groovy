package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.Product

interface ProductService {

    List<Product> findAll()

    Product findBySku(String sku)

    Product saveProduct(Product product)
}
