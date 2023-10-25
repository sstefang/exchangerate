package com.exchangerate.project.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public class ExchangeRateResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("terms")
    private String terms;

    @JsonProperty("privacy")
    private String privacy;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("source")
    private String source;

    @JsonProperty("quotes")
    private Map<String, BigDecimal> quotes;

    // Getters and setters for each field

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Map<String, BigDecimal> getQuotes() {
        return quotes;
    }

    public void setQuotes(Map<String, BigDecimal> quotes) {
        this.quotes = quotes;
    }
}

