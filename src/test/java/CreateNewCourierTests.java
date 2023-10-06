import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateNewCourierTests {

    private static String lastLogin = "";
    private static String lastPassword = "";

    public static String newLogin() {
        lastLogin  = RandomStringUtils.randomAlphanumeric(10);
        return lastLogin;
    }

    public static String newPassword() {
        lastPassword  = RandomStringUtils.randomAlphanumeric(10);
        return lastPassword;
    }

    @BeforeClass
    public static void log() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/courier";
    }

    @Step("Create new courier (Send POST request)")
    public Response sendPostRequestCreateNewCourier(String json){
        Response response =given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post();
        return response;
    }

    @Step("Check status code and body")
    public void checkStatusCode201(Response response){
        response.then().assertThat().statusCode(201).and().assertThat().body("ok", equalTo(true));
    }

    @Step("Check status code and body")
    public void checkStatusCode409(Response response){
        response.then().assertThat().statusCode(409).and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Check status code and body")
    public void checkStatusCode400(Response response){
        response.then().assertThat().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Description("Always create original login for new courier. Check response status code and availability body \"ok: true\"")
    @DisplayName("Create new courier")
    public void createNewCourier() {
        String json = String.format("{\"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"Test\"}", newLogin(), newPassword());
        Response response = sendPostRequestCreateNewCourier(json);
        checkStatusCode201(response);
    }

    @Test
    @Description("Use existing login (status code should be 409 and message \"Этот логин уже используется. Попробуйте другой.\")")
    @DisplayName("Create new courier with exiting login")
    public void createNewCourierWithTheSameName() {
        createNewCourier();
        Courier courier = new Courier(lastLogin, lastPassword, "Test");
        Response response = sendPostRequestCreateNewCourier(courier.toJson());
        checkStatusCode409(response);
    }

    @Test
    @Description("Without login (status code should be 400 and message \"Недостаточно данных для создания учетной записи\")")
    @DisplayName("Create new courier without field login")
    public void createNewCourierWithoutLogin() {
        Courier courier = new Courier("", newPassword(), "Test");
        Response response = sendPostRequestCreateNewCourier(courier.toJson());
        checkStatusCode400(response);
    }

    @Test
    @Description("Always create original login for new courier (status code should be 400 and message \"Недостаточно данных для создания учетной записи\")")
    @DisplayName("Create new courier without field password")
    public void createNewCourierWithoutPassword() {
        Courier courier = new Courier(newLogin(), "", "Test");
        Response response = sendPostRequestCreateNewCourier(courier.toJson());
        checkStatusCode400(response);
    }

    @Test
    @Description("Always create original login (status code should be 201)")
    @DisplayName("Create new courier without field firstName")
    public void createNewCourierWithoutFirstName() {
        Courier courier = new Courier(newLogin(), newPassword(), "");
        Response response = sendPostRequestCreateNewCourier(courier.toJson());
        checkStatusCode201(response);
    }
}
