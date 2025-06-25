package praktikum.orders;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.helpers.OrderClient;
import praktikum.helpers.UserClient;
import praktikum.helpers.UserGenerator;
import praktikum.models.OrderPayload;
import praktikum.models.User;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Заказы")
@Feature("Получение заказов пользователя: GET /api/orders")
public class GetOrdersTest {

    private String accessToken;
    private User user;

    @Before
    public void setUp() {
        // Создаем пользователя
        user = UserGenerator.randomUser();
        Response registerResponse = UserClient.registerUser(user);
        accessToken = registerResponse.then().extract().body().path("accessToken");

        // Создаем для него заказ, чтобы было что получать
        OrderPayload orderPayload = new OrderPayload(List.of("61c0c5a71d1f82001bdaaa6d"));
        OrderClient.createOrder(orderPayload, accessToken);
    }

    @After
    public void tearDown() {
        // Удаляем пользователя после теста
        if (accessToken != null && !accessToken.isEmpty()) {
            UserClient.deleteUser(accessToken);
        }
    }

    @Test
    @Story("Получение заказов авторизованным пользователем")
    @Description("Проверка, что авторизованный пользователь может получить список своих заказов")
    public void getUserOrdersWithAuthIsSuccessful() {
        Response response = OrderClient.getUserOrders(accessToken);
        response.then().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("orders", notNullValue());
    }

    @Test
    @Story("Получение заказов без авторизации")
    @Description("Проверка, что неавторизованный пользователь не может получить список заказов")
    public void getUserOrdersWithoutAuthReturnsError() {
        Response response = OrderClient.getUserOrders(); // Вызываем метод без токена
        response.then().statusCode(401)
                .and().body("message", equalTo("You should be authorised"));
    }
}
