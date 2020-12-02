package com.service;

import com.mapping.*;
import com.model.dto.*;
import com.model.entity.*;
import com.model.enums.MenuType;
import com.repository.MenuRepository;
import com.repository.OwnerRepository;
import com.repository.ProductRepository;
import com.repository.RestaurantRepository;
import com.service.Util.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private RestaurantRepository restaurantRepository;

    @Autowired
    DTOUtils dtoUtils;


    public List<MenuDTO> findAllMenuForRestaurants(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).get();
        return restaurant.getMenus().stream()
                .filter(menu -> menu.getMenuType() == MenuType.FOOD)
                .map(menu -> dtoUtils.mapMenuToMenuDTO(menu))
                .collect(Collectors.toList());
    }

    public MenuDTO createMenu(Long restoId, String menuName) {
        Restaurant restaurant = restaurantRepository.findById(restoId).get();
        if (Objects.nonNull(findMenuInRestaurantByName(menuName, restaurant))) {
            return null;
        }
        Menu menu = new Menu();
        menu.setName(menuName);
        menu.setProducts(new ArrayList<>());
        restaurant.getMenus().add(menu);
        menu = findMenuInRestaurantByName(menuName, restaurantRepository.save(restaurant));
        return dtoUtils.mapMenuToMenuDTO(menu);
    }

    private Menu findMenuInRestaurantByName(String menuName, Restaurant restaurant) {
        Menu menu = restaurant.getMenus()
                .stream()
                .filter(x -> x.getName().contentEquals(menuName))
                .findFirst().orElse(null);
        return menu;
    }

    public List<RestaurantSelectionDTO> findAllRestaurantName(String ownerUsername) {
        List<RestaurantSelectionDTO> restaurantSelectionDTOS = new ArrayList<>();

        ownerRepository.findByUsername(ownerUsername).ifPresent(owner -> {
            owner.getRestaurantList().forEach(restaurant -> {
                restaurantSelectionDTOS.add(dtoUtils.mapRestaurantToRestaurantSelectionDTO(restaurant));
            });
        });

        return restaurantSelectionDTOS;
    }

}
