package com.service;

import com.mapping.*;
import com.model.dto.*;
import com.model.entity.*;
import com.repository.MenuRepository;
import com.repository.OwnerRepository;
import com.repository.ProductRepository;
import com.service.Util.DTOUtils;
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
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    DTOUtils dtoUtils;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    //permet de set des obj en vedette
    public MenuDTO createSpecial(MenuDTO menuDTO, List<ProductDTO> specialProducts) {
        List<Product> products = findSpecialsInBD(specialProducts);
        Menu menu = menuRepository.findById(menuDTO.getId()).get();

        if (Objects.isNull(menu.getSpeciaux())) {
            menu.setSpeciaux(products);
        } else {
            menu.getSpeciaux().addAll(products);
            menu.setSpeciaux(menu.getSpeciaux());
        }
        menu = menuRepository.save(menu);
        return dtoUtils.generateMenuDTO(menu);
    }


    //permet de set des obj en vedette
    public MenuDTO removeSpecial(MenuDTO menuDTO, List<ProductDTO> specialProducts) {
        List<Product> products = findSpecialsInBD(specialProducts);
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> speciauxDuMenu = menu.getSpeciaux();
        speciauxDuMenu.removeAll(products);
        menu.setSpeciaux(speciauxDuMenu);
        menu = menuRepository.save(menu);
        return dtoUtils.generateMenuDTO(menu);
    }

    public MenuDTO findMenu(Long id) {
        Menu menu = menuRepository.findById(id).get();
        MenuDTO menuDTO = MenuToMenuDTO.instance.convert(menu);
        menuDTO.setSpeciaux(productService.findMenuSpecials(menuDTO));
        menuDTO.setFeatured(productService.findMenuChoixDuChef(menuDTO));
        menuDTO.setDejeuner(productService.findMenuDejeunerProduct(menuDTO));
        menuDTO.setSouper(productService.findMenuSouper(menuDTO));
        menuDTO.setDiner(productService.findMenuDinerProduct(menuDTO));
        menuDTO.setProducts(dtoUtils.mapProductListToProductDTOList(menu.getProducts()));

        return menuDTO;
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
        MenuDTO returnValue = MenuToMenuDTO.instance.convert(menuRepository.save(menu));
        returnValue.setSpeciaux(new ArrayList<>());
        for (Product special : menu.getSpeciaux()) {
            returnValue.getSpeciaux().add(ProductToProductDTO.instance.convert(special));
            returnValue.setSpeciaux(returnValue.getSpeciaux());
        }
        return returnValue;
    }

    public List<RestaurantSelectionDTO> findAllRestaurantName(String ownerUsername){
        List<RestaurantSelectionDTO> restaurantSelectionDTOS = new ArrayList<>();

        ownerRepository.findByUsername(ownerUsername).ifPresent(owner ->{
            owner.getRestaurantList().forEach(restaurant -> {
                restaurantSelectionDTOS.add(new RestaurantSelectionDTO(restaurant.getId(), restaurant.getMenu().getId(),restaurant.getName(),restaurant.getRestaurentTables()));
            });
        });

        return restaurantSelectionDTOS;
    }

}
