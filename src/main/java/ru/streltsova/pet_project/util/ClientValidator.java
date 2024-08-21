package ru.streltsova.pet_project.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.streltsova.pet_project.models.Client;
import ru.streltsova.pet_project.services.ClientDetailsService;

@Component
public class ClientValidator implements Validator {

    private final ClientDetailsService clientDetailsService;

    @Autowired
    public ClientValidator(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Client.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Client client = (Client) o;

        try {
            clientDetailsService.loadUserByUsername(client.getLogin());
        } catch (UsernameNotFoundException ignored) {
            return;
        }

        errors.rejectValue("login", "", "Client with this login already exist");
    }
}
