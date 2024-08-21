package ru.streltsova.pet_project.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CurrencyRateResponseDTO {
    private Map<String, BigDecimal> rates = new HashMap<>();

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
