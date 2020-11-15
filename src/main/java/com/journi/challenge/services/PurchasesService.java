package com.journi.challenge.services;

import com.journi.challenge.models.Purchase;
import com.journi.challenge.models.PurchaseStats;
import com.journi.challenge.repositories.PurchaseRepository;
import com.journi.challenge.utils.CurrencyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.DoubleStream;

@Service
public class PurchasesService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    private CurrencyConverter currencyConverter = new CurrencyConverter();

    public List<Purchase> list() {
        return this.purchaseRepository.findAll();
    }

    public void save(Purchase purchase) {
        purchase.setTotalValue(this.currencyConverter.convertCurrencyToEur(purchase.getCurrencyCode(), purchase.getTotalValue()));
        this.purchaseRepository.save(purchase);
    }

    public PurchaseStats getLast30DaysStats() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE.withZone(ZoneId.of("UTC"));

        LocalDateTime start = LocalDate.now().atStartOfDay().minusDays(30);

        //using sql to optimise speed instead of pulling whole table to run through a stream. Using ordering to optimise the min/max
        List<Purchase> recentPurchases = this.purchaseRepository.findByTimestampGreaterThanOrderByTimestampDesc(start);

        long countPurchases = recentPurchases.size();
        DoubleStream valueStream = recentPurchases.stream().mapToDouble(Purchase::getTotalValue);
        double totalAmountPurchases = valueStream.sum();
        double minAmountPurchase = valueStream.min().orElse(0.0);
        double maxAmountPurchase = valueStream.max().orElse(0.0);
        return new PurchaseStats(
                formatter.format(recentPurchases.get(0).getTimestamp()),
                formatter.format(recentPurchases.get(recentPurchases.size() - 1).getTimestamp()),
                countPurchases,
                totalAmountPurchases,
                totalAmountPurchases / countPurchases,
                minAmountPurchase,
                maxAmountPurchase
        );
    }
}
