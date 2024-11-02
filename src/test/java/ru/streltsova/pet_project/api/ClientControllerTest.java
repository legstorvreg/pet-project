package ru.streltsova.pet_project.api;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import ru.streltsova.pet_project.models.Client;
import ru.streltsova.pet_project.services.RegistrationService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClientControllerTest extends BaseApiTest {

    @MockBean
    private RegistrationService registrationService;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    public void registration_testSuccessfulRegistration() {
        String clientDTOJson = """
                    {
                        "login": "john.doe2@example.com",
                        "password": "password123"
                    }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(clientDTOJson)
                .log().all()
                .when()
                .post("/auth/registration")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

        verify(registrationService).register(any(Client.class));
    }

    @Test
    public void registration_testBadRequestWhenValidationsFalls() {
        String clientDTOJson = """
                    {
                        "login": "",
                        "password": "password123"
                    }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(clientDTOJson)
                .log().all()
                .when()
                .post("/auth/registration")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void login_testSuccessful() {
        Authentication auth = new UsernamePasswordAuthenticationToken("testuser@mail.ru",
                "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).
                thenReturn(auth);

        String clientDTOJson = """
                    {
                        "login": "testuser@mail.ru",
                        "password": "password"
                    }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(clientDTOJson)
                .log().all()
                .when()
                .post("/auth/login")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Login successful."));
    }

    @Test
    public void login_testInvalidCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));
        String clientDTOJson = """
                    {
                        "login": "wronguser@mail.ru",
                        "password": "wrongpassword"
                    }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(clientDTOJson)
                .log().all()
                .when()
                .post("/auth/login")
                .then()
                .log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .body(equalTo("Invalid credentials"));
    }
}
