package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.MenuDTO;
import com.model.dto.ProductDTO;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
public class MenuControllerTest {
    @Autowired
    MenuController menuController;

    @Test
    public void testAddProductSpecials() throws Exception {
        MockMvc mvc = initMockMvcMenuController();
        MenuDTO menuDTO = createMenuDTO();
        List<ProductDTO> productDTOS=createProductDTOSList();
        ObjectMapper mapper = new ObjectMapper();
        JSONObject sendObj = new JSONObject();
        sendObj.put("menuDTO",mapper.writeValueAsString(menuDTO));
        sendObj.put("productDTOList",mapper.writeValueAsString(productDTOS));

        mvc.perform(MockMvcRequestBuilders.post("/menu/changeFeatured").
                    contentType(MediaType.APPLICATION_JSON).
                    accept(MediaType.APPLICATION_JSON).
                    content(sendObj.toString())).
                    andExpect(status().isOk()).
                    andReturn();
    }
    private MenuDTO createMenuDTO(){
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(1L);
        return menuDTO;
    }
    private List<ProductDTO> createProductDTOSList(){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(2L);
        List<ProductDTO> productDTOS = new ArrayList<>();
        productDTOS.add(productDTO);
        productDTOS.add(productDTO2);
        return productDTOS;
    }

    private MockMvc initMockMvcMenuController() {
        return MockMvcBuilders.standaloneSetup(menuController).build();
    }

}
