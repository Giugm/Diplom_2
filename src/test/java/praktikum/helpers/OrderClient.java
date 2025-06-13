package praktikum.helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

public class OrderClient {

    @Step("Создание заказа")
    public static Response createOrder(Map<String, Object> body, String token) {
        return ApiClient.post("/api/orders", body, token);
    }

    @Step("Создание заказа без авторизации")
    public static Response createOrder(Map<String, Object> body) {
        return ApiClient.post("/api/orders", body);
    }

    @Step("Получение заказов пользователя")
    public static Response getUserOrders(String token) {
        return ApiClient.get("/api/orders", token);
    }
}

