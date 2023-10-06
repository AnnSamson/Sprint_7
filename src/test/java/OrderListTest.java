import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {
    @BeforeClass
    public static void log() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/orders";
    }

    @Test
    @Description("Check response status code and availability orders list")
    @DisplayName("Get all orders list without all fields")
    public void getOrderList() {
        String json = "{\"courierId\": \"\", \"nearestStation\": \"\", \"limit\": \"\", \"page\": \"\"}";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .get()
                .then().statusCode(200)
                .and()
                .assertThat().body("orders", notNullValue());
    }
}
