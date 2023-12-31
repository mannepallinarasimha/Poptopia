package com.kelloggs.promotions.lib.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvalidDetails {

    @JsonProperty(value = "InvalidShortText")
    private String invalidText;

    @JsonProperty(value = "InvalidReason")
    private String invalidReason;

    public String getInvalidText() {
        return invalidText;
    }

    public void setInvalidText(String invalidText) {
        this.invalidText = invalidText;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }

    @Override
    public String toString() {
        return "InvalidDetails{" +
                "invalidText='" + invalidText + '\'' +
                ", invalidReason='" + invalidReason + '\'' +
                '}';
    }
}
