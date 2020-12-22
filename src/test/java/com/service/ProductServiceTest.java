package com.service;

import com.model.dto.ProductDTO;
import com.model.entity.ImgFile;
import com.model.entity.Product;
import com.model.entity.Restaurant;
import com.repository.ImgFileRepository;
import com.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest

public class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @MockBean
    ImgFileRepository imgFileRepository;

    @Captor
    ArgumentCaptor<Product> productArgumentCaptor;

    @Test
    public void uploadImage() throws IOException {
        // Arrange
        Long productId = 1L;
        String fileName = "superimagetest.png";
        Product product = new Product();
        product.setCheckItems(new ArrayList<>());
        product.setOptions(new ArrayList<>());
        product.setOrderItems(new ArrayList<>());
        Mockito.when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        MockMultipartFile file = new MockMultipartFile("mysuperfile", fileName, "multipart/form-data", "salut".getBytes());

        Mockito.when(productRepository.save(any(Product.class))).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                return (Product) invocation.getArguments()[0];
            }
        });

        Mockito.when(imgFileRepository.save(any(ImgFile.class))).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                return (ImgFile) invocation.getArguments()[0];
            }
        });

        // Act
        productService.uploadFile(file, productId);

        Mockito.verify(productRepository).save(productArgumentCaptor.capture());
        // Assert
        assertEquals(productArgumentCaptor.getValue().getImgFile().getFileName(), fileName);
        assertNotNull(productArgumentCaptor.getValue().getImgFile().getData());
    }
}
