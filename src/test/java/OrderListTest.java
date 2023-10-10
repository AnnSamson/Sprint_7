import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

public class OrderListTest extends BaseTest {


    @Test
    @Description("Check response status code and availability orders list")
    @DisplayName("Get all orders list without all fields")
    public void getOrderList() {
        Response response = OrderApi.getOrderList();
        OrderApi.checkStatusCode(response, 200);
        OrderApi.checkBodyContainsOrders(response);
    }
}
