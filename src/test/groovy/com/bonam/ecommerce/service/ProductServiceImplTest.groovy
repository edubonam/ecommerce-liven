package com.bonam.ecommerce.service

import com.bonam.ecommerce.domain.Product
import com.bonam.ecommerce.exception.CustomException
import com.bonam.ecommerce.repository.ProductRepository
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

import static org.junit.jupiter.api.Assertions.assertThrows
import static org.mockito.Mockito.*

class ProductServiceImplTest {

    private final ProductRepository productRepository = mock(ProductRepository)
    private final ProductServiceImpl productService = new ProductServiceImpl(
            productRepository: productRepository
    )

    @Test
    void findAll() {
        def product1 = new Product(id: 1, name: "product one", price: BigDecimal.TEN, quantity: 10L)
        def product2 = new Product(id: 2, name: "product two", price: BigDecimal.ONE, quantity:  0L)

        when(productRepository.findAll()).thenReturn([product1, product2])

        def result = productService.findAll()

        assert result.size() == 2: "List should have 2 products"

        verify(productRepository, times(1)).findAll()
    }

    @Test
    void getPaginatedProducts() {
        def product1 = new Product(id: 1, name: "product one", price: BigDecimal.TEN, quantity: 10L)
        def product2 = new Product(id: 2, name: "product two", price: BigDecimal.ONE, quantity: 20L)

        def pageable = PageRequest.of(0, 2)
        def page = new PageImpl([product1, product2])

        when(productRepository.findByQuantityGreaterThan(0, pageable)).thenReturn(page)

        def result = productService.getPaginatedProducts(0, 2)

        assert result.size() == 2: "List should have 2 products"

        verify(productRepository, times(1)).findByQuantityGreaterThan(0, pageable)
    }

    @Test
    void saveNewProduct() {
        def product1 = new Product(
                id: null,
                sku:"sku-1",
                name: "product one",
                price: BigDecimal.TEN,
                quantity: 10L
        )

        when(productRepository.findBySku(product1.sku)).thenReturn(Optional.empty())
        when(productRepository.save(product1)).thenReturn(product1)

        def result = productService.saveProduct(product1)

        assert result != null : "Result should not be null"
        assert result.sku == product1.sku: "Product SKU should match"
        assert result.price == product1.price: "Product price should match"
        assert result.quantity == product1.quantity: "Product quantity should match"

        verify(productRepository, times(1)).save(product1)
        verify(productRepository, times(1)).findBySku(product1.sku)
    }

    @Test
    void saveNewProductInvalidSku() {
        def product1 = new Product(
                id: 1,
                sku:"sku-1",
                name: "product one",
                price: BigDecimal.TEN,
                quantity: 10L
        )

        def product2 = new Product(
                id: null,
                sku:"sku-1",
                name: "product two",
                price: BigDecimal.TEN,
                quantity: 20L
        )

        when(productRepository.findBySku(product2.sku)).thenReturn(Optional.of(product1))
        when(productRepository.save(product2)).thenReturn(product2)

        def exception = assertThrows(CustomException) {
            productService.saveProduct(product2)
        }

        assert exception.message == "Sku already exists!"

        verify(productRepository, times(1)).findBySku(product2.sku)
    }

    @Test
    void saveExistsProduct() {
        def product = new Product(
                id: 1,
                sku:"sku-1",
                name: "product one",
                price: BigDecimal.TEN,
                quantity: 10L
        )

        def productUpdated = new Product(
                id: 1,
                sku:"sku-1",
                name: "product one updated",
                price: BigDecimal.ONE,
                quantity: 10L
        )

        when(productRepository.findById(product.id)).thenReturn(Optional.of(product))
        when(productRepository.save(product)).thenReturn(productUpdated)

        def result = productService.saveProduct(product)

        assert result != null : "Result should not be null"
        assert result.id == productUpdated.id: "Product ID should match"
        assert result.sku == productUpdated.sku: "Product SKU should match"
        assert result.price == productUpdated.price: "Product price should match"
        assert result.quantity == productUpdated.quantity: "Product quantity should match"

        verify(productRepository, times(1)).save(product)
        verify(productRepository, times(1)).findById(product.id)
    }

    @Test
    void saveExistsProductInvalidSku() {
        def product = new Product(
                id: 1,
                sku:"sku-1",
                name: "product one",
                price: BigDecimal.TEN,
                quantity: 10L
        )

        def productUpdated = new Product(
                id: 1,
                sku:"sku-2",
                name: "product one updated",
                price: BigDecimal.ONE,
                quantity: 10L
        )

        when(productRepository.findById(product.id)).thenReturn(Optional.of(product))
        when(productRepository.save(product)).thenReturn(productUpdated)

        def exception = assertThrows(CustomException) {
            productService.saveProduct(productUpdated)
        }

        assert exception.message == "Cannot update sku value!"

        verify(productRepository, times(1)).findById(product.id)
    }
}
