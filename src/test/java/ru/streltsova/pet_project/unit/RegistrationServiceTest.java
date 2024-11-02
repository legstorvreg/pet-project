package ru.streltsova.pet_project.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.streltsova.pet_project.repositories.ClientRepository;
import ru.streltsova.pet_project.services.RegistrationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest extends BaseUnitTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void register_clientSaveTest() {
        when(passwordEncoder.encode(client.getPassword())).thenReturn("encodedPassword");

        registrationService.register(client);

        assertEquals("encodedPassword", client.getPassword());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void register_shouldNotSaveTheClient() {
        when(passwordEncoder.encode(client.getPassword())).thenThrow(new RuntimeException("Password encoding failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> registrationService.register(client));

        assertEquals("Password encoding failed", exception.getMessage());
        verify(clientRepository, never()).save(client);
    }
}
