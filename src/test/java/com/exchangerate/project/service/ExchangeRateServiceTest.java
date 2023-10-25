package com.exchangerate.project.service;

import com.exchangerate.project.dao.ExchangeRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ExchangeRateServiceTest {

    @Autowired
    private ExchangeRateService exchangeRateService;


    @BeforeEach
    void mockFetchData(){
        // Prepare data
        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setQuotes(Map.of(
                "USDGBP", BigDecimal.valueOf(0.73),
                "USDJPY", BigDecimal.valueOf(110)
        ));
        ReflectionTestUtils.setField(exchangeRateService, "exchangeRates", mockResponse.getQuotes());
    }

    @Test
    public void testGetExchangeRate_success() {

        // Verify results
        BigDecimal exchangeRate = exchangeRateService.getExchangeRate("USD", "GBP");
        assertEquals(BigDecimal.valueOf(0.73), exchangeRate);
        assertNotNull(exchangeRate);
    }

    @Test
    public void testGetExchangeRate_failure() {
        assertThrows(IllegalArgumentException.class, () -> {
            exchangeRateService.getExchangeRate("USD", "XYZ");
        });
    }

    @Test
    public void testGetAllExchangeRatesFrom() {
        Map<String, BigDecimal> filteredRates = exchangeRateService.getAllExchangeRatesFrom("USD");

        // Assuming that the exchangeRates map has USDGBP and USDJPY rates
        assertEquals(2, filteredRates.size());
        assertTrue(filteredRates.containsKey("GBP"));
        assertTrue(filteredRates.containsKey("JPY"));
    }

    @Test
    public void testConvertValue() {
        BigDecimal convertedValue = exchangeRateService.convertValue("USD", "GBP", BigDecimal.valueOf(100));

        // Assuming USD to GBP rate is 0.73
        assertEquals(0, BigDecimal.valueOf(73).compareTo(convertedValue));
    }

    @Test
    public void testConvertValueMultiple(){
        List<String> toCurrencies = Arrays.asList("GBP", "JPY");
        Map<String, BigDecimal> conversions = exchangeRateService.convertValueMultiple("USD", toCurrencies, BigDecimal.valueOf(100));

        // Assuming USD to GBP rate is 0.73 and USD to JPY rate is 110
        assertEquals(2, conversions.size());
        assertEquals(0, BigDecimal.valueOf(73).compareTo(conversions.get("GBP")));
        assertEquals(0, BigDecimal.valueOf(11000).compareTo(conversions.get("JPY")));
    }
}
