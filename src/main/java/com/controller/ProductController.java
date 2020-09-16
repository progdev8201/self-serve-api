package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapping.ProductDTOToProduct;
import com.model.dto.MenuDTO;
import com.model.dto.ProductDTO;
import com.model.entity.Menu;
import com.model.entity.Product;
import com.model.enums.ProductType;
import com.repository.MenuRepository;
import com.repository.ProductRepository;
import com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    //TODO put dto instead of entity
    @GetMapping("/{id}")
    public Product find(@PathVariable Long id) {
        return productService.find(id);
    }

    @GetMapping("/menu/{id}")
    public List<Product> findAllProductFromMenu(@PathVariable Long id) {
        return productService.findAllProductFromMenu(id);
    }

    @PostMapping("/{menuId}")
    public void create(@RequestBody ProductDTO productDTO, @PathVariable Long menuId) {
        productService.create(productDTO, menuId);
    }

    @PutMapping()
    public void update(@RequestBody ProductDTO productDTO) {
        productService.update(productDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @PostMapping("/findMenuSpecial")
    public ResponseEntity<List<ProductDTO>> findMenuSpecials(@RequestBody Map<String, String> json) throws JsonProcessingException {
        MenuDTO menuDTO = new ObjectMapper().readValue(json.get("menuDTO"), MenuDTO.class);
        return ResponseEntity.ok(productService.findMenuSpecials(menuDTO));
    }

    @PostMapping("/findChoixDuChef")
    public ResponseEntity<List<ProductDTO>> findMenuChoixDuChef(@RequestBody Map<String, String> json) throws JsonProcessingException {
        MenuDTO menuDTO = new ObjectMapper().readValue(json.get("menuDTO"), MenuDTO.class);
        return ResponseEntity.ok(productService.findMenuChoixDuChef(menuDTO));
    }

    @PostMapping("/setProductSpecial")
    public ResponseEntity<ProductDTO> setProductSpecial(@RequestBody Map<String, String> json) throws JsonProcessingException {
        ProductDTO productDTO = new ObjectMapper().readValue(json.get("productDTO"), ProductDTO.class);
        return ResponseEntity.ok(productService.setProductSpecial(productDTO));

    }

    @PostMapping("/deleteProductType")
    public ResponseEntity<ProductDTO> removeProductType(@RequestBody Map<String, String> json) throws JsonProcessingException {
        ProductDTO productDTO = new ObjectMapper().readValue(json.get("productDTO"), ProductDTO.class);
        return ResponseEntity.ok(productService.removeProductType(productDTO));
    }

    @PostMapping("/setMenuChefChoice")
    public ResponseEntity<ProductDTO> setProductChefChoice(@RequestBody Map<String, String> json) throws JsonProcessingException {
        ProductDTO productDTO = new ObjectMapper().readValue(json.get("productDTO"), ProductDTO.class);
        return ResponseEntity.ok(productService.setProductChefChoice(productDTO));
    }


}
