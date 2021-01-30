package com.service.feign;

import com.model.omnivore.OmnivoreLocation;
import com.model.omnivore.OmnivoreTable;
import com.model.omnivore.OmnivoreTableList;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface OmnivoreClient {
    @RequestLine("GET /{location}/tables/{tableId}")
    @Headers("Api-Key:{apikey}")
    OmnivoreTable findByTableById(@Param("location") String locationId, @Param("apikey") String apiKey, @Param("tableId") Long tableId);

    @RequestLine("GET /{location}/tables")
    @Headers("Api-Key:{apikey}")
    OmnivoreTableList findAllTables(@Param("location") String locationId, @Param("apikey") String apiKey);

    @RequestLine("GET /{location}")
    @Headers("Api-Key:{apikey}")
    OmnivoreLocation findLocationById(@Param("location") String locationId, @Param("apikey") String apiKey);
}
