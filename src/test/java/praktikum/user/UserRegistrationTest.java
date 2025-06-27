package praktikum.user;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import praktikum.helpers.UserClient;
import praktikum.helpers.UserGenerator;
import praktikum.models.User;

import static org.hamcrest.Matchers.equalTo;

@Epic("Пользователь")
@Feature("Регистрация: POST /api/auth/register")
public class UserRegistrationTest {

    private String accessToken;

    @After
    public void tearDown() {
        // Удаляем пользователя, если он был создан
        if (accessToken != null && !accessToken.isEmpty()) {
            UserClient.deleteUser(accessToken);
            accessToken = null; // Сбрасываем токен
        }
    }

    @Test
    @Story("Успешная регистрация")
    @Description("Проверка успешного создания нового уникального пользователя")
    public void registerNewUserIsSuccessful() {
        // Используем POJO User
        User user = UserGenerator.randomUser();

        Response response = UserClient.registerUser(user);
        // Сохраняем токен для последующей очистки
        accessToken = response.then().extract().path("accessToken");

        response.then().statusCode(200)
                .and().body("success", equalTo(true));
    }

    @Test
    @Story("Регистрация существующего пользователя")
    @Description("Проверка, что нельзя создать пользователя, который уже существует")
    public void registerExistingUserReturnsError() {
        // Генерируем случайного пользователя
        User user = UserGenerator.randomUser();

        // Первая (успешная) регистрация
        Response firstResponse = UserClient.registerUser(user);
        accessToken = firstResponse.then().extract().path("accessToken");

        // Повторная регистрация того же пользователя
        Response secondResponse = UserClient.registerUser(user);

        secondResponse.then().statusCode(403)
                .and().body("message", equalTo("User already exists"));
    }

    @Test
    @Story("Регистрация без email")
    @Description("Проверка, что нельзя создать пользователя без email")
    public void registerUserWithoutEmailReturnsError() {
        User user = UserGenerator.randomUser();
        user.setEmail(null); // Устанавливаем email в null

        Response response = UserClient.registerUser(user);

        response.then().statusCode(403)
                .and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Story("Регистрация без пароля")
    @Description("Проверка, что нельзя создать пользователя без пароля")
    public void registerUserWithoutPasswordReturnsError() {
        User user = UserGenerator.randomUser();
        user.setPassword(null); // Устанавливаем пароль в null

        Response response = UserClient.registerUser(user);

        response.then().statusCode(403)
                .and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Story("Регистрация без имени")
    @Description("Проверка, что нельзя создать пользователя без имени")
    public void registerUserWithoutNameReturnsError() {
        User user = UserGenerator.randomUser();
        user.setName(null); // Устанавливаем имя в null

        Response response = UserClient.registerUser(user);

        response.then().statusCode(403)
                .and().body("message", equalTo("Email, password and name are required fields"));
    }
}

