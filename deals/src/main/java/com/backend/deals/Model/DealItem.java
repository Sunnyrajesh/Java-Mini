package com.backend.deals.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DealItem {
    @JsonProperty("itemid")
    private String itemId;
    private String productTitle;
    private String size;
    @JsonProperty("Brand")
    private String brand;
    private Image image;
    private MarketingPrice marketingPrice;
    private Price price;
    private int stock;
    private String dealStartDate;
    private String dealEndDate;

    // Getters and Setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public MarketingPrice getMarketingPrice() {
        return marketingPrice;
    }

    public void setMarketingPrice(MarketingPrice marketingPrice) {
        this.marketingPrice = marketingPrice;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDealStartDate() {
        return dealStartDate;
    }

    public void setDealStartDate(String dealStartDate) {
        this.dealStartDate = dealStartDate;
    }

    public String getDealEndDate() {
        return dealEndDate;
    }

    public void setDealEndDate(String dealEndDate) {
        this.dealEndDate = dealEndDate;
    }
   
}
