package com.journi.challenge.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a completed Purchase.
 * invoiceNumber is unique
 * timestamp when the purchase was made. Epoch milliseconds
 * productIds list of product ids included in this purchase
 * customerName name of the customer
 * totalValue total value of this purchase, in EUR
 */
@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    @Column(name = "invoiceNumber", unique = true)
    private final String invoiceNumber;

    private final LocalDateTime timestamp;

    @OneToMany
    private final List<PurchaseItems> productIds;

    private final String customerName;
    private final String currencyCode;
    private Double totalValue;

    public Purchase(String invoiceNumber, LocalDateTime timestamp, List<String> productIds, String customerName,
                    Double totalValue, String currencyCode) {
        this.invoiceNumber = invoiceNumber;
        this.timestamp = timestamp;
        this.customerName = customerName;
        this.totalValue = totalValue;
        this.currencyCode = currencyCode;
        List<PurchaseItems> purchaseItems = new ArrayList<>();
        productIds.forEach(id -> purchaseItems.add(new PurchaseItems(id, invoiceNumber)));
        this.productIds = purchaseItems;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<PurchaseItems> getProductIds() {
        return productIds;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }
}
