package com.service;

import com.mapping.*;
import com.model.dto.MenuDTO;
import com.model.dto.ProductDTO;
import com.model.entity.*;
import com.model.enums.ProductMenuType;
import com.model.enums.ProductType;
import com.repository.BillRepository;
import com.repository.ImgFileRepository;
import com.repository.MenuRepository;
import com.repository.ProductRepository;
import com.service.DtoUtil.DTOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    private DTOUtils dtoUtils;


    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Value("${config.styles.images.path}")
    private String fileBasePath;


    //todo this method needs to go
    public Product find(Long id) {
        return productRepository.findById(id).get();
    }

    public List<Product> findAllProductFromMenu(Long id) {
        return menuRepository.findById(id).get().getProducts();
    }

    public List<ProductDTO> findAllWaiterRequestProductFromMenu(Long id) {
        List<ProductDTO> productDTOS = new ArrayList<>();
        menuRepository.findById(id).get().getProducts().stream().forEach(product -> {
            if ((product.getProductType() == ProductType.WAITERREQUEST) || (product.getProductType() == ProductType.WAITERCALL)) {
                productDTOS.add(dtoUtils.mapProductToProductDTO(product));
            }
        });

        return productDTOS;
    }

    //todo fix create and update method way too much repetition and code to interact with a simple entity  (clean code)

    public ProductDTO create(ProductDTO productDTO, Long menuId) {

        // convert product dto to a product
        Product product = DTOUtils.mapProductDTOToProduct(productDTO,imgFileRepository);

        Product finalProduct = productRepository.save(product);

        menuRepository.findById(menuId).ifPresent(menu -> {
            finalProduct.setMenu(menu);
            menu.getProducts().add(finalProduct);
            menu = menuRepository.save(menu);
        });

        return DTOUtils.mapProductToProductDTO(product);
    }

    public void update(ProductDTO productDTO) {
        productRepository.save(DTOUtils.mapProductDTOToProduct(productDTO,imgFileRepository));
    }

    public void delete(Long id) {
        List<Product> prod = productRepository.findAll();
        Product product = productRepository.findById(id).get();
        Menu menu = product.getMenu();
        Product productToRemove = menu.getProducts().stream().filter(product1 -> product1.getId() == id).findFirst().get();
        menu.getProducts().remove(productToRemove);
        menu = menuRepository.save(menu);
        LOGGER.info(String.valueOf(prod.size()));
        product.getOrderItems().forEach(orderItem -> {
            Bill bill = orderItem.getBill();
            bill.getOrderItems().remove(orderItem);
            orderItem.setBill(null);
            billRepository.save(bill);
        });
        product.setMenu(null);
        productRepository.delete(product);
        prod = productRepository.findAll();
        LOGGER.info(String.valueOf(prod.size()));

    }

    public List<ProductDTO> findMenuSpecials(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> productList = menu.getProducts().stream().filter(r -> {
            if (r.getProductType() == ProductType.SPECIAL) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return dtoUtils.mapProductListToProductDTOList(productList);

    }

    public List<ProductDTO> findMenuDinerProduct(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> productList = menu.getProducts().stream().filter(r -> {
            if (r.getProductMenuType() == ProductMenuType.DINER) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return dtoUtils.mapProductListToProductDTOList(productList);

    }

    public List<ProductDTO> findMenuDejeunerProduct(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> productList = menu.getProducts().stream().filter(r -> {
            if (r.getProductMenuType() == ProductMenuType.DEJEUNER) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return dtoUtils.mapProductListToProductDTOList(productList);

    }

    public List<ProductDTO> findMenuSouper(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> productList = menu.getProducts().stream().filter(r -> {
            if (r.getProductMenuType() == ProductMenuType.SOUPER) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return dtoUtils.mapProductListToProductDTOList(productList);

    }

    public List<ProductDTO> findMenuChoixDuChef(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> productList = menu.getProducts().stream().filter(r -> {
            if (r.getProductType() == ProductType.CHEFCHOICE) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return dtoUtils.mapProductListToProductDTOList(productList);

    }

    // todo ici dans la methode product dto on set deja le product type alors pk le reset
    public ProductDTO setProductSpecial(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId()).get();
        product.setProductType(ProductType.SPECIAL);
        ProductDTO retour = dtoUtils.mapProductToProductDTO(productRepository.save(product));
        retour.setProductType(product.getProductType());
        return retour;
    }

    // todo mm chose qu'au dessus on set le product type deux
    // todo on save deux fois le produit ce qui n'est pas optimal
    public ProductDTO removeProductType(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId()).get();
        product.setProductType(null);
        product = productRepository.save(product);
        ProductDTO retour = dtoUtils.mapProductToProductDTO(productRepository.save(product));
        retour.setProductType(product.getProductType());
        return retour;
    }

    public ProductDTO setProductChefChoice(ProductDTO productDTO) {

        Product product = productRepository.findById(productDTO.getId()).get();
        product.setProductType(ProductType.CHEFCHOICE);
        ProductDTO retour = dtoUtils.mapProductToProductDTO(productRepository.save(product));

        retour.setProductType(product.getProductType());
        return retour;
    }

    public ProductDTO uploadFile(MultipartFile file, long productId) throws IOException {
        Product product = productRepository.findById(productId).get();

        ImgFile img = new ImgFile();
        img.setFileType(file.getContentType());
        img.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
        img.setData(file.getBytes());

        product.setImgFile(imgFileRepository.save(img));
        product = productRepository.save(product);

        return dtoUtils.mapProductToProductDTO(product);
    }

    public byte[] returnImgAsByteArrayString(Long id) {
        return imgFileRepository.findById(id).get().getData();
    }

}
