package com.backend.deals.Controller;

import com.backend.deals.Model.DealItem;
import com.backend.deals.Service.DealsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deals")
public class DealsController {

    private final DealsService dealsService;

    public DealsController(DealsService dealsService) {
        this.dealsService = dealsService;
    }

    @GetMapping("/combined/{categoryName}")
    public ResponseEntity<List<DealItem>> getCombinedDeals(@PathVariable String categoryName) {
        List<DealItem> combinedDeals = dealsService.fetchAllCombinedDeals(categoryName);
        return ResponseEntity.ok(combinedDeals);
    }
}
