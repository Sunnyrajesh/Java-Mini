package com.backend1.Ebay.Service;



import com.backend1.Ebay.Model.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class DealStorageService {

    private static final String DEALS_FILE_PATH = "src/main/resources/data/jeans-deals.json";
    
    public List<DealItem> getDeals() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DealResponse response = objectMapper.readValue(new File(DEALS_FILE_PATH), DealResponse.class);
        return response.getDealItems();
    }

    private static class DealResponse {
        private String categoryName;
        private List<DealItem> dealItems;

        // Getters and setters
        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public List<DealItem> getDealItems() {
            return dealItems;
        }

        public void setDealItems(List<DealItem> dealItems) {
            this.dealItems = dealItems;
        }
    }
}
