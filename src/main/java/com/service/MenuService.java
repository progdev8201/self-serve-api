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
import com.service.validator.RestaurantOwnerShipValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private RestaurantOwnerShipValidator restaurantOwnerShipValidator;


    public List<MenuDTO> findFoodMenuForRestaurants(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).get();
        return restaurant.getMenus()
                .stream()
                .filter(menu -> menu.getMenuType() == MenuType.FOOD)
                .map(menu -> dtoUtils.mapMenuToMenuDTO(menu))
                .collect(Collectors.toList());
    }

    public ResponseEntity<List<MenuDTO>> findAllMenuForRestaurants(Long id) {
        if (!restaurantOwnerShipValidator.hasOwnerRight(id) && !restaurantOwnerShipValidator.isAdminConnected())
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Restaurant restaurant = restaurantRepository.findById(id).get();

        return ResponseEntity.ok(restaurant.getMenus()
                .stream()
                .map(menu -> dtoUtils.mapMenuToMenuDTO(menu))
                .collect(Collectors.toList()));
    }

    public ResponseEntity deleteMenuFromRestaurantList(Long restaurantId, Long menuId) {
        if (!restaurantOwnerShipValidator.hasOwnerRight(restaurantId) && !restaurantOwnerShipValidator.isAdminConnected())
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();

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


    public ResponseEntity createMenu(Long restoId, String menuName, MenuType menuType) {
        if (!restaurantOwnerShipValidator.hasOwnerRight(restoId) && !restaurantOwnerShipValidator.isAdminConnected())
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Restaurant restaurant = restaurantRepository.findById(restoId).get();

        if (findMenuInRestaurantByName(menuName, restaurant) != null) {
            return new ResponseEntity<String>("Fail -> Menu with same name already exists", HttpStatus.BAD_REQUEST);
        }

        Menu menu = new Menu();
        menu.setName(menuName);
        menu.setProducts(new ArrayList<>());
        menu.setMenuType(menuType);
        restaurant.getMenus().add(menu);
        menu = findMenuInRestaurantByName(menuName, restaurantRepository.save(restaurant));

        return ResponseEntity.ok(dtoUtils.mapMenuToMenuDTO(menu));
    }

    public MenuDTO updateMenu(Long menuId, String menuName, MenuType menuType) {
        Menu menu = menuRepository.findById(menuId).get();
        menu.setName(menuName);
        menu.setMenuType(menuType);
        menu = menuRepository.save(menu);
        return dtoUtils.mapMenuToMenuDTO(menuRepository.save(menu));
    }

    private Menu findMenuInRestaurantByName(String menuName, Restaurant restaurant) {
        return restaurant.getMenus()
                .stream()
                .filter(x -> x.getName().contentEquals(menuName))
                .findFirst().orElse(null);
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
