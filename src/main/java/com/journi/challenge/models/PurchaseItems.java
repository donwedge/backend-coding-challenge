package com.journi.challenge.models;

import javax.persistence.*;

@Table(name = "purchaseItems")
@Entity
public class PurchaseItems {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String productId;

    @Column
    private String invoiceNumber;

    public PurchaseItems(String productId, String invoiceNumber) {
        this.productId = productId;
        this.invoiceNumber = invoiceNumber;
    }

    public String getProductId() {
        return productId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }
}
