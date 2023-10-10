import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierApi {

    public class CourierResponse {
        int id;
    }

    public static String newLogin() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static String newPassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    @Step("Create new courier (Send POST request)")
    public static Response sendPostRequestCreateNewCourier(String json){
        RestAssured.basePath = "/api/v1/courier";
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post();
    }

    public static Response loginCourier(String login, String password) {
        RestAssured.basePath = "/api/v1/courier/login";
        Courier courier = new Courier(login, password, "Test");
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier.toJson())
                .when()
                .post();
    }

    public static Response deleteCourier(int id) {
        RestAssured.basePath = "/api/v1/courier/" + id;
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("id", String.valueOf(id));
        try {
            String json = objectMapper.writeValueAsString(jsonMap);
            return given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(json)
                    .when()
                    .delete();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Step("Check status code and body")
    public static void checkStatusCode(Response response, int code){
        response.then().assertThat().statusCode(code);
    }

    @Step("Check status code and body")
    public static void checkStatusCode201(Response response){
        response.then().assertThat().statusCode(201).and().assertThat().body("ok", equalTo(true));
    }

    @Step("Check status code and body")
    public static void checkStatusCode409(Response response){
        response.then().assertThat().statusCode(409).and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Check status code and body")
    public static void checkStatusCode400(Response response){
        response.then().assertThat().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Check status code and body")
    public static void checkStatusCode404(Response response){
        response.then().statusCode(404).and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Check status code and body")
    public static void checkBodyContainsId(Response response){
        response.then().assertThat().body("id", notNullValue());
    }
}
