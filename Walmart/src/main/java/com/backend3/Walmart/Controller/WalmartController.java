package com.backend3.Walmart.Controller;

import com.backend3.Walmart.Model.*;

import com.backend3.Walmart.Service.DealStorageService;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backendserver3/walmart")
public class WalmartController {

    
    @Autowired
    private DealStorageService dealStorageService;

    @GetMapping("/deals/{categoryName}")
    public ResponseEntity<List<DealItem>> getDeals(@PathVariable String categoryName) throws IOException {
        if ("Jeans".equalsIgnoreCase(categoryName)) {
            return ResponseEntity.ok(dealStorageService.getDeals());
        }
        return ResponseEntity.notFound().build();
    }
}
