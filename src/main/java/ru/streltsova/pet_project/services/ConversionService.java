package ru.streltsova.pet_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.streltsova.pet_project.dto.ConversionResultDTO;
import ru.streltsova.pet_project.dto.CurrencyRateResponseDTO;
import ru.streltsova.pet_project.enums.CurrencyEnum;

import java.math.BigDecimal;

@Service
public class ConversionService {

    private final RestTemplate restTemplate;

    @Autowired
    public ConversionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(cacheNames = "CONVERSION_CACHE", key = "#from.name() + #to.name()")
    public ConversionResultDTO doConversion(CurrencyEnum from, CurrencyEnum to, BigDecimal amount) {
        CurrencyRateResponseDTO response = restTemplate.getForObject(
                "https://open.er-api.com/v6/latest/" + from,
                CurrencyRateResponseDTO.class
        );
        if (response == null) {
            throw new IllegalStateException("No response received.");
        }

        BigDecimal currencyRate = response.getRates().get(to.name());

        ConversionResultDTO result = new ConversionResultDTO();

        result.setFrom(from);
        result.setTo(to);
        result.setOriginalAmount(amount);
        result.setConversionAmount(amount.multiply(currencyRate));
        result.setCurrencyRate(currencyRate);

        return result;
    }
}
