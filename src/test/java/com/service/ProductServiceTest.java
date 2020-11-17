package com.service;

import com.model.dto.ProductDTO;
import com.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("dev")
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    public void uploadImage() throws IOException {
        // Arrange
        Long productId = 1L;
        String fileName = "superimagetest.png";

        MockMultipartFile file = new MockMultipartFile("mysuperfile", fileName, "multipart/form-data", "salut".getBytes());

        // Act
        ProductDTO productDTO = productService.uploadFile(file, productId);

        // Assert
        assertEquals(productRepository.findById(productDTO.getId()).get().getImgFile().getFileName(), fileName);
        assertNotNull(productService.returnImgAsByteArrayString(productId));
    }
}
