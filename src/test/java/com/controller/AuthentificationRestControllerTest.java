package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
public class AuthentificationRestControllerTest {

    @Autowired
    private AuthentificationRestController authentificationRestController;

    @Test
    public void authenticateUserTest_ValidRequest() throws Exception {
        // Arrange

        MockMvc mvc = initMockMvcAuthenticationController();
        ObjectMapper objectMapper = new ObjectMapper();

        LoginForm loginForm = new LoginForm("owner@mail.com", "123456");

        // Act

        MvcResult result = mvc.perform(post("/auth/signin")
                .content(objectMapper.writeValueAsString(loginForm))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JwtResponse jwtResponse = objectMapper.readValue(result.getResponse().getContentAsString(), JwtResponse.class);

        // Assert

        assertNotNull(jwtResponse);
    }

    @Test
    public void authenticateUserTest_BadRequest() throws Exception {
        // Arrange

        MockMvc mvc = initMockMvcAuthenticationController();
        ObjectMapper objectMapper = new ObjectMapper();

        LoginForm loginForm = new LoginForm("fail@mail.com", "123456");

        // Act && Assert

        mvc.perform(post("/auth/signin")
                .content(objectMapper.writeValueAsString(loginForm))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void registerUserTest_ValidRequest() throws Exception {
        // Arrange

        MockMvc mvc = initMockMvcAuthenticationController();
        ObjectMapper objectMapper = new ObjectMapper();

        SignUpForm signUpForm = new SignUpForm("bestUser@mail.com", "test", "5147889578", "client");

        // Act && Assert

        mvc.perform(post("/auth/signup")
                .content(objectMapper.writeValueAsString(signUpForm))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void registerUserTestEmailAlreadyUsed_BadRequest() throws Exception {
        // Arrange

        MockMvc mvc = initMockMvcAuthenticationController();
        ObjectMapper objectMapper = new ObjectMapper();

        SignUpForm signUpForm = new SignUpForm("owner@mail.com", "test", "5147889578", "client");

        // Act && Assert

        mvc.perform(post("/auth/signup")
                .content(objectMapper.writeValueAsString(signUpForm))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void registerUserTestUndefinedRole_BadRequest() throws Exception {
        // Arrange

        MockMvc mvc = initMockMvcAuthenticationController();
        ObjectMapper objectMapper = new ObjectMapper();

        SignUpForm signUpForm = new SignUpForm("test@mail.com", "test", "5147889578", "massou");

        // Act && Assert

        mvc.perform(post("/auth/signup")
                .content(objectMapper.writeValueAsString(signUpForm))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void fetchOwner_ValidRequest() throws Exception {
        // Arrange

        MockMvc mvc = initMockMvcAuthenticationController();
        ObjectMapper objectMapper = new ObjectMapper();

        OwnerDTO ownerDTO = new OwnerDTO(5l, "owner@mail.com", null, new ArrayList<>(), new TreeSet<RoleDTO>(), null, null, null, null);

        // Act

        MvcResult result = mvc.perform(post("/auth/fetchOwner")
                .content(objectMapper.writeValueAsString(ownerDTO))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        OwnerDTO response = objectMapper.readValue(result.getResponse().getContentAsString(), OwnerDTO.class);

        // Assert

        assertEquals(ownerDTO.getRestaurants(), response.getRestaurants());
        assertEquals(ownerDTO.getStripeAccountId(), response.getStripeAccountId());
        assertEquals(ownerDTO.getStripeCustomerId(), response.getStripeCustomerId());
        assertEquals(ownerDTO.getUsername(), ownerDTO.getUsername());
        assertEquals(ownerDTO.getPassword(), ownerDTO.getPassword());
    }

    private MockMvc initMockMvcAuthenticationController() {
        return standaloneSetup(authentificationRestController).build();
    }
}
