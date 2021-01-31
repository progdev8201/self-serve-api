package com.service;

import com.model.entity.*;
import com.model.enums.MenuType;
import com.model.enums.ProgressStatus;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class MenuCreationService {
    @Value("${config.styles.images.path}")
    private String fileBasePath;

    @Autowired
    private ResourceLoader resourceLoader;


    public Menu createMenuRequest() throws IOException {
        Menu menuRequest = createMenu("request", MenuType.WAITERREQUEST);
        createProduct(MenuType.WAITERREQUEST, "fork.png", BigDecimal.valueOf(0),0,"FORK", menuRequest);
        createProduct(MenuType.WAITERREQUEST, "knife.png",BigDecimal.valueOf(0),0,"KNIFE", menuRequest);
        createProduct(MenuType.WAITERREQUEST, "salt.png",BigDecimal.valueOf(0),0,"SALT", menuRequest);
        createProduct(MenuType.WAITERREQUEST, "sauce.png",BigDecimal.valueOf(0),0,"SAUCE", menuRequest);
        createProduct(MenuType.WAITERREQUEST, "sugar.png",BigDecimal.valueOf(0),0,"SUGAR", menuRequest);
        createProduct(MenuType.WAITERCALL, null,BigDecimal.valueOf(0),0,"CALL WAITER", menuRequest);
        createProduct(MenuType.TERMINALREQUEST, null,BigDecimal.valueOf(0),0,"Request Terminal", menuRequest);
        return menuRequest;
    }

    public Product createProduct(MenuType menuType, String fileToCopy, BigDecimal prix, int tempsPreparation,String productName,Menu menu ) throws IOException {
        Product product = new Product();
        product.setName(productName);
        product.setPrix(prix);
        product.setTempsDePreparation(tempsPreparation);

        if(Objects.nonNull(fileToCopy)){
            ImgFile img = getImgFile(fileToCopy);
            img.setFileType("image");
            img.setProduct(product);
            product.setImgFile(img);
        }
        product.setMenuType(menuType);
        // product.setImgUrl(serverPort+absolutePath+1);
        product.setDescription("cest bon cest bon cest bon");
        product.setOrderItems(new ArrayList<>());
        product.setCheckItems(new ArrayList<>());
        product.getCheckItems().add(createCheckItem("fromage bleu", BigDecimal.valueOf(5)));
        product.getCheckItems().add(createCheckItem("Miel", BigDecimal.valueOf(10)));
        Option option = createOption("cuisson");
        option.getCheckItemList().add(createCheckItem("moyen", BigDecimal.valueOf(0)));
        option.getCheckItemList().add(createCheckItem("faible", BigDecimal.valueOf(0)));
        option.getCheckItemList().add(createCheckItem("fort", BigDecimal.valueOf(0)));
        product.setOptions(new ArrayList<>(Arrays.asList(option)));
        if (Objects.nonNull(menu)) {
            menu.getProducts().add(product);
            product.setMenu(menu);
        }
        return product;
    }

    public Menu createMenu(String nom, MenuType menuType) {
        Menu menuRequest = new Menu();
        menuRequest.setProducts(new ArrayList<>());
        menuRequest.setName(nom);
        menuRequest.setMenuType(menuType);
        return menuRequest;
    }

    public ImgFile getImgFile(String fileToCopy) throws IOException {
        String pathDansProjet = fileBasePath + fileToCopy;
        Path currentRelativePath = Paths.get("");
        String absolutePath = currentRelativePath.toAbsolutePath().toString();
        InputStream is = resourceLoader.getResource(
                "classpath:img/" + fileToCopy).getInputStream();
        ImgFile img = new ImgFile();
        img.setData(IOUtils.toByteArray(is));
        return img;
    }

    private CheckItem createCheckItem(String name, BigDecimal prix) {
        CheckItem productCheckItem = new CheckItem();
        productCheckItem.setName(name);
        productCheckItem.setPrix(prix);
        return productCheckItem;
    }


    private Option createOption(String name) {
        Option option = new Option();
        option.setName(name);
        option.setCheckItemList(new ArrayList<>());
        return option;
    }

    public OrderItem createOrderItem(ProgressStatus progressStatus, MenuType menuType, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderStatus(progressStatus);
        orderItem.setMenuType(menuType);
        orderItem.setProduct(product);
        orderItem.setTempsDePreparation(new Date(System.currentTimeMillis()));
        product.getOrderItems().add(orderItem);
        return orderItem;
    }

}
