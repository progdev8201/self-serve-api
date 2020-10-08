package com.service;

import com.event.DataLoader;
import com.mapping.*;
import com.model.dto.CheckItemDTO;
import com.model.dto.MenuDTO;
import com.model.dto.OptionDTO;
import com.model.dto.ProductDTO;
import com.model.entity.*;
import com.model.enums.ProductMenuType;
import com.model.enums.ProductType;
import com.repository.BillRepository;
import com.repository.ImgFileRepository;
import com.repository.MenuRepository;
import com.repository.ProductRepository;
import com.service.DtoUtil.DTOUtils;
import javassist.bytecode.ByteArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ImgFileRepository imgFileRepository;

    @Autowired
    BillRepository billRepository;

    @Autowired
    DTOUtils dtoUtils;


    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Value("${config.styles.images.path}")
    private String fileBasePath;

    public Product find(Long id) {
        return productRepository.findById(id).get();
    }

    public List<Product> findAllProductFromMenu(Long id) {
        return menuRepository.findById(id).get().getProducts();
    }

    public List<ProductDTO> findAllWaiterRequestProductFromMenu(Long id) {
        List<ProductDTO> productDTOS = new ArrayList<>();
        menuRepository.findById(id).get().getProducts().stream().forEach(product -> {
            if((product.getProductType() == ProductType.WAITERREQUEST)||(product.getProductType() == ProductType.WAITERCALL)){
                productDTOS.add(dtoUtils.generateProductDTO(product));
            }
        });

        return productDTOS;
    }
    public ProductDTO create(ProductDTO productDTO, Long menuId) {
        List<Option> options = new ArrayList<>();
        List<CheckItem> checkItems = new ArrayList<>();

        // convert product dto to a product
        Product product = ProductDTOToProduct.instance.convert(productDTO);

        if (productDTO.getOptions() != null) {

            //add all product dto options to product
            productDTO.getOptions().stream().forEach(optionDTO -> {

                //empty array
                checkItems.clear();

                //map check item list dto to check item list
                optionDTO.getCheckItemList().stream().forEach(checkItemDTO -> {
                    checkItems.add(CheckItemDTOCheckItem.instance.convert(checkItemDTO));
                });

                Option option = OptionDTOToOption.instance.convert(optionDTO);
                option.setCheckItemList(checkItems);

                options.add(option);
            });

        }

        product.setOptions(options);

        product = productRepository.save(product);
        Product finalProduct = product;

        menuRepository.findById(menuId).ifPresent(menu -> {
            finalProduct.setMenu(menu);
            menu.getProducts().add(finalProduct);
            menu = menuRepository.save(menu);
        });

        //map product to

        return ProductToProductDTO.instance.convert(product);
    }

    public void update(ProductDTO productDTO) {
        List<Option> options = new ArrayList<>();
        List<CheckItem> checkItems = new ArrayList<>();

        // convert product dto to a product
        Product product = ProductDTOToProduct.instance.convert(productDTO);

        if (productDTO.getOptions() != null) {

            //add all product dto options to product
            productDTO.getOptions().stream().forEach(optionDTO -> {

                //empty array
                checkItems.clear();

                //map check item list dto to check item list
                optionDTO.getCheckItemList().stream().forEach(checkItemDTO -> {
                    checkItems.add(CheckItemDTOCheckItem.instance.convert(checkItemDTO));
                });

                Option option = OptionDTOToOption.instance.convert(optionDTO);
                option.setCheckItemList(checkItems);

                options.add(option);
            });

        }

        product.setOptions(options);

        System.out.println(productRepository.save(product));
    }

    public void delete(Long id) {
        List<Product> prod = productRepository.findAll();
        Product product = productRepository.findById(id).get();
        Menu menu = product.getMenu();
        Product productToRemove = menu.getProducts().stream().filter(product1 -> product1.getId()==id).findFirst().get();
        menu.getProducts().remove(productToRemove);
        menu= menuRepository.save(menu);
        LOGGER.info(String.valueOf(prod.size()));
        product.getOrderItems().forEach(orderItem -> {
            Bill bill =orderItem.getBill();
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
        return dtoUtils.generateProductDTO(productList);

    }

    public List<ProductDTO> findMenuDinerProduct(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> productList = menu.getProducts().stream().filter(r -> {
            if (r.getProductMenuType() == ProductMenuType.DINER) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return dtoUtils.generateProductDTO(productList);

    }

    public List<ProductDTO> findMenuDejeunerProduct(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> productList = menu.getProducts().stream().filter(r -> {
            if (r.getProductMenuType() == ProductMenuType.DEJEUNER) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return dtoUtils.generateProductDTO(productList);

    }

    public List<ProductDTO> findMenuSouper(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> productList = menu.getProducts().stream().filter(r -> {
            if (r.getProductMenuType() == ProductMenuType.SOUPER) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return dtoUtils.generateProductDTO(productList);

    }

    public List<ProductDTO> findMenuChoixDuChef(MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuDTO.getId()).get();
        List<Product> productList = menu.getProducts().stream().filter(r -> {
            if (r.getProductType() == ProductType.CHEFCHOICE) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return dtoUtils.generateProductDTO(productList);

    }

    public ProductDTO setProductSpecial(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId()).get();
        product.setProductType(ProductType.SPECIAL);
        ProductDTO retour =dtoUtils.generateProductDTO(productRepository.save(product));
        retour.setProductType(product.getProductType());
        return retour;
    }

    public ProductDTO removeProductType(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId()).get();
        product.setProductType(null);
        product = productRepository.save(product);
        ProductDTO retour =dtoUtils.generateProductDTO(productRepository.save(product));
        retour.setProductType(product.getProductType());
        return retour;
    }

    public ProductDTO setProductChefChoice(ProductDTO productDTO) {

        Product product = productRepository.findById(productDTO.getId()).get();
        product.setProductType(ProductType.CHEFCHOICE);
        ProductDTO retour =dtoUtils.generateProductDTO(productRepository.save(product));;
        retour.setProductType(product.getProductType());
        return retour;
    }

    public ProductDTO uploadFile(MultipartFile file, ProductDTO productDTO) throws IOException {
        Path currentRelativePath = Paths.get("");
        String absolutePath = currentRelativePath.toAbsolutePath().toString();
        String fileName = StringUtils.cleanPath(UUID.randomUUID().toString());
        String pathDansProjet = fileBasePath + fileName + ".jpg";
        File imgFile = new File(absolutePath + pathDansProjet);
        file.transferTo(imgFile);

        Product product = productRepository.findById(productDTO.getId()).get();
        product.setImgUrl(pathDansProjet);
        ImgFile img = new ImgFile();
        img.setFileType("image");
        img.setData(file.getBytes());
        product.setImgFile(imgFileRepository.save(img));
        product=productRepository.save(product);
        return dtoUtils.generateProductDTO(product);
    }

    public byte[] returnImgAsByteArrayString(Long id) {
        byte[] bytes =imgFileRepository.findById(id).get().getData();
        return imgFileRepository.findById(id).get().getData();
    }

    public List<ProductDTO> generateProductDTO(List<Product> products) {
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = ProductToProductDTO.instance.convert(product);
            productDTO.setImgFileDTO(ImgFileToImgFileDTO.instance.convert(product.getImgFile()));
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

    public ProductDTO generateProductDTO(Product product) {
        ProductDTO productDTO = ProductToProductDTO.instance.convert(product);
        productDTO.setImgFileDTO(ImgFileToImgFileDTO.instance.convert(product.getImgFile()));
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
        return productDTO;
    }
}
