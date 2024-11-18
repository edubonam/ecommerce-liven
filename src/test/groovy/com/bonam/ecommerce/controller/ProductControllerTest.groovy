package com.bonam.ecommerce.controller

import com.bonam.ecommerce.config.security.SecurityFilter
import com.bonam.ecommerce.domain.Product
import com.bonam.ecommerce.dto.ProductDTO
import com.bonam.ecommerce.service.ProductService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc

import static org.mockito.ArgumentMatchers.any
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(ProductController)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private ProductService productService

    @MockBean
    private SecurityFilter securityFilter

    @Test
    void getAllProductList() {
        def product1 = new Product(id: 1L)
        def product2 = new Product(id: 2L)
        def productList = [product1, product2]

        Mockito.when(productService.findAll()).thenReturn(productList)

        mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(2))
                .andExpect(jsonPath('$[0].id').value(1))
                .andExpect(jsonPath('$[1].id').value(2))
    }

    @Test
    void getPaginatedProducts() {
        def productDTO = new ProductDTO(id: 1L)
        def productPage = new PageImpl<>([productDTO], PageRequest.of(0, 10), 1)

        Mockito.when(productService.getPaginatedProducts(0, 10)).thenReturn(productPage)

        mockMvc.perform(get("/products/page")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.content.length()').value(1))
                .andExpect(jsonPath('$.content[0].id').value(1))
    }

    @Test
    void saveProduct() {
        def productDTO = new ProductDTO(id: 1L, name: "Test Product")

        Mockito.when(productService.saveProduct(any(Product))).thenReturn(productDTO)

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"name":"Test Product"}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.id').value(1))
                .andExpect(jsonPath('$.name').value("Test Product"))
    }
}
