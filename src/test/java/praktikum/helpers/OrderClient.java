package praktikum.helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.models.OrderPayload;

public class OrderClient {

    private static final String ORDERS_PATH = "/api/orders";

    @Step("Создание заказа с токеном авторизации")
    public static Response createOrder(OrderPayload payload, String token) {
        return ApiClient.post(ORDERS_PATH, payload, token);
    }

    @Step("Создание заказа без авторизации")
    public static Response createOrder(OrderPayload payload) {
        return ApiClient.post(ORDERS_PATH, payload);
    }

    @Step("Получение заказов пользователя")
    public static Response getUserOrders(String token) {
        return ApiClient.get(ORDERS_PATH, token);
    }

    @Step("Получение заказов пользователя без авторизации")
    public static Response getUserOrders() {
        return ApiClient.get(ORDERS_PATH);
    }
}
