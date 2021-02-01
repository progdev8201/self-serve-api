package com.model.omnivore;

import com.fasterxml.jackson.annotation.JsonAlias;

public class OmnivoreOrderLocation {
    private String id;
    private String name;
    private String type;
    private Long created;
    @JsonAlias("auto_close")
    private boolean autoClose;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public boolean isAutoClose() {
        return autoClose;
    }

    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }
}
