package com.backend.deals.Model;

public class Price {
    private String value; // The price value
    private String currency; // The currency type (e.g., USD)

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
