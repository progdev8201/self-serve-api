package com.service;

import com.mapping.MenuToMenuDTOImpl;
import com.mapping.ProductToProductDTO;
import com.model.dto.MenuDTO;
import com.model.dto.ProductDTO;
import com.model.entity.Menu;
import com.model.entity.Product;
import com.repository.MenuRepository;
import com.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class MenuService {
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }
    //permet de set des obj en vedette
    public MenuDTO createSpecial(MenuDTO menuDTO, List<ProductDTO> specialProducts){
        List<Product> products = findSpecialsInBD(specialProducts);
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        if(Objects.isNull(menu.getSpeciaux()))
        {
            menu.setSpeciaux(products);
        }
        else{
            menu.getSpeciaux().addAll(products);
            menu.setSpeciaux(menu.getSpeciaux());
        }
        menu=menuRepository.save(menu);
        return returnMenu(menu);
    }


    //permet de set des obj en vedette
    public MenuDTO removeSpecial(MenuDTO menuDTO, List<ProductDTO> specialProducts){
        List<Product> products = findSpecialsInBD(specialProducts);
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> speciauxDuMenu = menu.getSpeciaux();
        speciauxDuMenu.removeAll(products);
        menu.setSpeciaux(speciauxDuMenu);
        menu=menuRepository.save(menu);
        return returnMenu(menu);
    }

    private List<Product> findSpecialsInBD(List<ProductDTO> specialProducts) {
        List<Product> products = new ArrayList<>();
        for (ProductDTO productDTO : specialProducts) {
            Product product = productRepository.findById(productDTO.getId()).get();
            products.add(product);
        }
        return products;
    }
    private MenuDTO returnMenu(Menu menu) {
        MenuDTO returnValue = MenuToMenuDTOImpl.instance.convert(menuRepository.save(menu));
        returnValue.setSpeciaux(new ArrayList<>());
        for (Product special : menu.getSpeciaux()) {
            returnValue.getSpeciaux().add(ProductToProductDTO.instance.convert(special));
            returnValue.setSpeciaux(returnValue.getSpeciaux());
        }
        return returnValue;
    }


}