package com.journi.challenge.controllers;

import com.journi.challenge.models.Purchase;
import com.journi.challenge.models.PurchaseRequest;
import com.journi.challenge.models.PurchaseStats;
import com.journi.challenge.services.PurchasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return purchasesService.save(purchaseRequest);
    }
}
