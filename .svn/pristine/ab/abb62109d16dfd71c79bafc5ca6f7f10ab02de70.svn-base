package com.kelloggs.promotions.lib.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class SnippInfo {


    @NotBlank(message = "ClientEventID cannot be null or empty")
    @JsonProperty(value = "ClientEventID")
    private String clientEventId;

    @NotBlank(message = "SnippEventID cannot be null or empty")
    @JsonProperty(value = "SnippEventID")
    private String snippEventId;

    public SnippInfo() {
        //default Constructor
    }

    public String getClientEventId() {
        return clientEventId;
    }

    public void setClientEventId(String clientEventId) {
        this.clientEventId = clientEventId;
    }

    public String getSnippEventId() {
        return snippEventId;
    }

    public void setSnippEventId(String snippEventId) {
        this.snippEventId = snippEventId;
    }
}
