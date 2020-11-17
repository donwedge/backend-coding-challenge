package com.journi.challenge.services;

import com.journi.challenge.models.Purchase;
import com.journi.challenge.models.PurchaseItems;
import com.journi.challenge.models.PurchaseRequest;
import com.journi.challenge.models.PurchaseStats;
import com.journi.challenge.repositories.PurchaseItemRepository;
import com.journi.challenge.repositories.PurchaseRepository;
import com.journi.challenge.utils.CurrencyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchasesService {

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private PurchaseItemRepository purchaseItemRepository;

    private CurrencyConverter currencyConverter = new CurrencyConverter();

    public List<Purchase> list() {
        return this.purchaseRepository.findAll();
    }

    public Purchase save(PurchaseRequest purchaseRequest) {
        Purchase purchase = new Purchase();
        purchase.setInvoiceNumber(purchaseRequest.getInvoiceNumber());
        purchase.setTimestamp(LocalDateTime.parse(purchaseRequest.getDateTime(), DateTimeFormatter.ISO_DATE_TIME));
        purchase.setCustomerName(purchaseRequest.getCustomerName());
        purchase.setCurrencyCode(purchaseRequest.getCurrencyCode());
        purchase.setTotalValue(this.currencyConverter.convertCurrencyToEur(purchase.getCurrencyCode(), purchaseRequest.getAmount()));
        List<PurchaseItems> purchaseItems = purchaseRequest.getProductIds().stream().map(id -> {
            PurchaseItems item = new PurchaseItems();
            item.setInvoiceNumber(purchase.getInvoiceNumber());
            item.setProductId(id);
            return item;
        }).collect(Collectors.toList());
        this.purchaseItemRepository.saveAll(purchaseItems);
        purchase.setProductIds(purchaseItems);
        this.purchaseRepository.save(purchase);
        return purchase;
    }

    public PurchaseStats getLast30DaysStats() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE.withZone(ZoneId.of("UTC"));

        LocalDateTime start = LocalDate.now().atStartOfDay().minusDays(30);

        PurchaseRepository.TimestampQuery queryObjects = this.purchaseRepository.findByTimestampGreaterThan(formatter.format(start));
        //using sql to optimise speed instead of pulling whole table to run through a stream. Using ordering to optimise the min/max
        List<Purchase> recentPurchases = this.purchaseRepository.findByTimestampGreaterThanOrderByTimestampDesc(Timestamp.valueOf(start));

        long countPurchases = recentPurchases.size();


        double totalAmountPurchases = recentPurchases.stream().mapToDouble(Purchase::getTotalValue).sum();
        double minAmountPurchase = recentPurchases.stream().mapToDouble(Purchase::getTotalValue).min().orElse(0.0);
        double maxAmountPurchase = recentPurchases.stream().mapToDouble(Purchase::getTotalValue).max().orElse(0.0);
        return new PurchaseStats(
                formatter.format(recentPurchases.get(0).getTimestamp().toLocalDateTime()),
                formatter.format(recentPurchases.get(recentPurchases.size() - 1).getTimestamp().toLocalDateTime()),
                countPurchases,
                totalAmountPurchases,
                totalAmountPurchases / countPurchases,
                minAmountPurchase,
                maxAmountPurchase
        );
    }
}
