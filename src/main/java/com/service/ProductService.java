package com.service;

import com.mapping.CheckItemToCheckItemDTO;
import com.mapping.OptionToOptionDTO;
import com.mapping.ProductDTOToProduct;
import com.mapping.ProductToProductDTO;
import com.model.dto.CheckItemDTO;
import com.model.dto.MenuDTO;
import com.model.dto.OptionDTO;
import com.model.dto.ProductDTO;
import com.model.entity.CheckItem;
import com.model.entity.Menu;
import com.model.entity.Option;
import com.model.entity.Product;
import com.model.enums.ProductMenuType;
import com.model.enums.ProductType;
import com.repository.MenuRepository;
import com.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuRepository menuRepository;

    public Product find(Long id) {
        return productRepository.findById(id).get();
    }

    public List<Product> findAllProductFromMenu(Long id) {
        return menuRepository.findById(id).get().getProducts();
    }

    public void create(ProductDTO productDTO, Long menuId) {
        Product product = ProductDTOToProduct.instance.convert(productDTO);
        Menu menu = menuRepository.findById(menuId).get();
        menu.getProducts().add(product);
        menuRepository.save(menu);
    }

    public void update(ProductDTO productDTO) {
        Product product = ProductDTOToProduct.instance.convert(productDTO);
        productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductDTO> findMenuSpecials(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> productList = menu.getProducts().parallelStream().filter(r -> {
            if (r.getProductType() == ProductType.SPECIAL) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return generateProductDTO(productList);

    }

    public List<ProductDTO> findMenuDinerProduct(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> productList = menu.getProducts().parallelStream().filter(r -> {
            if (r.getProductMenuType() == ProductMenuType.DINER) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return generateProductDTO(productList);

    }

    public List<ProductDTO> findMenuDejeunerProduct(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> productList = menu.getProducts().stream().filter(r -> {
            if (r.getProductMenuType() == ProductMenuType.DEJEUNER) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return generateProductDTO(productList);

    }

    public List<ProductDTO> findMenuSouper(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> productList = menu.getProducts().parallelStream().filter(r -> {
            if (r.getProductMenuType() == ProductMenuType.SOUPER) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return generateProductDTO(productList);

    }

    public List<ProductDTO> findMenuChoixDuChef(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> productList = menu.getProducts().parallelStream().filter(r -> {
            if (r.getProductType() == ProductType.CHEFCHOICE) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return generateProductDTO(productList);

    }

    public ProductDTO setProductSpecial(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId()).get();
        product.setProductType(ProductType.SPECIAL);
        ProductDTO retour = ProductToProductDTO.instance.convert(productRepository.save(product));
        retour.setProductType(product.getProductType());
        return retour;
    }

    public ProductDTO removeProductType(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId()).get();
        product.setProductType(null);
        product = productRepository.save(product);
        ProductDTO retour = ProductToProductDTO.instance.convert(product);
        retour.setProductType(product.getProductType());
        return retour;
    }

    public ProductDTO setProductChefChoice(ProductDTO productDTO) {

        Product product = productRepository.findById(productDTO.getId()).get();
        product.setProductType(ProductType.CHEFCHOICE);
        ProductDTO retour = ProductToProductDTO.instance.convert(productRepository.save(product));
        retour.setProductType(product.getProductType());
        return retour;
    }

    public List<ProductDTO> generateProductDTO(List<Product> products) {
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = ProductToProductDTO.instance.convert(product);
            productDTO.setProductType(product.getProductType());
            productDTO.setOptions(new ArrayList<>());
            for (Option option : product.getOptions()) {
                OptionDTO optionDTO = OptionToOptionDTO.instance.convert(option);
                optionDTO.setCheckItemList(new ArrayList<>());
                for (CheckItem checkItem : option.getCheckItemList()) {
                    CheckItemDTO checkItemDTO = CheckItemToCheckItemDTO.instance.convert(checkItem);
                    optionDTO.getCheckItemList().add(checkItemDTO);
                }
                productDTO.getOptions().add(optionDTO);
            }
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }
}
