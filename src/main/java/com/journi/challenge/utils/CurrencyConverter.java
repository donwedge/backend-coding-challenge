package com.journi.challenge.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class CurrencyConverter {
    private final Map<String, String> supportedCountriesCurrency;
    private final Map<String, Double> currencyEurRate;

    public CurrencyConverter() {
        this.supportedCountriesCurrency = new HashMap<>();
        this.supportedCountriesCurrency.put("AT", "EUR");
        this.supportedCountriesCurrency.put("DE", "EUR");
        this.supportedCountriesCurrency.put("HU", "HUF");
        this.supportedCountriesCurrency.put("GB", "GBP");
        this.supportedCountriesCurrency.put("FR", "EUR");
        this.supportedCountriesCurrency.put("PT", "EUR");
        this.supportedCountriesCurrency.put("IE", "EUR");
        this.supportedCountriesCurrency.put("ES", "EUR");
        this.supportedCountriesCurrency.put("BR", "BRL");
        this.supportedCountriesCurrency.put("US", "USD");
        this.supportedCountriesCurrency.put("CA", "CAD");

        this.currencyEurRate = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            URL rates = getClass().getResource("/eur_rate.json");
            JsonNode ratesTree = mapper.readTree(rates);
            Iterator<JsonNode> currenciesIterator = ratesTree.findPath("currencies").elements();
            currenciesIterator.forEachRemaining(currency -> this.currencyEurRate.put(currency.get("currency").asText(), currency.get("rate").asDouble()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getCurrencyForCountryCode(String countryCode) {
        return supportedCountriesCurrency.getOrDefault(countryCode.toUpperCase(), "EUR");
    }

    public Double convertCurrencyToEur(String currencyCode, Double originalValue) {
        return originalValue * this.currencyEurRate.getOrDefault(currencyCode, 1.0);
    }

    public Double convertEurToCurrency(String currencyCode, Double eurValue) {
        return eurValue * currencyEurRate.getOrDefault(currencyCode, 1.0);
    }
}
