package ru.streltsova.pet_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.streltsova.pet_project.models.Operations;

import java.util.List;

public interface OperationsRepository extends JpaRepository<Operations, Integer> {

    List<Operations> findAllByClientId(Integer clientId);
}
