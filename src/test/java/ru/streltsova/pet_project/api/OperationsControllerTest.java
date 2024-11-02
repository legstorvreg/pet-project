package ru.streltsova.pet_project.api;


import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import ru.streltsova.pet_project.models.Client;
import ru.streltsova.pet_project.models.Operations;
import ru.streltsova.pet_project.services.OperationsService;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class OperationsControllerTest extends BaseApiTest {

    @MockBean
    private OperationsService operationsService;

    @Test
    public void getOperations_positiveCase() {
        Operations operations1 = new Operations();
        operations1.setConversionAmount(new BigDecimal(100));
        Operations operations2 = new Operations();
        operations2.setConversionAmount(new BigDecimal(1000));
        when(operationsService.findAll(any(Client.class))).thenReturn(List.of(operations1, operations2));

        given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/wallet/operations")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2))
                .body("[0].conversionAmount", equalTo(100))
                .body("[1].conversionAmount", equalTo(1000));
    }

    @Test
    public void getOperations_negativeCaseWhenOperationsListIsNull() {
        when(operationsService.findAll(any(Client.class))).thenReturn(List.of());
        Response response = given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/wallet/operations")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("", hasSize(0))
                .extract().response();
    }
}
