package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.BillDTO;
import com.model.dto.MenuDTO;
import com.model.dto.RateDTO;
import com.model.entity.Product;
import com.model.entity.Rate;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// TODO: all test should include assert arrange act as comments so its easier to understand code
@SpringBootTest

class RateControllerTest {

    @Autowired
    RateController rateController;
    @Test
    public void testCreateRateResponseNonNull() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        Rate rate = new Rate();
        rate.setRate(5);
        rate.setRaterUsername("killua");
        Product product = new Product();
        product.setName("le steak chico");
        product.setPrix(BigDecimal.valueOf(29.99));
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("rate",objectMapper.writeValueAsString(rate));
        sendObj.put("productDTOID","1");

        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/rate/createRate").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        RateDTO reponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), RateDTO.class);
        assertNotNull(reponse);
        assertEquals(rate.getRate(),reponse.getRate());
        assertEquals(rate.getRaterUsername(),reponse.getRaterUsername());
    }

    private MockMvc initMockMvc(){
        return MockMvcBuilders.standaloneSetup(rateController).build();
    }

}