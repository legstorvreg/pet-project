package ru.streltsova.pet_project.dto;

import ru.streltsova.pet_project.enums.CurrencyEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class ConversionRequestDTO {

    @NotNull
    private CurrencyEnum from;
    @NotNull
    private CurrencyEnum to;
    @Positive
    private BigDecimal amount;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
