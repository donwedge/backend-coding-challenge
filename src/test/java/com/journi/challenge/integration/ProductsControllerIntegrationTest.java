package com.journi.challenge.integration;

import com.journi.challenge.models.Product;
import com.journi.challenge.services.ProductsService;
import com.journi.challenge.utils.CurrencyConverter;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class ProductsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${server.port}")
    private String serverPort;

    private String getFullUrl() {
        return "http://localhost:" + this.serverPort;
    }

    private TestRestTemplate testRestTemplate = new TestRestTemplate();
    private CurrencyConverter currencyConverter = new CurrencyConverter();

    @BeforeAll
    private static void preLoadProducts(@Autowired ProductsService productsService) {
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(new Product("photobook-square-soft-cover", "Photobook Square with Soft Cover", 25.0));
        allProducts.add(new Product("photobook-square-hard-cover", "Photobook Square with Hard Cover", 30.0));
        allProducts.add(new Product("photobook-landscape-soft-cover", "Photobook Landscape with Soft Cover", 35.0));
        allProducts.add(new Product("photobook-landscape-hard-cover", "Photobook Landscape with Hard Cover", 45.0));
        productsService.saveAll(allProducts);
    }

    @Test
    public void shouldListProductsWithCurrencyCodeAndConvertedPriceDefault() throws Exception {

        ResponseEntity<List> response = testRestTemplate.getForEntity(this.getFullUrl() + "/products", List.class);
        assertThat(response.getBody().size(), IsEqual.equalTo(4));

    }

    @Test
    public void shouldListProductsWithCurrencyCodeAndConvertedPriceZA() throws Exception {
        ResponseEntity<List<Product>> response = testRestTemplate.exchange(this.getFullUrl() + "/products?countryCode=ZA", HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
        });
        assertThat(response.getBody().size(), IsEqual.equalTo(4));
        assertThat(response.getBody().stream().allMatch(p -> {
            return p.getCurrencyCode().equals("ZAR");
        }), IsEqual.equalTo(true));
    }

    @Test
    public void shouldListProductsWithCurrencyCodeEURWhenCountryCodeNonSupported() throws Exception {
        ResponseEntity<List<Product>> response = testRestTemplate.exchange(this.getFullUrl() + "/products?countryCode=JP", HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
        });
        assertThat(response.getBody().size(), IsEqual.equalTo(4));
        assertThat(response.getBody().stream().allMatch(p -> {
            return p.getCurrencyCode().equals("EUR");
        }), IsEqual.equalTo(true));
    }
}
