package com.controller;

import com.mapping.ProductDTOToProduct;
import com.model.dto.ProductDTO;
import com.model.entity.Menu;
import com.model.entity.Product;
import com.repository.MenuRepository;
import com.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuRepository menuRepository;

    @GetMapping("/{id}")
    public Product find(@PathVariable Long id) {
        return productRepository.findById(id).get();
    }

    @GetMapping("/menu/{id}")
    public List<Product> findAllProductFromMenu(@PathVariable Long id) {
        return menuRepository.findById(id).get().getProducts();
    }

    @PostMapping("/{menuId}")
    public void create(@RequestBody ProductDTO productDTO, @PathVariable Long menuId) {
        Product product = ProductDTOToProduct.instance.convert(productDTO);
        Menu menu = menuRepository.findById(menuId).get();
        menu.getProducts().add(product);
        menuRepository.save(menu);
    }

    @PutMapping()
    public void update(@RequestBody ProductDTO productDTO) {
        Product product = ProductDTOToProduct.instance.convert(productDTO);
        productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productRepository.deleteById(id);
    }


}
