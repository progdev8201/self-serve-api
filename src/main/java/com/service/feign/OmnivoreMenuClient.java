package com.service.feign;

import com.model.omnivore.OmnivoreMenu;
import com.model.omnivore.OmnivoreMenuList;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface OmnivoreMenuClient {

    @RequestLine("GET /{location}/mms/menus/{menuId}")
    @Headers("Api-Key:{apikey}")
    OmnivoreMenu findMenuById(@Param("location") String locationId, @Param("apikey") String apiKey, @Param("menuId") String menuId);

    @RequestLine("GET /{location}/mms/menus")
    @Headers("Api-Key:{apikey}")
    OmnivoreMenuList findAllMenus(@Param("location") String locationId, @Param("apikey") String apiKey);

}
