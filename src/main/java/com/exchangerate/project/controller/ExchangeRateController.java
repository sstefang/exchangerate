package com.exchangerate.project.controller;

import com.exchangerate.project.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exchange-rates")
@Tag(name = "Exchange Rate Operations", description = "APIs for fetching and converting exchange rates")
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping("/rate")
    @Operation(summary = "Fetch the exchange rate between two currencies",
            description = "Provide two currency codes to get the exchange rate from one to the other")
    public ResponseEntity<BigDecimal> getExchangeRate(
            @Parameter(description = "The currency code to convert from", example = "USD") @RequestParam String fromCurrency,
            @Parameter(description = "The currency code to convert to", example = "EUR") @RequestParam String toCurrency) {

        BigDecimal rate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        return ResponseEntity.ok(rate);
    }

    @GetMapping("/{baseCurrency}")
    @Operation(summary = "Fetch all exchange rates for a base currency",
            description = "Provide a base currency code to get the exchange rates to all other currencies")
    public ResponseEntity<Map<String, BigDecimal>> getAllExchangeRates(
            @Parameter(description = "The base currency code", example = "USD") @PathVariable String baseCurrency) {

        Map<String, BigDecimal> rates = exchangeRateService.getAllExchangeRatesFrom(baseCurrency);
        return ResponseEntity.ok(rates);
    }

    @GetMapping("/convert")
    @Operation(summary = "Convert an amount from one currency to another",
            description = "Provide two currency codes and an amount to convert the amount from one currency to the other")
    public ResponseEntity<BigDecimal> getConversion(
            @Parameter(description = "The currency code to convert from", example = "USD") @RequestParam String fromCurrency,
            @Parameter(description = "The currency code to convert to", example = "EUR") @RequestParam String toCurrency,
            @Parameter(description = "The amount to convert", example = "100.0") @RequestParam BigDecimal amount) {

        BigDecimal convertedAmount = exchangeRateService.convertValue(fromCurrency, toCurrency, amount);
        return ResponseEntity.ok(convertedAmount);
    }

    @GetMapping("/convert/multiple")
    @Operation(summary = "Convert an amount from one currency to multiple other currencies",
            description = "Provide a currency code, a list of target currency codes, and an amount to get the converted amounts")
    public ResponseEntity<Map<String, BigDecimal>> getMultipleConversions(
            @Parameter(description = "The currency code to convert from", example = "USD") @RequestParam String fromCurrency,
            @Parameter(description = "The list of currency codes to convert to") @RequestParam List<String> toCurrencies,
            @Parameter(description = "The amount to convert", example = "100.0") @RequestParam BigDecimal amount) {

        Map<String, BigDecimal> conversions = exchangeRateService.convertValueMultiple(fromCurrency, toCurrencies, amount);
        return ResponseEntity.ok(conversions);
    }
}
