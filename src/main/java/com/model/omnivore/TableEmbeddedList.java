package com.model.omnivore;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class TableEmbeddedList {
    @JsonAlias("tables")
    private List<OmnivoreTable> omnivoreTableList;

    public List<OmnivoreTable> getOmnivoreTableList() {
        return omnivoreTableList;
    }

    public void setOmnivoreTableList(List<OmnivoreTable> omnivoreTableList) {
        this.omnivoreTableList = omnivoreTableList;
    }
}
