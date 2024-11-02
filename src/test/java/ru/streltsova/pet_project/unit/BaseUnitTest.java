package ru.streltsova.pet_project.unit;

import org.junit.jupiter.api.BeforeEach;
import ru.streltsova.pet_project.dto.ConversionRequestDTO;
import ru.streltsova.pet_project.enums.CurrencyEnum;
import ru.streltsova.pet_project.models.Client;
import ru.streltsova.pet_project.models.Wallet;

import java.math.BigDecimal;

class BaseUnitTest {

    protected Client client;
    protected Wallet wallet;
    protected ConversionRequestDTO dto;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1);
        wallet = new Wallet();
        dto = new ConversionRequestDTO();
        dto.setFrom(CurrencyEnum.USD);
        dto.setTo(CurrencyEnum.EUR);
        dto.setAmount(new BigDecimal("100"));
    }
}
