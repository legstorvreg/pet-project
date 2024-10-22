package ru.streltsova.pet_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.streltsova.pet_project.enums.CurrencyEnum;
import ru.streltsova.pet_project.models.Client;
import ru.streltsova.pet_project.models.Operations;
import ru.streltsova.pet_project.repositories.OperationsRepository;
import ru.streltsova.pet_project.security.ClientDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OperationsService {

    private final OperationsRepository operationsRepository;

    @Autowired
    public OperationsService(OperationsRepository operationsRepository) {
        this.operationsRepository = operationsRepository;
    }

    public List<Operations> findAll(Client client) {
        return operationsRepository.findAllByClientId(client.getId());
    }

    public void saveOperation(Client client,
                              CurrencyEnum currencyFrom,
                              CurrencyEnum currencyTo,
                              BigDecimal conversionAmount,
                              BigDecimal originalAmount) {
        Operations operation = new Operations();
        operation.setClient(client);
        operation.setCurrencyFrom(currencyFrom);
        operation.setCurrencyTo(currencyTo);
        operation.setConversionAmount(conversionAmount);
        operation.setOriginalAmount(originalAmount);
        operation.setOperationDate(LocalDateTime.now());
        operationsRepository.save(operation);

    }
}
