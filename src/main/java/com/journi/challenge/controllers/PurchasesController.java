package com.journi.challenge.controllers;

import com.journi.challenge.models.Purchase;
import com.journi.challenge.models.PurchaseRequest;
import com.journi.challenge.models.PurchaseStats;
import com.journi.challenge.services.PurchasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class PurchasesController {

    @Autowired
    private PurchasesService purchasesService;

    @GetMapping("/purchases/statistics")
    @ResponseBody
    public PurchaseStats getStats() {
        return this.purchasesService.getLast30DaysStats();
    }

    @PostMapping("/purchases")
    @ResponseBody
    public Purchase save(@RequestBody PurchaseRequest purchaseRequest) {
        Purchase newPurchase = new Purchase(
                purchaseRequest.getInvoiceNumber(),
                LocalDateTime.parse(purchaseRequest.getDateTime(), DateTimeFormatter.ISO_DATE_TIME),
                purchaseRequest.getProductIds(),
                purchaseRequest.getCustomerName(),
                purchaseRequest.getAmount(),
                purchaseRequest.getCurrencyCode()
        );
        purchasesService.save(newPurchase);
        return newPurchase;
    }
}
