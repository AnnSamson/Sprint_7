import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.empty;

public class OrderApi {

    @Step("Create new order (Send POST request)")
    public static Response sendPostRequestCreateNewOrder(Object order){
        RestAssured.basePath = "/api/v1/orders";
        Response response =given()
                .header("Content-type", "application/json")
                .body(order)
                .and()
                .when()
                .post();
        return response;
    }

    public static Response getOrderList() {
        RestAssured.basePath = "/api/v1/orders";
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("courierId", "");
        jsonMap.put("nearestStation", "");
        jsonMap.put("limit", "");
        jsonMap.put("page", "");
        try {
            String json = objectMapper.writeValueAsString(jsonMap);
            return given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(json)
                    .when()
                    .get();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Step("Check status code")
    public static void checkStatusCode(Response response, int code){
        response.then().assertThat().statusCode(code);
    }

    @Step("Check body contains track")
    public static void checkBodyContainsTrack(Response response){
        response.then().assertThat().body("track",notNullValue());
    }

    @Step("Check body contains orders")
    public static void checkBodyContainsOrders(Response response){
        response.then().assertThat().body("orders",  is(not(empty())));
    }

}
