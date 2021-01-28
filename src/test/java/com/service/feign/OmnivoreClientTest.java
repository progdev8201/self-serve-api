package com.service.feign;

import com.model.omnivore.OmnivoreTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OmnivoreClientTest {
    @Autowired
    OmnivoreClient omnivoreClient;
    private String locationId="idpXoB7T";
    @Value("${omnivore.apiKey}")
    private String apiKey;

    @Test
    public void faireUnAppelAvecClient(){
        OmnivoreTable omnivoreTable = omnivoreClient.findByLocation(locationId,apiKey,1L);
        assertNotNull(omnivoreTable.getId());
        assertNotNull(omnivoreTable.getName());
        assertNotNull(omnivoreTable.getSeats());
        assertNotNull(omnivoreTable.getPosId());
        assertNotNull(omnivoreTable.getNumber());
    }
}