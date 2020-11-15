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
    //    private List<Product> allProducts = new ArrayList<>();
//    {
//        allProducts.add(new Product("photobook-square-soft-cover", "Photobook Square with Soft Cover", 25.0));
//        allProducts.add(new Product("photobook-square-hard-cover", "Photobook Square with Hard Cover", 30.0));
//        allProducts.add(new Product("photobook-landscape-soft-cover", "Photobook Landscape with Soft Cover", 35.0));
//        allProducts.add(new Product("photobook-landscape-hard-cover", "Photobook Landscape with Hard Cover", 45.0));
//    }
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CurrencyConverter currencyConverter;

    public List<Product> list(String currencyCode) {
        String currency = this.currencyConverter.getCurrencyForCountryCode(currencyCode);
        return this.productRepository.findAll().stream().map(product -> {
            product.setPrice(this.currencyConverter.convertEurToCurrency(currency, product.getPrice()));
            product.setCurrencyCode(currency);
            return product;
        }).collect(Collectors.toList());
    }
}
