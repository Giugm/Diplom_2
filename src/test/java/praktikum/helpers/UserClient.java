package praktikum.helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.models.Credentials;
import praktikum.models.User;

public class UserClient {

    private static final String REGISTER_PATH = "/api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String USER_DATA_PATH = "/api/auth/user";

    @Step("Регистрация пользователя")
    public static Response registerUser(User user) {
        return ApiClient.post(REGISTER_PATH, user);
    }

    @Step("Логин пользователя")
    public static Response loginUser(Credentials credentials) {
        return ApiClient.post(LOGIN_PATH, credentials);
    }

    @Step("Удаление пользователя")
    public static Response deleteUser(String accessToken) {
        // Проверяем, что токен не пустой и не null
        if (accessToken != null && !accessToken.isEmpty()) {
            return ApiClient.delete(USER_DATA_PATH, accessToken);
        }
        // Возвращаем null или кастомный ответ, если токен невалиден
        return null;
    }

    @Step("Изменение данных пользователя")
    public static Response updateUser(User userData, String accessToken) {
        return ApiClient.patch(USER_DATA_PATH, userData, accessToken);
    }
}
