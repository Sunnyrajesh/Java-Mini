package com.backend1.Ebay.Controller;

import com.backend1.Ebay.Model.DealItem;

import com.backend1.Ebay.Service.DealStorageService;
import com.backend1.Ebay.*;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backendserver2/ebay")
public class EbayController {


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

