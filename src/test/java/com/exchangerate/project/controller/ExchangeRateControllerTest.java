package com.exchangerate.project.controller;
import com.exchangerate.project.service.ExchangeRateService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ExchangeRateController.class)
public class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Test
    public void testGetExchangeRate() throws Exception {
        BigDecimal rate = new BigDecimal("0.85");
        when(exchangeRateService.getExchangeRate("USD", "EUR")).thenReturn(rate);

        mockMvc.perform(get("/api/exchange-rates/rate")
                        .param("fromCurrency", "USD")
                        .param("toCurrency", "EUR"))
                .andExpect(status().isOk())
                .andExpect(content().json(rate.toString()));
    }

    @Test
    public void testGetAllExchangeRates() throws Exception {
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.85"));
        when(exchangeRateService.getAllExchangeRatesFrom("USD")).thenReturn(rates);

        mockMvc.perform(get("/api/exchange-rates/USD"))
                .andExpect(status().isOk())
                .andExpect(content().json(rates.toString()));
    }

    @Test
    public void testGetConversion() throws Exception {
        BigDecimal convertedAmount = new BigDecimal("85.0");
        when(exchangeRateService.convertValue("USD", "EUR", new BigDecimal("100.0"))).thenReturn(convertedAmount);

        mockMvc.perform(get("/api/exchange-rates/convert")
                        .param("fromCurrency", "USD")
                        .param("toCurrency", "EUR")
                        .param("amount", "100.0"))
                .andExpect(status().isOk())
                .andExpect(content().json(convertedAmount.toString()));
    }

    @Test
    public void testGetMultipleConversions() throws Exception {
        Map<String, BigDecimal> conversions = new HashMap<>();
        conversions.put("EUR", new BigDecimal("85.0"));
        when(exchangeRateService.convertValueMultiple("USD", List.of("EUR"), new BigDecimal("100.0"))).thenReturn(conversions);

        mockMvc.perform(get("/api/exchange-rates/convert/multiple")
                        .param("fromCurrency", "USD")
                        .param("toCurrencies", "EUR")
                        .param("amount", "100.0"))
                .andExpect(status().isOk())
                .andExpect(content().json(conversions.toString()));
    }
}

