package ru.streltsova.pet_project.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.streltsova.pet_project.dto.OperationsDTO;
import ru.streltsova.pet_project.models.Operations;
import ru.streltsova.pet_project.services.OperationsService;

import java.util.List;

@RestController
@RequestMapping("/wallet")
public class OperationsController {

    private final OperationsService operationsService;
    private final ModelMapper modelMapper;

    @Autowired
    public OperationsController(OperationsService operationsService, ModelMapper modelMapper) {
        this.operationsService = operationsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/operations")
    public List<OperationsDTO> getOperations() {
        return operationsService.findAll().stream()
                .map(this::convertToOperationsDTO)
                .toList();
    }

    public OperationsDTO convertToOperationsDTO(Operations operations) {
        return modelMapper.map(operations, OperationsDTO.class);
    }
}