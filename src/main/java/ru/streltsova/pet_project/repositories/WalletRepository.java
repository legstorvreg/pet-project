package ru.streltsova.pet_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.streltsova.pet_project.enums.CurrencyEnum;
import ru.streltsova.pet_project.models.Wallet;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Optional<Wallet> findByClientIdAndCurrency(Integer clientId, CurrencyEnum currency);
}
