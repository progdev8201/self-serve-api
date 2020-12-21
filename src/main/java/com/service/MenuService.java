package com.service;

import com.model.dto.MenuDTO;
import com.model.dto.RestaurantSelectionDTO;
import com.model.entity.Menu;
import com.model.entity.Restaurant;
import com.model.enums.MenuType;
import com.repository.MenuRepository;
import com.repository.OwnerRepository;
import com.repository.RestaurantRepository;
import com.service.Util.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private OwnerRepository ownerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DTOUtils dtoUtils;


    public List<MenuDTO> findFoodMenuForRestaurants(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).get();
        return restaurant.getMenus()
                .stream()
                .filter(menu -> menu.getMenuType() == MenuType.FOOD)
                .map(menu -> dtoUtils.mapMenuToMenuDTO(menu))
                .collect(Collectors.toList());
    }

    public List<MenuDTO> findAllMenuForRestaurants(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).get();
        return restaurant.getMenus()
                .stream()
                .map(menu -> dtoUtils.mapMenuToMenuDTO(menu))
                .collect(Collectors.toList());
    }

    public ResponseEntity deleteMenuFromRestaurantList(Long restaurantId, Long menuId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();

        Menu menuToRemove = restaurant.getMenus()
                .stream()
                .filter(menu -> menu.getId() == menuId)
                .findFirst().get();

        if (menuToRemove.getMenuType().equals(MenuType.WAITERREQUEST))
            return ResponseEntity.badRequest().body("Menu of type WAITERREQUEST is not deletable");

        restaurant.getMenus().remove(menuToRemove);

        restaurantRepository.save(restaurant);

        return ResponseEntity.ok().build();
    }


    public MenuDTO createMenu(Long restoId, String menuName, MenuType menuType) {
        Restaurant restaurant = restaurantRepository.findById(restoId).get();
        if (Objects.nonNull(findMenuInRestaurantByName(menuName, restaurant))) {
            return null;
        }
        Menu menu = new Menu();
        menu.setName(menuName);
        menu.setProducts(new ArrayList<>());
        menu.setMenuType(menuType);
        restaurant.getMenus().add(menu);
        menu = findMenuInRestaurantByName(menuName, restaurantRepository.save(restaurant));
        return dtoUtils.mapMenuToMenuDTO(menu);
    }

    public MenuDTO updateMenu(Long menuId, String menuName, MenuType menuType) {
        Menu menu = menuRepository.findById(menuId).get();
        menu.setName(menuName);
        menu.setMenuType(menuType);
        menu = menuRepository.save(menu);
        return dtoUtils.mapMenuToMenuDTO(menuRepository.save(menu));
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
