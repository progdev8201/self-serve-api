package com.model.omnivore;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class OmnivoreItemList {
    private List<OmnivoreItem> omnivoreItems;

    private long count;
    private long limit;

    public List<OmnivoreItem> getOmnivoreItems() {
        return omnivoreItems;
    }

    public void setOmnivoreItems(List<OmnivoreItem> omnivoreItems) {
        this.omnivoreItems = omnivoreItems;
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
        this.setOmnivoreItems(new ObjectMapper().convertValue(embeddedValues.get("items"), new TypeReference<List<OmnivoreItem>>() {
        }));
    }
}
