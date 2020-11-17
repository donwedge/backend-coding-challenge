package com.journi.challenge.integration;

import com.journi.challenge.models.Purchase;
import com.journi.challenge.services.PurchasesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class PurchaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchasesService purchasesService;

    @Value("${server.port}")
    private String serverPort;

    private String getFullUrl() {
        return "http://localhost:" + this.serverPort;
    }

    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    private String getPurchaseJson(String invoiceNumber, String customerName, String dateTime, Double totalValue, String currencyCode, String... productIds) {
        String productIdList = "[\"" + String.join("\",\"", productIds) + "\"]";
        return String.format(Locale.US, "{\"invoiceNumber\":\"%s\",\"customerName\":\"%s\",\"dateTime\":\"%s\",\"productIds\":%s,\"amount\":%.2f,\"currencyCode\":\"%s\"}", invoiceNumber, customerName, dateTime, productIdList, totalValue, currencyCode);
    }

    @Test
    public void testPurchaseCurrencyCodeEUR() throws Exception {
        String body = getPurchaseJson("1", "customer 1", "2020-01-01T10:00:00+01:00", 25.34, "EUR", "product1");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(body, headers);
        testRestTemplate.postForEntity(getFullUrl() + "/purchases", request, Purchase.class);

        Purchase savedPurchase = purchasesService.list().get(purchasesService.list().size() - 1);
        assertEquals("customer 1", savedPurchase.getCustomerName());
        assertEquals("1", savedPurchase.getInvoiceNumber());
        assertEquals("2020-01-01T10:00:00", savedPurchase.getTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME));
        assertEquals(25.34, savedPurchase.getTotalValue());
    }

    @Test
    public void testPurchaseCurrencyCodeZAR() throws Exception {
        String body = getPurchaseJson("1", "customer 1", "2020-01-01T10:00:00+01:00", 25.34, "ZAR", "product1");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(body, headers);
        testRestTemplate.postForEntity(getFullUrl() + "/purchases", request, Purchase.class);

        Purchase savedPurchase = purchasesService.list().get(purchasesService.list().size() - 1);
        assertEquals("customer 1", savedPurchase.getCustomerName());
        assertEquals("1", savedPurchase.getInvoiceNumber());
        assertEquals("2020-01-01T10:00:00", savedPurchase.getTimestamp().toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME));
        assertEquals(437.738364, savedPurchase.getTotalValue());
        assertEquals("ZAR", savedPurchase.getCurrencyCode());
    }
}
