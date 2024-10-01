package com.backend1.amazon.Controller;



import com.backend1.amazon.Model.DealItem;
import com.backend1.amazon.Service.DealStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/backendserver1/amazon")
public class AmazonController {

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
