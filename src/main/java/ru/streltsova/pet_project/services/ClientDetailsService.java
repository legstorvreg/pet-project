package ru.streltsova.pet_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.streltsova.pet_project.models.Client;
import ru.streltsova.pet_project.repositories.ClientRepository;
import ru.streltsova.pet_project.security.ClientDetails;

import java.util.Optional;

@Service
public class ClientDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Client> client = clientRepository.findByLogin(login);

        if (client.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new ClientDetails(client.get());
    }
}
