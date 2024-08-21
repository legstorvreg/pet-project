package ru.streltsova.pet_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.streltsova.pet_project.models.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByLogin(String login);
}
