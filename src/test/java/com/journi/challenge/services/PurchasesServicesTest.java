package com.journi.challenge.services;

import com.journi.challenge.models.PurchaseRequest;
import com.journi.challenge.models.PurchaseStats;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class PurchasesServicesTest {

    @Autowired
    private PurchasesService purchasesService;

    @Test
    public void testPurchaseStatistics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDate = now.minusDays(20);
        String customerName = "Test Bloke";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE.withZone(ZoneId.of("UTC"));

        // Inside window purchases
        purchasesService.save(new PurchaseRequest("1", customerName, firstDate.toString(), Collections.emptyList(), 10.0, "EUR"));
        purchasesService.save(new PurchaseRequest("1", customerName, firstDate.plusDays(1).toString(), Collections.emptyList(), 10.0, "EUR"));
        purchasesService.save(new PurchaseRequest("1", customerName, firstDate.plusDays(2).toString(), Collections.emptyList(), 10.0, "EUR"));
        purchasesService.save(new PurchaseRequest("1", customerName, firstDate.plusDays(3).toString(), Collections.emptyList(), 10.0, "EUR"));
        purchasesService.save(new PurchaseRequest("1", customerName, firstDate.plusDays(4).toString(), Collections.emptyList(), 10.0, "EUR"));
        purchasesService.save(new PurchaseRequest("1", customerName, firstDate.plusDays(5).toString(), Collections.emptyList(), 10.0, "EUR"));
        purchasesService.save(new PurchaseRequest("1", customerName, firstDate.plusDays(6).toString(), Collections.emptyList(), 10.0, "EUR"));
        purchasesService.save(new PurchaseRequest("1", customerName, firstDate.plusDays(7).toString(), Collections.emptyList(), 10.0, "EUR"));
        purchasesService.save(new PurchaseRequest("1", customerName, firstDate.plusDays(8).toString(), Collections.emptyList(), 10.0, "EUR"));
        purchasesService.save(new PurchaseRequest("1", customerName, firstDate.plusDays(9).toString(), Collections.emptyList(), 10.0, "EUR"));

        // Outside window purchases
        purchasesService.save(new PurchaseRequest("1", customerName, now.minusDays(31).toString(), Collections.emptyList(), 10.0, "EUR"));
        purchasesService.save(new PurchaseRequest("1", customerName, now.minusDays(31).toString(), Collections.emptyList(), 10.0, "EUR"));
        purchasesService.save(new PurchaseRequest("1", customerName, now.minusDays(32).toString(), Collections.emptyList(), 10.0, "EUR"));
        purchasesService.save(new PurchaseRequest("1", customerName, now.minusDays(33).toString(), Collections.emptyList(), 10.0, "EUR"));
        purchasesService.save(new PurchaseRequest("1", customerName, now.minusDays(34).toString(), Collections.emptyList(), 10.0, "EUR"));
        purchasesService.save(new PurchaseRequest("1", customerName, now.minusDays(35).toString(), Collections.emptyList(), 10.0, "EUR"));

        PurchaseStats purchaseStats = purchasesService.getLast30DaysStats();
        assertEquals(formatter.format(firstDate), purchaseStats.getFrom());
        assertEquals(formatter.format(firstDate.plusDays(9)), purchaseStats.getTo());
        assertEquals(10, purchaseStats.getCountPurchases());
        assertEquals(100.0, purchaseStats.getTotalAmount());
        assertEquals(10.0, purchaseStats.getAvgAmount());
        assertEquals(10.0, purchaseStats.getMinAmount());
        assertEquals(10.0, purchaseStats.getMaxAmount());
    }
}
