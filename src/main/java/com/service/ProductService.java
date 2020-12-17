package com.service;

import com.model.dto.MenuDTO;
import com.model.dto.ProductDTO;
import com.model.entity.*;
import com.model.enums.MenuType;
import com.repository.*;
import com.service.Util.DTOUtils;
import com.service.Util.ImgFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ImgFileRepository imgFileRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DTOUtils dtoUtils;


    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Value("${config.styles.images.path}")
    private String fileBasePath;


    //todo this method needs to go
    public ProductDTO find(Long id) {
        return DTOUtils.mapProductToProductDTO(productRepository.findById(id).get());
    }

    public List<ProductDTO> findAllProductFromMenu(Long id) {
        List<Product> productList = menuRepository.findById(id).get().getProducts();
        return productList.stream().
                map(DTOUtils::mapProductToProductDTO).
                collect(Collectors.toList());
    }

    public MenuDTO findMenuWaiterRequest(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).get();
        return restaurant.getMenus()
                .stream()
                .filter(menu -> menu.getMenuType()==MenuType.WAITERREQUEST)
                .map(menu -> dtoUtils.mapMenuToMenuDTO(menu))
                .collect(Collectors.toList()).get(0);
    }

    //todo fix create and update method way too much repetition and code to interact with a simple entity  (clean code)

    public ProductDTO create(ProductDTO productDTO, Long menuId) {
        // convert product dto to a product
        Product product = DTOUtils.mapProductDTOToProduct(productDTO, imgFileRepository);
        Menu menu =menuRepository.findById(menuId).get();
        product = linkMenuAndProduct(product, menu);

        return DTOUtils.mapProductToProductDTO(product);
    }

    private Product linkMenuAndProduct(Product finalProduct, Menu menu) {
        finalProduct.setMenu(menu);
        finalProduct.setMenuType(menu.getMenuType());
        menu.getProducts().add(finalProduct);
        menu =menuRepository.save(menu);
        //on retourne le dernier produit qu'on a save
        return menu.getProducts().get(menu.getProducts().size()-1);
    }

    public void update(ProductDTO productDTO) {
        productRepository.save(DTOUtils.mapProductDTOToProduct(productDTO, imgFileRepository));
    }

    public void delete(Long id) {
        // find one product
        Product product = productRepository.findById(id).get();

        // find menu from product
        Menu menu = product.getMenu();

        // find product to remove
        Product productToRemove = menu.getProducts().stream().filter(product1 -> product1.getId() == id).findFirst().get();

        // remove product
        menu.getProducts().remove(productToRemove);

        // save menu
        menuRepository.save(menu);

        //todo a retirer? on voudrai pas garder cette info pour des stats?
        //find all order items
        product.getOrderItems().forEach(orderItem -> {
            // remove orderitem from bill
            Bill bill = orderItem.getBill();
            bill.getOrderItems().remove(orderItem);
            orderItem.setBill(null);
            billRepository.save(bill);
        });

        //remove menu from product
        product.setMenu(null);

        // delete product
        productRepository.delete(product);

    }

    // todo on save deux fois le produit ce qui n'est pas optimal
    public ProductDTO removeMenuType(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId()).get();
        product.setMenuType(null);
        ProductDTO retour = dtoUtils.mapProductToProductDTO(productRepository.save(product));
        return retour;
    }


    public ProductDTO uploadFile(MultipartFile file, long productId) throws IOException {
        Product product = productRepository.findById(productId).get();

        product.setImgFile(imgFileRepository.save(ImgFileUtils.createImgFile(file, StringUtils.cleanPath(file.getOriginalFilename()), file.getContentType())));

        return dtoUtils.mapProductToProductDTO(productRepository.save(product));
    }

    public byte[] returnImgAsByteArrayString(Long id) {
        return imgFileRepository.findById(id).get().getData();
    }

}
