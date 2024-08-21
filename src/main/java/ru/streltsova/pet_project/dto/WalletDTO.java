package ru.streltsova.pet_project.dto;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class WalletDTO {

    @Positive(message = "Amount must be positive!")
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
