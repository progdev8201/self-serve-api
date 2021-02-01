package com.service.feign;

import com.model.omnivore.OmnivoreItem;
import com.model.omnivore.OmnivoreItemList;
import com.model.omnivore.OmnivoreMenu;
import com.model.omnivore.OmnivoreMenuList;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface OmnivoreItemClient {

    @RequestLine("GET /{location}/mms/menus/{menuId}/sections/{sectionId}/items/{itemId}")
    @Headers("Api-Key:{apikey}")
    OmnivoreItem findItemByIdFromMenuSection(@Param("location") String locationId, @Param("apikey") String apiKey, @Param("menuId") String menuId, @Param("sectionId") String sectionId, @Param("itemId") String itemId);

    @RequestLine("GET /{location}/mms/menus/{menuId}/sections/{sectionId}/items")
    @Headers("Api-Key:{apikey}")
    OmnivoreItemList findAllItemsFromMenuSection(@Param("location") String locationId, @Param("apikey") String apiKey, @Param("menuId") String menuId, @Param("sectionId") String sectionId);

}
