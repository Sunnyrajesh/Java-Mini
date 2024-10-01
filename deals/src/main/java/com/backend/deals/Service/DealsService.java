package com.backend.deals.Service;

import com.backend.deals.Model.DealItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DealsService {
    private static final Logger logger = LoggerFactory.getLogger(DealsService.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final WebClient webClientAmazon;
    private final WebClient webClientWalmart;
    private final WebClient webClientEbay;

    public DealsService(WebClient.Builder webClientBuilder) {
        this.webClientAmazon = webClientBuilder.baseUrl("http://localhost:8081/backendserver1/amazon").build();
        this.webClientWalmart = webClientBuilder.baseUrl("http://localhost:8083/backendserver3/walmart").build();
        this.webClientEbay = webClientBuilder.baseUrl("http://localhost:8082/backendserver2/ebay").build();
    }

    public List<DealItem> fetchAllCombinedDeals(String categoryName) {
        CompletableFuture<List<DealItem>> amazonFuture = fetchDeals(categoryName, webClientAmazon);
        CompletableFuture<List<DealItem>> walmartFuture = fetchDeals(categoryName, webClientWalmart);
        CompletableFuture<List<DealItem>> ebayFuture = fetchDeals(categoryName, webClientEbay);

        return Stream.of(amazonFuture, walmartFuture, ebayFuture)
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .filter(this::isValidDeal)
                .filter(deal -> deal.getStock() > 0)
                .filter(this::isDealActive)
                .sorted(this::compareDeals)
                .collect(Collectors.toList());
    }

    private CompletableFuture<List<DealItem>> fetchDeals(String categoryName, WebClient webClient) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/deals/{categoryName}").build(categoryName))
                .retrieve()
                .bodyToFlux(DealItem.class)
                .collectList()
                .timeout(TIMEOUT)
                .onErrorResume(e -> {
                    logger.error("Error fetching deals: ", e);
                    return Mono.just(List.of());
                })
                .toFuture();
    }

    private boolean isValidDeal(DealItem deal) {
        if (deal.getPrice() == null || deal.getPrice().getValue() == null) {
            return false;
        }
        double price = convertPriceToDouble(deal.getPrice().getValue());
        return price > 0;
    }

    private boolean isDealActive(DealItem deal) {
        if (deal.getDealStartDate() == null || deal.getDealEndDate() == null) {
            return false;
        }
        try {
            ZonedDateTime dealStartDate = ZonedDateTime.parse(deal.getDealStartDate(), DateTimeFormatter.ISO_DATE_TIME);
            ZonedDateTime dealEndDate = ZonedDateTime.parse(deal.getDealEndDate(), DateTimeFormatter.ISO_DATE_TIME);
            ZonedDateTime now = ZonedDateTime.now();
            return !now.isBefore(dealStartDate) && !now.isAfter(dealEndDate);
        } catch (Exception e) {
            logger.error("Error parsing deal dates: ", e);
            return false;
        }
    }

    private int compareDeals(DealItem deal1, DealItem deal2) {
        double discountComparison = Double.compare(calculateDiscountPercentage(deal2), calculateDiscountPercentage(deal1));
        return discountComparison != 0 
                ? (int) discountComparison
                : Double.compare(convertPriceToDouble(deal1.getPrice().getValue()), convertPriceToDouble(deal2.getPrice().getValue()));
    }

    private double calculateDiscountPercentage(DealItem deal) {
        if (deal.getMarketingPrice() == null || deal.getMarketingPrice().getOriginalPrice() == null || deal.getPrice() == null) {
            return 0;
        }
        double originalPrice = convertPriceToDouble(deal.getMarketingPrice().getOriginalPrice().getValue());
        double currentPrice = convertPriceToDouble(deal.getPrice().getValue());
        return (originalPrice > 0) ? (originalPrice - currentPrice) / originalPrice * 100 : 0;
    }

    private double convertPriceToDouble(String priceValue) {
        try {
            return Math.max(Double.parseDouble(priceValue.replaceAll("[^\\d.-]", "")), 0);
        } catch (NumberFormatException e) {
            logger.error("Error parsing price: " + priceValue, e);
            return 0;
        }
    }
}