package com.journi.challenge.services;

import com.journi.challenge.models.Product;
import com.journi.challenge.repositories.ProductRepository;
import com.journi.challenge.utils.CurrencyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//Using Service to make use of the autowiring to the sql repo
@Service
public class ProductsService {
    @Autowired
    private ProductRepository productRepository;

    private CurrencyConverter currencyConverter = new CurrencyConverter();

    public void saveAll(List<Product> allProducts) {
        this.productRepository.saveAll(allProducts);
    }

    public List<Product> list(String currencyCode) {
        String currency = this.currencyConverter.getCurrencyForCountryCode(currencyCode);
        return this.productRepository.findAll().stream().map(product -> {
            product.setPrice(this.currencyConverter.convertEurToCurrency(currency, product.getPrice()));
            product.setCurrencyCode(currency);
            return product;
        }).collect(Collectors.toList());
    }
}
