package com.service.feign;

import com.model.omnivore.OmnivoreLocation;
import com.model.omnivore.OmnivoreTable;
import com.model.omnivore.OmnivoreTableList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class OmnivoreClientTest {
    @Autowired
    private OmnivoreClient omnivoreClient;
    private String locationId="idpXoB7T";
    @Value("${omnivore.apiKey}")
    private String apiKey;

    @Test
    public void fetchTableById(){
        OmnivoreTable omnivoreTable = omnivoreClient.findByTableById(locationId,apiKey,1L);
        assertNotNull(omnivoreTable.getId());
        assertNotNull(omnivoreTable.getName());
        assertNotNull(omnivoreTable.getSeats());
        assertNotNull(omnivoreTable.getPosId());
        assertNotNull(omnivoreTable.getNumber());
    }

    @Test
    public void fetchAllTables(){
        OmnivoreTableList omnivoreTableList = omnivoreClient.findAllTables(locationId,apiKey);
        assertNotNull(omnivoreTableList);
        assertNotNull(omnivoreTableList.getOmnivoreTableList());
    }
    @Test
    public void fetchLocation(){
        OmnivoreLocation omnivoreLocation = omnivoreClient.findLocationById(locationId,apiKey);
        assertNotNull(omnivoreLocation.getId());
        assertNotNull(omnivoreLocation.getName());
        assertNotNull(omnivoreLocation.getOwner());
    }
}