package com.backend.deals.Model;

public class MarketingPrice {
    private Price originalPrice; // Original price
    private String discountPercentage; // Discount percentage
    private Price discountAmount; // Discount amount
    private String priceTreatment; // Price treatment information

    public Price getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Price originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Price getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Price discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getPriceTreatment() {
        return priceTreatment;
    }

    public void setPriceTreatment(String priceTreatment) {
        this.priceTreatment = priceTreatment;
    }
}
