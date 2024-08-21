package ru.streltsova.pet_project.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.streltsova.pet_project.dto.ClientDTO;
import ru.streltsova.pet_project.models.Client;
import ru.streltsova.pet_project.services.RegistrationService;
import ru.streltsova.pet_project.util.ClientValidator;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class ClientController {

    private final AuthenticationManager authenticationManager;
    private final ClientValidator clientValidator;
    private final RegistrationService registrationService;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientController(AuthenticationManager authenticationManager,
                            ClientValidator clientValidator,
                            RegistrationService registrationService,
                            ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.clientValidator = clientValidator;
        this.registrationService = registrationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid ClientDTO dto,
                                                   BindingResult bindingResult) {
        Client client = convertToClient(dto);

        clientValidator.validate(client, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        registrationService.register(client);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid ClientDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok("Login successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    public Client convertToClient(ClientDTO dto) {
        return modelMapper.map(dto, Client.class);
    }
}
