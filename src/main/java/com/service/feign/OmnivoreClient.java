package com.service.feign;

import com.model.omnivore.OmnivoreTable;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface OmnivoreClient {
    @RequestLine("GET /{location}/tables/{tableId}")
    @Headers("Api-Key:{apikey}")
    OmnivoreTable findByLocation(@Param("location") String locationId, @Param("apikey") String apiKey,@Param("tableId") Long tableId);
}
