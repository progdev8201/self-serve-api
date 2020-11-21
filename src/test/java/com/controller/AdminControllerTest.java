package com.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.model.dto.OwnerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AdminControllerTest {
    @Autowired
    AdminController adminController;

    @Test
    public void testFetchAllOwners() throws Exception {
        MockMvc mvc = initMockMvc();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/admin/getOwners").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<OwnerDTO> ownerDTOList= mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<OwnerDTO>>(){});
        ownerDTOList.forEach(ownerDTO -> {
            assertNull(ownerDTO.getPassword());
        });
        assertEquals(1,ownerDTOList.size());
    }

    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(adminController).build();
    }
}