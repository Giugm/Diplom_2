package praktikum.helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

public class UserClient {

    @Step("Регистрация пользователя")
    public static Response registerUser(Map<String, String> user) {
        return ApiClient.post("/api/auth/register", user);
    }

    @Step("Логин пользователя")
    public static Response loginUser(Map<String, String> credentials) {
        return ApiClient.post("/api/auth/login", credentials);
    }

    @Step("Удаление пользователя")
    public static Response deleteUser(String token) {
        return ApiClient.delete("/api/auth/user", token);
    }

    @Step("Изменение пользователя")
    public static Response updateUser(Map<String, String> userData, String token) {
        return ApiClient.patch("/api/auth/user", userData, token);
    }
}
