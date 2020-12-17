package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.MenuDTO;
import com.model.dto.ProductDTO;
import com.repository.MenuRepository;
import com.repository.ProductRepository;
import com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//TODO les methode devrais seulement avoir une seule ligne de code, le map de string devrait etre passer au service
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ProductService productService;


    //GET MAPPING

    @GetMapping("/{id}")
    public ProductDTO find(@PathVariable Long id) {
        return productService.find(id);
    }

    @GetMapping("/menu/{id}")
    public List<ProductDTO> findAllProductFromMenu(@PathVariable Long id) {
        return productService.findAllProductFromMenu(id);
    }

    @GetMapping("/findWaiterRequestProducts/{id}")
    public MenuDTO findAllWaiterRequestProductFromMenu(@PathVariable Long id) {
        return productService.findMenuWaiterRequest(id);
    }

    @GetMapping("/getProductImg/{imgId}")
    public ResponseEntity<?> getImg(@PathVariable Long imgId) throws IOException {
        return ResponseEntity.ok(productService.returnImgAsByteArrayString(imgId));
    }

    //POST MAPPING

    @PostMapping("/{menuId}")
    public ProductDTO create(@RequestBody ProductDTO productDTO, @PathVariable Long menuId) {
        return productService.create(productDTO, menuId);
    }


    @PostMapping("/deleteMenuType")
    public ResponseEntity<ProductDTO> removeMenuType(@RequestBody Map<String, String> json) throws JsonProcessingException {
        ProductDTO productDTO = new ObjectMapper().readValue(json.get("productDTO"), ProductDTO.class);
        return ResponseEntity.ok(productService.removeMenuType(productDTO));
    }

    @PostMapping("/image/{productId}")
    public ResponseEntity<?> saveProductImg(@RequestParam("file") MultipartFile file,@PathVariable long productId) throws IOException {
        return ResponseEntity.ok(productService.uploadFile(file, productId));
    }

    //PUT MAPPING

    @PutMapping
    public void update(@RequestBody ProductDTO productDTO) {
        productService.update(productDTO);
    }

    // DELETE MAPPING

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
