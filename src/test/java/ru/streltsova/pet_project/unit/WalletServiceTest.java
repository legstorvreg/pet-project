package ru.streltsova.pet_project.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.streltsova.pet_project.dto.ConversionResultDTO;
import ru.streltsova.pet_project.enums.CurrencyEnum;
import ru.streltsova.pet_project.exceptions.ClientNotFoundException;
import ru.streltsova.pet_project.exceptions.CurrencyNotFoundException;
import ru.streltsova.pet_project.models.Wallet;
import ru.streltsova.pet_project.repositories.ClientRepository;
import ru.streltsova.pet_project.repositories.WalletRepository;
import ru.streltsova.pet_project.services.ConversionService;
import ru.streltsova.pet_project.services.OperationsService;
import ru.streltsova.pet_project.services.WalletService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest extends BaseUnitTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ConversionService conversionService;
    @Mock
    private OperationsService operationsService;

    @InjectMocks
    private WalletService walletService;

    @Test
    void test() {
        when(clientRepository.existsById(1)).thenReturn(true);

        walletService.add(client, wallet);

        assertEquals(CurrencyEnum.USD, wallet.getCurrency());
        assertEquals(1, wallet.getClient().getId());
    }

    @Test
    void test2() {
        when(clientRepository.existsById(1)).thenReturn(false);

        assertThrows(ClientNotFoundException.class, () -> walletService.add(client, wallet));
    }

    @Test
    void buy_positiveCaseWhenClientAndWalletExists() {
        Wallet walletFrom = new Wallet(client, new BigDecimal("200"), CurrencyEnum.USD);
        Wallet walletTo = new Wallet(client, new BigDecimal("50"), CurrencyEnum.EUR);

        ConversionResultDTO conversionResult = new ConversionResultDTO();
        conversionResult.setConversionAmount(new BigDecimal("80"));
        conversionResult.setOriginalAmount(dto.getAmount());

        when(walletRepository.findByClientIdAndCurrency(client.getId(), CurrencyEnum.USD))
                .thenReturn(Optional.of(walletFrom));
        when(walletRepository.findByClientIdAndCurrency(client.getId(), CurrencyEnum.EUR))
                .thenReturn(Optional.of(walletTo));
        when(conversionService.doConversion(CurrencyEnum.USD, CurrencyEnum.EUR, dto.getAmount()))
                .thenReturn(conversionResult);

        walletService.buy(client, dto);

        assertEquals(new BigDecimal("100"), walletFrom.getAmount());
        assertEquals(new BigDecimal("130"), walletTo.getAmount());

        verify(operationsService, times(1)).saveOperation(
                eq(client),
                eq(CurrencyEnum.USD),
                eq(CurrencyEnum.EUR),
                eq(conversionResult.getConversionAmount()),
                eq(conversionResult.getOriginalAmount())
        );
    }

    @Test
    void buy_shouldThrowCurrencyNotFoundException() {
        when(walletRepository.findByClientIdAndCurrency(client.getId(), CurrencyEnum.USD))
                .thenReturn(Optional.empty());

        CurrencyNotFoundException exception = assertThrows(
                CurrencyNotFoundException.class,
                () -> walletService.buy(client, dto));

        assertEquals("Client has not this currency for conversion!", exception.getMessage());
        verify(walletRepository, never()).findByClientIdAndCurrency(client.getId(), CurrencyEnum.EUR);
        verify(conversionService, never()).doConversion(any(), any(), any());
        verify(operationsService, never()).saveOperation(any(), any(), any(), any(), any());
    }

    @Test
    void buy_shouldThrowIllegalStateException() {
        Wallet walletFrom = new Wallet(client, new BigDecimal("50"), CurrencyEnum.USD);

        when(walletRepository.findByClientIdAndCurrency(client.getId(), CurrencyEnum.USD))
                .thenReturn(Optional.of(walletFrom));
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> walletService.buy(client, dto)
        );

        assertEquals("Not enough money!", exception.getMessage());
        verify(walletRepository, never()).findByClientIdAndCurrency(client.getId(), CurrencyEnum.EUR);
        verify(conversionService, never()).doConversion(any(), any(), any());
        verify(operationsService, never()).saveOperation(any(), any(), any(), any(), any());
    }

    @Test
    void buy_createNewWalletIfIsDoesntExist() {
        Wallet walletFrom = new Wallet(client, new BigDecimal("200"), CurrencyEnum.USD);

        when(walletRepository.findByClientIdAndCurrency(client.getId(), CurrencyEnum.USD))
                .thenReturn(Optional.of(walletFrom));
        when(walletRepository.findByClientIdAndCurrency(client.getId(), CurrencyEnum.EUR))
                .thenReturn(Optional.empty());

        ConversionResultDTO conversionResult = new ConversionResultDTO();
        conversionResult.setConversionAmount(new BigDecimal("80"));
        conversionResult.setOriginalAmount(dto.getAmount());
        when(conversionService.doConversion(CurrencyEnum.USD, CurrencyEnum.EUR, dto.getAmount()))
                .thenReturn(conversionResult);

        walletService.buy(client, dto);
        assertEquals(new BigDecimal("100"), walletFrom.getAmount());
        verify(walletRepository, times(1)).save(any(Wallet.class));

        verify(operationsService, times(1)).saveOperation(
                eq(client),
                eq(CurrencyEnum.USD),
                eq(CurrencyEnum.EUR),
                eq(conversionResult.getConversionAmount()),
                eq(conversionResult.getOriginalAmount())
        );
    }
}

