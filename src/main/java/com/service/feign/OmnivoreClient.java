package com.service.feign;

import com.model.omnivore.OmnivoreItem;
import com.model.omnivore.OmnivoreTable;
import com.model.omnivore.OmnivoreTableList;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface OmnivoreClient {
    @RequestLine("GET /{location}/tables/{tableId}")
    @Headers("Api-Key:{apikey}")
    OmnivoreTable findByTableById(@Param("location") String locationId, @Param("apikey") String apiKey, @Param("tableId") Long tableId);

    @RequestLine("GET /{location}/tables")
    @Headers("Api-Key:{apikey}")
    OmnivoreTableList findAllTables(@Param("location") String locationId, @Param("apikey") String apiKey);
}
