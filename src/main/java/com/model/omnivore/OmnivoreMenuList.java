package com.model.omnivore;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class OmnivoreMenuList {
    private List<OmnivoreMenu> omnivoreMenuList;

    private long count;
    private long limit;

    public List<OmnivoreMenu> getOmnivoreMenuList() {
        return omnivoreMenuList;
    }

    public void setOmnivoreMenuList(List<OmnivoreMenu> omnivoreMenuList) {
        this.omnivoreMenuList = omnivoreMenuList;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    @JsonProperty("_embedded")
    private void unpackNested(Map<String, Object> embeddedValues) throws JsonProcessingException {
        this.setOmnivoreMenuList(new ObjectMapper().convertValue(embeddedValues.get("menus"), new TypeReference<List<OmnivoreMenu>>() {
        }));
    }
}
