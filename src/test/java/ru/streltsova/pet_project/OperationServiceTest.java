package ru.streltsova.pet_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.streltsova.pet_project.enums.CurrencyEnum;
import ru.streltsova.pet_project.models.Client;
import ru.streltsova.pet_project.models.Operations;
import ru.streltsova.pet_project.repositories.OperationsRepository;
import ru.streltsova.pet_project.security.ClientDetails;
import ru.streltsova.pet_project.services.OperationsService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OperationServiceTest {

    @Mock
    private OperationsRepository operationsRepository;

    @InjectMocks
    private OperationsService operationsService;

    private Client client;

    @BeforeEach
    void setUp(){
        client=new Client();
        client.setId(1);
    }

    @Test
    void findAll_positiveCase(){
        List<Operations> expectedOperations = List.of(new Operations(), new Operations());
        when(operationsRepository.findAllByClientId(client.getId())).thenReturn(expectedOperations);

        List<Operations> actualOperations = operationsService.findAll(client);

        assertEquals(expectedOperations.size(), actualOperations.size());
        assertEquals(expectedOperations, actualOperations);

        verify(operationsRepository, times(1)).findAllByClientId(client.getId());

    }

    @Test
    void findAll_returnEmptyListCase(){
        when(operationsRepository.findAllByClientId(client.getId())).thenReturn(Collections.emptyList());

        List<Operations> actualOperations=operationsService.findAll(client);

        assertTrue(actualOperations.isEmpty());

        verify(operationsRepository,times(1)).findAllByClientId(client.getId());

    }

    @Test
    void saveOperation_shouldSaveCorrectOperations(){
        CurrencyEnum currencyFrom = CurrencyEnum.USD;
        CurrencyEnum currencyTo = CurrencyEnum.EUR;
        BigDecimal conversionAmount = new BigDecimal("100");
        BigDecimal originalAmount = new BigDecimal("120");

        operationsService.saveOperation(client, currencyFrom, currencyTo, conversionAmount, originalAmount);

    }




}
