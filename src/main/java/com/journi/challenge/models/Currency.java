package com.journi.challenge.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "currency")
@Entity
public class Currency {
    @Id
    @Column(name = "currency")
    private String currency;

    private double rate;

}
