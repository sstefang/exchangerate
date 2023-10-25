package com.exchangerate.project.service;

import com.exchangerate.project.dao.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExchangeRateService {

    private final Map<String, BigDecimal> exchangeRates = new ConcurrentHashMap<>();

    @Value("${exchangerate.api.key}")
    private String API_URL;

    @Scheduled(fixedRate = 600000)
    public void fetchData() {
        RestTemplate restTemplate = new RestTemplate();
        ExchangeRateResponse response = restTemplate.getForObject(API_URL , ExchangeRateResponse.class);
        if (response != null && response.getQuotes() != null) {
            exchangeRates.clear();
            exchangeRates.putAll(response.getQuotes());
        }
    }


    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        String key = fromCurrency + toCurrency;
        BigDecimal rate = exchangeRates.get(key);
        if (rate == null) {
            throw new IllegalArgumentException("Exchange rate not found for " + fromCurrency + " to " + toCurrency);
        }
        return rate;
    }

    public Map<String, BigDecimal> getAllExchangeRatesFrom(String fromCurrency) {
        Map<String, BigDecimal> filteredRates = new HashMap<>();
        for (Map.Entry<String, BigDecimal> entry : exchangeRates.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(fromCurrency)) {
                String toCurrency = key.substring(fromCurrency.length());
                filteredRates.put(toCurrency, entry.getValue());
            }
        }
        return filteredRates;
    }

    public BigDecimal convertValue(String fromCurrency, String toCurrency, BigDecimal amount) {
        BigDecimal exchangeRate = getExchangeRate(fromCurrency, toCurrency);
        return amount.multiply(exchangeRate);
    }

    public Map<String, BigDecimal> convertValueMultiple(String fromCurrency, List<String> toCurrencies, BigDecimal amount) {
        Map<String, BigDecimal> conversions = new HashMap<>();
        for (String toCurrency : toCurrencies) {
            BigDecimal exchangeRate = getExchangeRate(fromCurrency, toCurrency);
            BigDecimal convertedValue = amount.multiply(exchangeRate);
            conversions.put(toCurrency, convertedValue);
        }
        return conversions;
    }


//    @Cacheable("exchangeRates")
//    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
//        String apiKey = exchangeRateApiConfiguration.getApiKey();
//        String url = String.format("%s/convert?from=%s&to=%s&apiKey=%s", API_URL, fromCurrency, toCurrency, apiKey);
//        ResponseEntity<ExchangeRateResponse> response = restTemplate.getForEntity(url, ExchangeRateResponse.class);
//        return Objects.requireNonNull(response.getBody()).getQuotes().get(0);
//    }
//
//    @Cacheable("allExchangeRates")
//    public Map<String, BigDecimal> getAllExchangeRates(String baseCurrency) {
//        String url = String.format("%s/latest?base=%s", API_URL, baseCurrency);
//        ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
//        return response.getQuotes();
//    }
//    @Cacheable("conversion")
//    public BigDecimal getConversion(String fromCurrency, String toCurrency, BigDecimal amount) {
//        BigDecimal rate = getExchangeRate(fromCurrency, toCurrency);  // Reusing the method from your previous implementation
//        return rate.multiply(amount);
//    }
//
//    @Cacheable("multipleConversions")
//    public Map<String, BigDecimal> getMultipleConversions(String fromCurrency, List<String> toCurrencies, BigDecimal amount) {
//        Map<String, BigDecimal> conversions = new HashMap<>();
//        for (String toCurrency : toCurrencies) {
//            BigDecimal rate = getExchangeRate(fromCurrency, toCurrency);
//            BigDecimal convertedAmount = rate.multiply(amount);
//            conversions.put(toCurrency, convertedAmount);
//        }
//        return conversions;
//    }


//    @CacheEvict(cacheNames = {"exchangeRates", "allExchangeRates", "conversion", "multipleConversions"}, allEntries = true)
//    public void clearCache() {
//
//    }
}
