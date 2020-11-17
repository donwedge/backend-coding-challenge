package com.journi.challenge.models;

import javax.persistence.*;
import java.sql.Timestamp;
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
    private String invoiceNumber;

    //    @Convert(converter = LocalDateTimeConverter.class)
    private Timestamp timestamp;

    @OneToMany
    private List<PurchaseItems> productIds;

    private String customerName;
    private String currencyCode;
    private Double totalValue;

    public Purchase() {
    }

    public Purchase(String invoiceNumber, LocalDateTime timestamp, List<String> productIds, String customerName,
                    Double totalValue, String currencyCode) {
        this.invoiceNumber = invoiceNumber;
        this.timestamp = Timestamp.valueOf(timestamp);
        this.customerName = customerName;
        this.totalValue = totalValue;
        this.currencyCode = currencyCode;
        List<PurchaseItems> purchaseItems = new ArrayList<>();
        productIds.forEach(id -> purchaseItems.add(new PurchaseItems(id, invoiceNumber)));
        this.productIds = purchaseItems;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = Timestamp.valueOf(timestamp);
    }

    public List<PurchaseItems> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<PurchaseItems> productIds) {
        this.productIds = productIds;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }
}
