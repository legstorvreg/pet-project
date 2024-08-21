package ru.streltsova.pet_project.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.streltsova.pet_project.dto.ConversionRequestDTO;
import ru.streltsova.pet_project.dto.ConversionResultDTO;
import ru.streltsova.pet_project.dto.WalletDTO;
import ru.streltsova.pet_project.enums.CurrencyEnum;
import ru.streltsova.pet_project.models.Wallet;
import ru.streltsova.pet_project.services.ConversionService;
import ru.streltsova.pet_project.services.WalletService;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;
    private final ModelMapper modelMapper;
    private final ConversionService conversionService;

    @Autowired
    public WalletController(WalletService walletService,
                            ModelMapper modelMapper,
                            ConversionService conversionService) {
        this.walletService = walletService;
        this.modelMapper = modelMapper;
        this.conversionService = conversionService;
    }

    @PostMapping("/addMoney")
    public ResponseEntity<HttpStatus> addMoney(@RequestBody @Valid WalletDTO dto) {
        walletService.add(convertToWallet(dto));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/conversion")
    public ResponseEntity<ConversionResultDTO> doConversion(@RequestParam(defaultValue = "USD") CurrencyEnum from,
                                                            @RequestParam(defaultValue = "EUR") CurrencyEnum to,
                                                            @RequestParam BigDecimal amount) {
        ConversionResultDTO result = conversionService.doConversion(from, to, amount);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/buy")
    public ResponseEntity<HttpStatus> buyMoney(@RequestBody @Valid ConversionRequestDTO dto) {
        walletService.buy(dto);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    public Wallet convertToWallet(WalletDTO dto) {
        return modelMapper.map(dto, Wallet.class);
    }
}
