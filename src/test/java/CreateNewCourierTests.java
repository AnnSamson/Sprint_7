import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

public class CreateNewCourierTests extends BaseTest {

    @Test
    @Description("Always create original login for new courier. Check response status code and availability body \"ok: true\"")
    @DisplayName("Create new courier")
    public void createNewCourier() {
        String login = CourierApi.newLogin();
        String password = CourierApi.newPassword();
        Courier courier = new Courier(login, password, "Test");
        Response response = CourierApi.sendPostRequestCreateNewCourier(courier.toJson());
        CourierApi.checkStatusCode201(response);
        Gson g = new Gson();
        String loginResponse = CourierApi.loginCourier(login, password).getBody().asString();
        int courierId = g.fromJson(loginResponse, CourierApi.CourierResponse.class).id;
        CourierApi.deleteCourier(courierId);
    }

    @Test
    @Description("Use existing login (status code should be 409 and message \"Этот логин уже используется. Попробуйте другой.\")")
    @DisplayName("Create new courier with exiting login")
    public void createNewCourierWithTheSameName() {
        String login = CourierApi.newLogin();
        String password = CourierApi.newPassword();
        Courier courier = new Courier(login, password, "Test");
        CourierApi.sendPostRequestCreateNewCourier(courier.toJson());
        Response response = CourierApi.sendPostRequestCreateNewCourier(courier.toJson());
        CourierApi.checkStatusCode409(response);
        Gson g = new Gson();
        String loginResponse = CourierApi.loginCourier(login, password).getBody().asString();
        int courierId = g.fromJson(loginResponse, CourierApi.CourierResponse.class).id;
        CourierApi.deleteCourier(courierId);
    }

    @Test
    @Description("Without login (status code should be 400 and message \"Недостаточно данных для создания учетной записи\")")
    @DisplayName("Create new courier without field login")
    public void createNewCourierWithoutLogin() {
        Courier courier = new Courier("", CourierApi.newPassword(), "Test");
        Response response = CourierApi.sendPostRequestCreateNewCourier(courier.toJson());
        CourierApi.checkStatusCode400(response);
    }

    @Test
    @Description("Always create original login for new courier (status code should be 400 and message \"Недостаточно данных для создания учетной записи\")")
    @DisplayName("Create new courier without field password")
    public void createNewCourierWithoutPassword() {
        Courier courier = new Courier(CourierApi.newLogin(), "", "Test");
        Response response = CourierApi.sendPostRequestCreateNewCourier(courier.toJson());
        CourierApi.checkStatusCode400(response);
    }

    @Test
    @Description("Always create original login (status code should be 201)")
    @DisplayName("Create new courier without field firstName")
    public void createNewCourierWithoutFirstName() {
        String login = CourierApi.newLogin();
        String password = CourierApi.newPassword();
        Courier courier = new Courier(login, password, "");
        Response response = CourierApi.sendPostRequestCreateNewCourier(courier.toJson());
        CourierApi.checkStatusCode201(response);
        Gson g = new Gson();
        String loginResponse = CourierApi.loginCourier(login, password).getBody().asString();
        int courierId = g.fromJson(loginResponse, CourierApi.CourierResponse.class).id;
        CourierApi.deleteCourier(courierId);
    }
}
