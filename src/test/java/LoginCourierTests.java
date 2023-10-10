import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

public class LoginCourierTests extends BaseTest {

    @Test
    @Description("Check response status code and availability body id")
    @DisplayName("Authorization registered courier")
    public void loginCourierCorrectly() {
        String login = CourierApi.newLogin();
        String password = CourierApi.newPassword();
        Courier courier = new Courier(login, password, "Test");
        CourierApi.sendPostRequestCreateNewCourier(courier.toJson());
        Response loginResponse = CourierApi.loginCourier(login, password);
        CourierApi.checkStatusCode(loginResponse, 200);
        CourierApi.checkBodyContainsId(loginResponse);
        Gson g = new Gson();
        String loginResponseStr = loginResponse.getBody().asString();
        int courierId = g.fromJson(loginResponseStr, CourierApi.CourierResponse.class).id;
        CourierApi.deleteCourier(courierId);
    }

    @Test
    @Description("Check response status code (it should be 404) and message (\"Учетная запись не найдена\")")
    @DisplayName("Authorization courier with non-existent login")
    public void loginCourierNonExistentName() {
        Response response = CourierApi.loginCourier(CourierApi.newLogin(), CourierApi.newPassword());
        CourierApi.checkStatusCode404(response);
    }

    @Test
    @Description("Check response status code (it should be 404) and message (\"Учетная запись не найдена\")")
    @DisplayName("Authorization courier with wrong password")
    public void loginCourierWithWrongPassword() {
        String login = CourierApi.newLogin();
        String password = CourierApi.newPassword();
        Courier courier = new Courier(login, password, "Test");
        CourierApi.sendPostRequestCreateNewCourier(courier.toJson());
        Response response = CourierApi.loginCourier(login, CourierApi.newPassword());
        CourierApi.checkStatusCode404(response);
        Gson g = new Gson();
        String loginResponse = CourierApi.loginCourier(login, password).getBody().asString();
        int courierId = g.fromJson(loginResponse, CourierApi.CourierResponse.class).id;
        CourierApi.deleteCourier(courierId);
    }

    @Test
    @Description("Check response status code (it should be 400) and message")
    @DisplayName("Authorization courier without field login")
    public void loginCourierWithoutLogin() {
        Response response = CourierApi.loginCourier("", CourierApi.newPassword());
        CourierApi.checkStatusCode(response, 400);
    }
}
