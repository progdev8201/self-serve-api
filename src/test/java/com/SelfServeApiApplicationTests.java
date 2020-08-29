package com;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SelfServeApiApplicationTests {

    @Test
    void contextLoads() {
    }
    /*@Test
    public void testCreateBillReturnBill() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();
        JSONObject sendObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject proprioFacture1 =new JSONObject();
        proprioFacture1.put("email","proprio1@gmail.com");
        jsonArray.put(proprioFacture1);
        JSONObject proprioFacture2 =new JSONObject();
        proprioFacture2.put("email","proprio2@gmail.com");
        jsonArray.put(proprioFacture2);
        sendObj.put("montant","100");
        sendObj.put("proprioList",jsonArray.toString());
        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/creerFacture").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        JSONObject valeurRetour =new JSONObject(result.getResponse().getContentAsString());
        assertEquals(100.0,valeurRetour.get("montant"));
    }*/

   /* private MockMvc initMockMvc(){
        return MockMvcBuilders.standaloneSetup(facturationApplicationController).build();
    }*/

}
