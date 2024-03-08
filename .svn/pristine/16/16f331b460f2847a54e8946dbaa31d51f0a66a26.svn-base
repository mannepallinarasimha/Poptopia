package com.kelloggs.promotions.lib.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kelloggs.promotions.lib.entity.Item;
import com.kelloggs.promotions.lib.entity.ReceiptHeader;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDetails {

    @JsonProperty(value = "ReceiptHeader")
    private ReceiptHeader receiptHeader;

    @JsonProperty(value = "Items")
    private List<Item> items;

    @JsonProperty(value = "ItemCount")
    private Integer itemCount;

    public ReceiptHeader getReceiptHeader() {
        return receiptHeader;
    }

    public void setReceiptHeader(ReceiptHeader receiptHeader) {
        this.receiptHeader = receiptHeader;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    @Override
    public String toString() {
        return "ItemDetails{" +
                "header=" + receiptHeader +
                ", items=" + items +
                ", itemCount=" + itemCount +
                '}';
    }
}
