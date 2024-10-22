package ru.streltsova.pet_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.streltsova.pet_project.enums.CurrencyEnum;
import ru.streltsova.pet_project.exceptions.ClientNotFoundException;
import ru.streltsova.pet_project.models.Client;
import ru.streltsova.pet_project.models.Wallet;
import ru.streltsova.pet_project.repositories.ClientRepository;
import ru.streltsova.pet_project.repositories.WalletRepository;
import ru.streltsova.pet_project.services.ConversionService;
import ru.streltsova.pet_project.services.OperationsService;
import ru.streltsova.pet_project.services.WalletService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

// нужно для работы моков
@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock // аннотация говорит о необходимости создать мок
    private ClientRepository clientRepository;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private ConversionService conversionService;
    @Mock
    private OperationsService operationsService;

    @InjectMocks // говорит о том, что создатся walletService с моками выше
    private WalletService walletService; // сервис, который мы тестируем

    private Client client;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1);

        wallet = new Wallet();
    }

    @Test
    void add_ShouldAddWalletWhenClientExists() {
        // Настройка поведения mock-объектов
        when(clientRepository.existsById(client.getId())).thenReturn(true);

        // Выполнение метода
        walletService.add(client, wallet);

        // Проверка: кошелек должен быть сохранен
        verify(walletRepository, times(1)).save(wallet);
        assertEquals(client, wallet.getClient());
        assertEquals(CurrencyEnum.USD, wallet.getCurrency());
    }

    @Test
    void add_ShouldThrowExceptionWhenClientNotFound() {
        // Настройка: клиент не существует
        when(clientRepository.existsById(client.getId())).thenReturn(false);

        // Проверка: метод должен выбросить исключение ClientNotFoundException
        ClientNotFoundException thrownException = assertThrows(
                ClientNotFoundException.class,
                () -> walletService.add(client, wallet)
        );

        // Убедимся, что сообщение исключения корректно
        assertEquals("This Client is not found!", thrownException.getMessage());

        // Проверка: кошелек не должен быть сохранен
        verify(walletRepository, never()).save(wallet);
    }
}
