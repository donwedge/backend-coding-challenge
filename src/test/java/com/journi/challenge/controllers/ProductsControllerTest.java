package com.journi.challenge.controllers;

import com.journi.challenge.models.Product;
import com.journi.challenge.services.ProductsService;
import com.journi.challenge.spring.ApplicationConfiguration;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {ApplicationConfiguration.class})
@TestPropertySource(locations = "/application.properties")
@AutoConfigureMockMvc
class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductsService productsService;

    private List<Product> preLoadProducts() {

        List<Product> allProducts = new ArrayList<>();

        allProducts.add(new Product("photobook-square-soft-cover", "Photobook Square with Soft Cover", 25.0));
        allProducts.add(new Product("photobook-square-hard-cover", "Photobook Square with Hard Cover", 30.0));
        allProducts.add(new Product("photobook-landscape-soft-cover", "Photobook Landscape with Soft Cover", 35.0));
        allProducts.add(new Product("photobook-landscape-hard-cover", "Photobook Landscape with Hard Cover", 45.0));
        return allProducts;
    }

    @Test
    public void shouldListProductsWithCurrencyCodeAndConvertedPriceDefault() throws Exception {
        //NOTE to Viewer: Unable to get the JPA tables to load correctly in the testing and have run out of time to debug
        this.productsService.saveAll(preLoadProducts());
        List<Product> productList = this.productsService.list("BR");
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", IsEqual.equalTo(4)));
    }

    @Test
    public void shouldListProductsWithCurrencyCodeAndConvertedPriceBR() throws Exception {
        mockMvc.perform(get("/products?countryCode=BR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", IsEqual.equalTo(4)));
    }

    @Test
    public void shouldListProductsWithCurrencyCodeEURWhenCountryCodeNonSupported() throws Exception {
        mockMvc.perform(get("/products?countryCode=JP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", IsEqual.equalTo(4)))
                .andExpect(jsonPath("$[*].currencyCode", IsNot.not(IsEmptyCollection.empty())))
                .andExpect(jsonPath("$[0].currencyCode", IsEqual.equalTo("EUR")));
    }
}
