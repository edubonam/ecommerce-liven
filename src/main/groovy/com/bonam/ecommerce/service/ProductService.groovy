package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.Product
import com.bonam.ecommerce.dto.ProductDTO
import org.springframework.data.domain.Page

interface ProductService {

    List<Product> findAll()

    Page<ProductDTO> getPaginatedProducts(int page, int size)

    ProductDTO saveProduct(Product product)

}
