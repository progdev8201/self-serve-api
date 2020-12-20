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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

@Service
public class MenuCreationService {
    @Value("${config.styles.images.path}")
    private String fileBasePath;

    @Autowired
    private ResourceLoader resourceLoader;


    public Menu createMenuRequest() throws IOException {
        Menu menuRequest = createMenu("request", MenuType.WAITERREQUEST);
        createProduct(MenuType.WAITERREQUEST, "fork.png",0,0,"FORK", menuRequest);
        createProduct(MenuType.WAITERREQUEST, "knife.png",0,0,"KNIFE", menuRequest);
        createProduct(MenuType.WAITERREQUEST, "salt.png",0,0,"SALT", menuRequest);
        createProduct(MenuType.WAITERREQUEST, "sauce.png",0,0,"SAUCE", menuRequest);
        createProduct(MenuType.WAITERREQUEST, "sugar.png",0,0,"SUGAR", menuRequest);
        createProduct(MenuType.WAITERCALL, null,0,0,"CALL WAITER", menuRequest);
        createProduct(MenuType.TERMINALREQUEST, null,0,0,"Request Terminal", menuRequest);
        return menuRequest;
    }

    public Product createProduct(MenuType menuType, String fileToCopy, double prix, int tempsPreparation,String productName,Menu menu ) throws IOException {
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
        product.getCheckItems().add(createCheckItem("fromage bleu", 5));
        product.getCheckItems().add(createCheckItem("Miel", 10));
        Option option = createOption("cuisson");
        option.getCheckItemList().add(createCheckItem("moyen", 0));
        option.getCheckItemList().add(createCheckItem("faible", 0));
        option.getCheckItemList().add(createCheckItem("fort", 0));
        product.setOptions(Collections.singletonList(option));
        if (Objects.nonNull(menu)) {
            menu.getProducts().add(product);
            product.setMenu(menu);
        }
        return product;
    }

    private CheckItem createCheckItem(String name, int prix) {
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

    private ImgFile getImgFile(String fileToCopy) throws IOException {
        String pathDansProjet = fileBasePath + fileToCopy;
        Path currentRelativePath = Paths.get("");
        String absolutePath = currentRelativePath.toAbsolutePath().toString();
        InputStream is = resourceLoader.getResource(
                "classpath:img/" + fileToCopy).getInputStream();
        ImgFile img = new ImgFile();
        img.setData(IOUtils.toByteArray(is));
        return img;
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

    private Menu createMenu(String nom, MenuType menuType) {
        Menu menuRequest = new Menu();
        menuRequest.setProducts(new ArrayList<>());
        menuRequest.setName(nom);
        menuRequest.setMenuType(menuType);
        return menuRequest;
    }

}
