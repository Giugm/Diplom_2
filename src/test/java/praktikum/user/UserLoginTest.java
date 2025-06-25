package praktikum.user;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.helpers.UserClient;
import praktikum.helpers.UserGenerator;
import praktikum.models.Credentials;
import praktikum.models.User;

import static org.hamcrest.Matchers.*;

@Epic("Пользователь")
@Feature("Логин: POST /api/auth/login")
public class UserLoginTest {

    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        // Создаем пользователя перед каждым тестом
        user = UserGenerator.randomUser();
        Response registerResponse = UserClient.registerUser(user);
        // Сохраняем токен, чтобы потом удалить пользователя
        accessToken = registerResponse.then().extract().body().path("accessToken");
    }

    @After
    public void tearDown() {
        // Удаляем пользователя после каждого теста
        if (accessToken != null && !accessToken.isEmpty()) {
            UserClient.deleteUser(accessToken);
        }
    }

    @Test
    @Story("Успешный логин")
    @Description("Проверка логина с корректными учетными данными")
    public void loginWithValidCredentialsIsSuccessful() {
        // Создаем объект Credentials из нашего пользователя
        Credentials credentials = Credentials.from(user);
        Response response = UserClient.loginUser(credentials);

        response.then().statusCode(200)
                .and().body("success", is(true))
                .and().body("accessToken", notNullValue())
                .and().body("user.email", equalTo(user.getEmail().toLowerCase()));
    }

    @Test
    @Story("Логин с неверными учетными данными")
    @Description("Проверка логина с неправильным паролем")
    public void loginWithInvalidCredentialsReturnsError() {
        // Используем правильный email, но неверный пароль
        Credentials credentials = new Credentials(user.getEmail(), "wrongpass");
        Response response = UserClient.loginUser(credentials);

        response.then().statusCode(401)
                .and().body("message", equalTo("email or password are incorrect"));
    }
}

