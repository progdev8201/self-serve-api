package com.controller;

import com.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qrcode")
public class QrCodeController {
    @Autowired
    private QrCodeService qrCodeService;

    @GetMapping("/download/{tableId}")
    public ResponseEntity<Resource> download(@PathVariable final int tableId) {
        return qrCodeService.downloadQrCode(tableId);
    }
}
