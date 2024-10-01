package com.backend.deals;

import com.backend.deals.Model.DealItem;
import com.backend.deals.Model.MarketingPrice;
import com.backend.deals.Model.Price;
import com.backend.deals.Service.DealsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DealsServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClientAmazon;
    @Mock
    private WebClient webClientWalmart;
    @Mock
    private WebClient webClientEbay;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    private DealsService dealsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClientAmazon, webClientWalmart, webClientEbay);
        dealsService = new DealsService(webClientBuilder);
    }

    @Test
    void testFetchAllCombinedDeals() {
        // Arrange
        String categoryName = "Jeans";
        List<DealItem> amazonDeals = createMockDeals("Amazon", 2);
        List<DealItem> walmartDeals = createMockDeals("Walmart", 2);
        List<DealItem> ebayDeals = createMockDeals("eBay", 2);

        mockWebClientResponse(webClientAmazon, amazonDeals);
        mockWebClientResponse(webClientWalmart, walmartDeals);
        mockWebClientResponse(webClientEbay, ebayDeals);

        // Act
        List<DealItem> result = dealsService.fetchAllCombinedDeals(categoryName);

        // Assert
        assertNotNull(result);
        assertEquals(6, result.size());
        assertTrue(result.stream().allMatch(deal -> deal.getStock() > 0));
        assertTrue(result.stream().allMatch(this::isValidDeal));
    }
    private void mockWebClientResponse(WebClient webClient, List<DealItem> deals) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(DealItem.class)).thenReturn(Flux.fromIterable(deals));
    }

    private List<DealItem> createMockDeals(String source, int count) {
        return List.of(
            createDealItem(source + "1", 100.0, 80.0, 5),
            createDealItem(source + "2", 200.0, 150.0, 3)
        );
    }

    private List<DealItem> createInvalidMockDeals(int count) {
        DealItem invalidDeal1 = createDealItem("Invalid1", 0.0, 0.0, 0);
        DealItem invalidDeal2 = createDealItem("Invalid2", -10.0, -5.0, -1);
        invalidDeal2.setPrice(null);  // Set price to null to make it invalid
        return List.of(invalidDeal1, invalidDeal2);
    }

    private List<DealItem> createExpiredMockDeals(int count) {
        DealItem expiredDeal1 = createDealItem("Expired1", 100.0, 80.0, 5);
        DealItem expiredDeal2 = createDealItem("Expired2", 200.0, 150.0, 3);

        ZonedDateTime now = ZonedDateTime.now();
        expiredDeal1.setDealEndDate(now.minusDays(1).format(DateTimeFormatter.ISO_DATE_TIME));
        expiredDeal2.setDealEndDate(now.minusDays(2).format(DateTimeFormatter.ISO_DATE_TIME));

        return List.of(expiredDeal1, expiredDeal2);
    }

    private DealItem createDealItem(String id, double originalPrice, double currentPrice, int stock) {
        DealItem deal = new DealItem();
        deal.setItemId(id);
        deal.setProductTitle("Product " + id);
        deal.setStock(stock);

        Price price = new Price();
        price.setValue(String.valueOf(currentPrice));
        price.setCurrency("USD");
        deal.setPrice(price);

        MarketingPrice marketingPrice = new MarketingPrice();
        Price originalPriceObj = new Price();
        originalPriceObj.setValue(String.valueOf(originalPrice));
        originalPriceObj.setCurrency("USD");
        marketingPrice.setOriginalPrice(originalPriceObj);
        deal.setMarketingPrice(marketingPrice);

        ZonedDateTime now = ZonedDateTime.now();
        deal.setDealStartDate(now.minusDays(1).format(DateTimeFormatter.ISO_DATE_TIME));
        deal.setDealEndDate(now.plusDays(1).format(DateTimeFormatter.ISO_DATE_TIME));

        return deal;
    }

    private boolean isValidDeal(DealItem deal) {
        return deal.getPrice() != null && deal.getPrice().getValue() != null && Double.parseDouble(deal.getPrice().getValue()) > 0;
    }

    private boolean isDealActive(DealItem deal) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime startDate = ZonedDateTime.parse(deal.getDealStartDate(), DateTimeFormatter.ISO_DATE_TIME);
        ZonedDateTime endDate = ZonedDateTime.parse(deal.getDealEndDate(), DateTimeFormatter.ISO_DATE_TIME);
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }
    @Test
    void testIsDealActive() {
        // Arrange
        DealItem activeDeal = createDealItem("Active1", 100.0, 80.0, 5);
        DealItem expiredDeal = createDealItem("Expired1", 100.0, 80.0, 5);
        ZonedDateTime now = ZonedDateTime.now();
        expiredDeal.setDealEndDate(now.minusDays(1).format(DateTimeFormatter.ISO_DATE_TIME));  // Expired deal

        // Act
        boolean isActiveDealActive = isDealActive(activeDeal);
        boolean isExpiredDealActive = isDealActive(expiredDeal);

        // Assert
        assertTrue(isActiveDealActive, "The active deal should be active.");
        assertFalse(isExpiredDealActive, "The expired deal should not be active.");
    }
    @Test
    void testCreateDealItem() {
        // Arrange
        String expectedId = "Deal123";
        double expectedOriginalPrice = 200.0;
        double expectedCurrentPrice = 150.0;
        int expectedStock = 10;

        // Act
        DealItem dealItem = createDealItem(expectedId, expectedOriginalPrice, expectedCurrentPrice, expectedStock);

        // Assert
        assertNotNull(dealItem);
        assertEquals(expectedId, dealItem.getItemId(), "Deal ID should match");
        assertEquals("Product " + expectedId, dealItem.getProductTitle(), "Product title should match the ID");
        assertEquals(expectedStock, dealItem.getStock(), "Stock should match");

        assertNotNull(dealItem.getPrice(), "Price object should not be null");
        assertEquals(String.valueOf(expectedCurrentPrice), dealItem.getPrice().getValue(), "Current price should match");

        assertNotNull(dealItem.getMarketingPrice(), "MarketingPrice object should not be null");
        assertNotNull(dealItem.getMarketingPrice().getOriginalPrice(), "Original price object should not be null");
        assertEquals(String.valueOf(expectedOriginalPrice), dealItem.getMarketingPrice().getOriginalPrice().getValue(), "Original price should match");

        assertNotNull(dealItem.getDealStartDate(), "Deal start date should not be null");
        assertNotNull(dealItem.getDealEndDate(), "Deal end date should not be null");

        // Check the start and end date validity
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime startDate = ZonedDateTime.parse(dealItem.getDealStartDate(), DateTimeFormatter.ISO_DATE_TIME);
        ZonedDateTime endDate = ZonedDateTime.parse(dealItem.getDealEndDate(), DateTimeFormatter.ISO_DATE_TIME);

        assertTrue(startDate.isBefore(now), "Start date should be in the past");
        assertTrue(endDate.isAfter(now), "End date should be in the future");
    }

    @Test
    void testFetchAllCombinedDealsAPICheck() {
        // Arrange
        String categoryName = "Jeans";
        
        // Mock the WebClient responses for each API with empty or mock data to simulate a working API call
        mockWebClientResponse(webClientAmazon, createMockDeals("Amazon", 1));
        mockWebClientResponse(webClientWalmart, createMockDeals("Walmart", 1));
        mockWebClientResponse(webClientEbay, createMockDeals("eBay", 1));

        // Act
        List<DealItem> result = dealsService.fetchAllCombinedDeals(categoryName);

        // Assert
        assertNotNull(result, "API response should not be null");
        assertFalse(result.isEmpty(), "API response should not be empty");
        
        // Optionally log for debugging purposes
        System.out.println("Deals fetched from all APIs: " + result);
    }


    
}