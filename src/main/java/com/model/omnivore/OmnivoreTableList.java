package com.model.omnivore;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.BillDTO;

import java.util.List;
import java.util.Map;

public class OmnivoreTableList {
    @JsonAlias("_embedded")
    private TableEmbeddedList tableEmbeddedList;

    private List<OmnivoreTable> omnivoreTableList;

    private Long count;
    private Long limit;

    public List<OmnivoreTable> getOmnivoreTableList() {
        return omnivoreTableList;
    }

    public void setOmnivoreTableList(List<OmnivoreTable> omnivoreTableList) {
        this.omnivoreTableList = omnivoreTableList;
    }

    public TableEmbeddedList getTableEmbeddedList() {
        return tableEmbeddedList;
    }

    public void setTableEmbeddedList(TableEmbeddedList tableEmbeddedList) {
        this.tableEmbeddedList = tableEmbeddedList;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    @JsonProperty("_embedded")
    private void unpackNested(Map<String,Object> embeddedValues) throws JsonProcessingException {
        this.setOmnivoreTableList( new ObjectMapper().convertValue(embeddedValues.get("tables"), new TypeReference<List<OmnivoreTable>>() {}));
    }
}
