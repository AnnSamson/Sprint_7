import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateNewOrderTests<firstName, lastName, address, metroStation, phone, color, comment, deliveryDate, rentTime> {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;

    public CreateNewOrderTests(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name = "firstName = {0}, lastName = {1}, address = {2}, metroStation = {3}, phone = {4}, rentTime = {5}, " +
            "deliveryDate = {6}, comment = {7}, color = {8}")

    public static Object[][] getData() {
        return new Object[][]{
                {"Иван", "Иванов", "Дунайский, 5, 45", "1", "+7 921 777 77 77", 1, "2022-12-12", "Comment1", List.of("BLACK", "GREY")},
                {"Петр", "Петоров", "Среднерогатская, 20, 450", "14", "+7 911 111 11 11", 3, "2022-12-17", "Comment2", Arrays.asList("BLACK")},
                {"Тест", "Тестов", "Петрововка, 38, 13", "101", "+7 901 999 99 99", 7, "2022-12-25", "Comment3", null},
        };
    }

    @BeforeClass
    public static void log() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/orders";
    }

    @Step("Create new order (Send POST request)")
    public Response sendPostRequestCreateNewOrder(Object order){
        Response response =given()
                .header("Content-type", "application/json")
                .body(order)
                .and()
                .when()
                .post();
        return response;
    }

    @Step("Check status code")
    public void checkStatusCode(Response response){
        response.then().assertThat().statusCode(201);
    }

    @Step("Check body contains track")
    public void checkBodyContainsTrack(Response response){
        response.then().assertThat().body("track",notNullValue());
    }

    @Test
    @Description("Check response status code and availability track")
    @DisplayName("Create new orders")
    public void createNewOrders() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = sendPostRequestCreateNewOrder(order);
        checkStatusCode(response);
        checkBodyContainsTrack(response);
    }
}