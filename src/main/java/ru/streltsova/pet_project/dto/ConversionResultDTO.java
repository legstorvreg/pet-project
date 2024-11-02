package ru.streltsova.pet_project.dto;

import ru.streltsova.pet_project.enums.CurrencyEnum;

import java.math.BigDecimal;

public class ConversionResultDTO {

    private CurrencyEnum from;
    private CurrencyEnum to;
    private BigDecimal originalAmount;
    private BigDecimal conversionAmount;
    private BigDecimal currencyRate;

    public CurrencyEnum getFrom() {
        return from;
    }

    public void setFrom(CurrencyEnum from) {
        this.from = from;
    }

    public CurrencyEnum getTo() {
        return to;
    }

    public void setTo(CurrencyEnum to) {
        this.to = to;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public BigDecimal getConversionAmount() {
        return conversionAmount;
    }

    public void setConversionAmount(BigDecimal conversionAmount) {
        this.conversionAmount = conversionAmount;
    }

    public BigDecimal getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(BigDecimal currencyRate) {
        this.currencyRate = currencyRate;
    }


}
