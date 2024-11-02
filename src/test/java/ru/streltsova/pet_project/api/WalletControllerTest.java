package ru.streltsova.pet_project.api;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import ru.streltsova.pet_project.dto.ConversionResultDTO;
import ru.streltsova.pet_project.enums.CurrencyEnum;
import ru.streltsova.pet_project.services.ConversionService;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class WalletControllerTest extends BaseApiTest {

    @MockBean
    private ConversionService conversionService;

    @Test
    public void doConversion_ShouldReturnConversionResult_WhenValidParameters() {
        ConversionResultDTO mockResult = new ConversionResultDTO();
        mockResult.setConversionAmount(BigDecimal.TEN);
        mockResult.setCurrencyRate(BigDecimal.ONE);
        mockResult.setFrom(CurrencyEnum.USD);
        mockResult.setTo(CurrencyEnum.EUR);
        mockResult.setOriginalAmount(new BigDecimal("100"));

        when(conversionService.doConversion(any(CurrencyEnum.class), any(CurrencyEnum.class), any(BigDecimal.class)))
                .thenReturn(mockResult);

        given()
                .contentType(ContentType.JSON)
                .queryParam("from", "USD")
                .queryParam("to", "EUR")
                .queryParam("amount", "100")
                .log().all()
                .when()
                .get("/wallet/conversion")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value()) // Expect 200 OK
                .body("from", equalTo("USD"))
                .body("to", equalTo("EUR"))
                .body("originalAmount", equalTo(100))
                .body("conversionAmount", equalTo(10))
                .body("currencyRate", equalTo(1));
    }

    @Test
    public void doConversion_ShouldReturnBadRequest_whenParametersIncorrect() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("from", "USD")
                .queryParam("to", "EUR")
                .log().all()
                .when()
                .get("/wallet/conversion")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addMoney_positiveCaseWhenJsonDataIsCorrect() {
        String walletDTOJson = """
                    {
                        "amount": 100,
                        "currency": "USD"
                    }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(walletDTOJson)
                .when()
                .post("/wallet/addMoney")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void addMoney_negativeCaseWithIncorrectJsonData() {
        String walletDTOJson = """
                    {
                        "amount": -100,
                        "currency": "USD"
                    }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(walletDTOJson)
                .when()
                .post("/wallet/addMoney")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void addMoney_negativeCaseWithIncorrectJsonWnehAmountIsNull() {
        String walletDTOJson = """
                    {
                        "amount": ,
                        "currency": "USD"
                    }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(walletDTOJson)
                .when()
                .post("/wallet/addMoney")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void buyMoney_PositiveCaseWhenJsonDataIsCorrect() {
        String conversionRequestDTO = """
                {
                "from":"USD",
                "to":"EUR",
                "amount":100
                }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(conversionRequestDTO)
                .when()
                .post("/wallet/buy")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void buyMoney_NegativeCaseWhenjsonDataIncorrectCurrencyEnumIsNull() {
        String conversionRequestDTO = """
                {
                "from":"",
                "to":"EUR",
                "amount":100
                }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(conversionRequestDTO)
                .when()
                .post("/wallet/buy")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void buyMoney_NegativeCaseWhenjsonDataIncorrectBigDecimalNotPositive() {
        String conversionRequestDTO = """
                {
                "from":"USD",
                "to":"EUR",
                "amount":-100
                }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(conversionRequestDTO)
                .when()
                .post("/wallet/buy")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
