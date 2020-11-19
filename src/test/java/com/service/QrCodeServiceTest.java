package com.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class QrCodeServiceTest {

    @Autowired
    private QrCodeService qrCodeService;

    @Test
    public void downloadQrCodeTest() throws IOException {
        // ARRANGE
        final int tableNumber = 5;
        final String fileName = "tableId" + tableNumber + ".png";

        // ACT
        ResponseEntity<Resource> response = qrCodeService.downloadQrCode(tableNumber);

        // ASSERT
        assertEquals(response.getHeaders().getContentDisposition().getFilename(), fileName);
        assertTrue(response.getBody() instanceof ByteArrayResource);
        assertNotNull(response.getBody());
    }
}
