package ru.streltsova.pet_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.streltsova.pet_project.dto.ConversionRequestDTO;
import ru.streltsova.pet_project.dto.ConversionResultDTO;
import ru.streltsova.pet_project.enums.CurrencyEnum;
import ru.streltsova.pet_project.exceptions.ClientNotFoundException;
import ru.streltsova.pet_project.exceptions.CurrencyNotFoundException;
import ru.streltsova.pet_project.models.Client;
import ru.streltsova.pet_project.models.Wallet;
import ru.streltsova.pet_project.repositories.ClientRepository;
import ru.streltsova.pet_project.repositories.WalletRepository;
import ru.streltsova.pet_project.security.ClientDetails;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final ClientRepository clientRepository;
    private final ConversionService conversionService;
    private final OperationsService operationsService;

    @Autowired
    public WalletService(WalletRepository walletRepository,
                         ClientRepository clientRepository,
                         ConversionService conversionService,
                         OperationsService operationsService) {
        this.walletRepository = walletRepository;
        this.clientRepository = clientRepository;
        this.conversionService = conversionService;
        this.operationsService = operationsService;
    }

    @Transactional
    public void add(Wallet wallet) {
        Client client = ((ClientDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();

        Integer clientId = client.getId();

        if (!clientRepository.existsById(clientId)) {
            throw new ClientNotFoundException("This Client is not found!");
        }

        wallet.setClient(client);
        wallet.getClient().setId(clientId);
        wallet.setCurrency(CurrencyEnum.USD);

        walletRepository.save(wallet);
    }

    @Transactional
    public void buy(ConversionRequestDTO dto) {
        Client client = ((ClientDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        Integer clientId = client.getId();

        Optional<Wallet> walletFromOptional = walletRepository.findByClientIdAndCurrency(clientId, dto.getFrom());
        if (walletFromOptional.isEmpty()) {
            throw new CurrencyNotFoundException("Client has not this currency for conversion!");
        }

        Wallet walletFrom = walletFromOptional.get();
        if (walletFrom.getAmount().compareTo(dto.getAmount()) < 0) {
            throw new IllegalStateException("Not enough money!");
        }

        ConversionResultDTO conversionResult = conversionService.doConversion(
                dto.getFrom(),
                dto.getTo(),
                dto.getAmount()
        );

        Optional<Wallet> walletToOptional = walletRepository.findByClientIdAndCurrency(clientId, dto.getTo());
        if (walletToOptional.isPresent()) {
            Wallet walletTo = walletToOptional.get();

            walletFrom.setAmount(walletFrom.getAmount().subtract(dto.getAmount()));
            walletTo.setAmount(walletTo.getAmount().add(conversionResult.getConversionAmount()));
        } else {
            Wallet walletTo = new Wallet(client, BigDecimal.ZERO, dto.getTo());

            walletFrom.setAmount(walletFrom.getAmount().subtract(dto.getAmount()));
            walletTo.setAmount(conversionResult.getConversionAmount());

            walletRepository.save(walletTo);
        }

        operationsService.saveOperation(
                client,
                dto.getFrom(),
                dto.getTo(),
                conversionResult.getConversionAmount(),
                conversionResult.getOriginalAmount()
        );
    }
}
