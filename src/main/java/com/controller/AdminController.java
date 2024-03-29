package com.controller;

import com.model.dto.OwnerDTO;
import com.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("getOwners")
    public ResponseEntity<List<OwnerDTO>> getAllOwners (){
        return ResponseEntity.ok(adminService.getAllOwners());
    }
}
