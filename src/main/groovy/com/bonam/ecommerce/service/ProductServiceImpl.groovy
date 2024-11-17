package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.Product
import com.bonam.ecommerce.dto.ProductDTO
import com.bonam.ecommerce.exception.CustomException
import com.bonam.ecommerce.repository.ProductRepository
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable;

@Service
class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository

    @Override
    List<Product> findAll() {
        productRepository.findAll()
    }

    @Override
    Page<ProductDTO> getPaginatedProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size)
        return productRepository.findByQuantityGreaterThan(0, pageable).map {return new ProductDTO().fromDomain(it)}
    }

    @Override
    ProductDTO saveProduct(@Valid Product product) {

        if (isNewProduct(product))
            verifyBeforeUpdate(product)
         else
            verifyBeforeInsert(product)


        return new ProductDTO().fromDomain(productRepository.save(product))
    }

    private static boolean isNewProduct(Product product) {
        product.getId() != null
    }

    private void verifyBeforeUpdate(Product updatedProduct) {
        Optional<Product> product = productRepository.findById(updatedProduct.id)

        if (product.get().sku != updatedProduct.sku)
            throw new CustomException('Cannot update sku value!')

    }

    private void verifyBeforeInsert(Product insertProduct) {
        Optional<Product> exitsProduct = productRepository.findBySku(insertProduct.sku)

        exitsProduct.ifPresent {throw new CustomException('Sku already exists!')}

    }
}
