package com.backend1.amazon.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DealItem {
    @JsonProperty("itemid") // Maps JSON field "itemid" to this property
    private String itemId; // Java property name is camelCase
    private String productTitle;
    private String size;
    @JsonProperty("Brand") // Maps JSON field "Brand" to this property
    private String brand; // Changed to camelCase
    private Image image;
    private MarketingPrice marketingPrice;
    private Price price;
    private int stock;
    private String dealStartDate;
    private String dealEndDate;

    // Getters and setters
    @JsonProperty("itemid")
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

    @JsonProperty("Brand") // Annotation for consistency
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

    // Inner classes for nested objects
    public static class Image {
        private String imageUrl;

        public Image() {
            // Default constructor required for Jackson
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    public static class MarketingPrice {
        private OriginalPrice originalPrice;
        private String discountPercentage;
        private DiscountAmount discountAmount;
        private String priceTreatment;

        public OriginalPrice getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(OriginalPrice originalPrice) {
            this.originalPrice = originalPrice;
        }

        public String getDiscountPercentage() {
            return discountPercentage;
        }

        public void setDiscountPercentage(String discountPercentage) {
            this.discountPercentage = discountPercentage;
        }

        public DiscountAmount getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(DiscountAmount discountAmount) {
            this.discountAmount = discountAmount;
        }

        public String getPriceTreatment() {
            return priceTreatment;
        }

        public void setPriceTreatment(String priceTreatment) {
            this.priceTreatment = priceTreatment;
        }
    }

    public static class OriginalPrice {
        private String value;
        private String currency;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    public static class DiscountAmount {
        private String value;
        private String currency;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    public static class Price {
        private String value;
        private String currency;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
}
