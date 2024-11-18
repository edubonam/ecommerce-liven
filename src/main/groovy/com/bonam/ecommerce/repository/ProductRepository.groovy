package com.bonam.ecommerce.repository

import com.bonam.ecommerce.domain.Product
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable;

@Repository
interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByQuantityGreaterThan(int value, Pageable pageable);

    Optional<Product> findBySku(String sku);
}