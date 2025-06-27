package praktikum.orders;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.helpers.OrderClient;
import praktikum.helpers.UserClient;
import praktikum.helpers.UserGenerator;
import praktikum.models.OrderPayload;
import praktikum.models.User;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;

@Epic("Заказы")
@Feature("Создание заказа: POST /api/orders")
public class CreateOrderTest {

    private String accessToken;
    private User user;

    // Валидные хеши ингредиентов
    private final String validIngredient1 = "61c0c5a71d1f82001bdaaa6d";
    private final String validIngredient2 = "61c0c5a71d1f82001bdaaa6f";
    // Невалидный хеш
    private final String invalidIngredient = "invalid_hash_ingredient";

    @Before
    public void setUp() {
        // Создаем пользователя и получаем токен
        user = UserGenerator.randomUser();
        Response registerResponse = UserClient.registerUser(user);
        accessToken = registerResponse.then().extract().body().path("accessToken");
    }

    @After
    public void tearDown() {
        // Удаляем пользователя после теста
        if (accessToken != null && !accessToken.isEmpty()) {
            UserClient.deleteUser(accessToken);
        }
    }

    @Test
    @Story("Успешное создание заказа")
    @Description("Проверка создания заказа с валидными ингредиентами и авторизацией")
    @Severity(SeverityLevel.BLOCKER)
    public void createOrderWithAuthAndIngredientsIsSuccessful() {
        OrderPayload orderPayload = new OrderPayload(List.of(validIngredient1, validIngredient2));
        Response response = OrderClient.createOrder(orderPayload, accessToken);
        response.then().statusCode(200).and().body("success", is(true));
    }

    @Test
    @Story("Создание заказа без авторизации")
    @Description("Проверка создания заказа с валидными ингредиентами, но без авторизации")
    @Severity(SeverityLevel.NORMAL)
    public void createOrderWithoutAuthIsSuccessful() {
        OrderPayload orderPayload = new OrderPayload(List.of(validIngredient1));
        Response response = OrderClient.createOrder(orderPayload);
        response.then().statusCode(200).and().body("success", is(true));
    }

    @Test
    @Story("Создание заказа без ингредиентов")
    @Description("Проверка, что нельзя создать заказ без ингредиентов")
    @Severity(SeverityLevel.NORMAL)
    public void createOrderWithoutIngredientsReturnsError() {
        OrderPayload orderPayload = new OrderPayload(Collections.emptyList());
        Response response = OrderClient.createOrder(orderPayload, accessToken);
        response.then().statusCode(400)
                .and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Story("Создание заказа с невалидным хешем ингредиента")
    @Description("Проверка, что система возвращает ошибку при попытке создать заказ с неверным хешем ингредиента")
    @Severity(SeverityLevel.NORMAL)
    public void createOrderWithInvalidIngredientHashReturnsError() {
        OrderPayload orderPayload = new OrderPayload(List.of(invalidIngredient));
        Response response = OrderClient.createOrder(orderPayload, accessToken);
        // Ожидаем 500, так как это ошибка сервера при обработке неверного ID
        response.then().statusCode(500);
    }
}