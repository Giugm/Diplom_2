package praktikum.user; // Помещаем в тот же пакет для консистентности

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
import praktikum.models.User;

import static org.hamcrest.Matchers.equalTo;

@Epic("Пользователь")
@Feature("Изменение данных: PATCH /api/auth/user")
public class UserEditTest {

    private String accessToken;
    private String accessTokenForSecondUser;
    private User user;

    @Before
    public void setUp() {
        // Создаем пользователя и получаем токен
        user = UserGenerator.randomUser();
        Response registerResponse = UserClient.registerUser(user);
        accessToken = registerResponse.then().extract().body().path("accessToken");
    }

    @After
    public void tearDown() {
        // Удаляем пользователей после теста
        if (accessToken != null && !accessToken.isEmpty()) {
            UserClient.deleteUser(accessToken);
        }
        if (accessTokenForSecondUser != null && !accessTokenForSecondUser.isEmpty()) {
            UserClient.deleteUser(accessTokenForSecondUser);
        }
    }

    @Test
    @Story("Изменение имени авторизованным пользователем")
    @Description("Проверка успешного изменения имени пользователя с авторизацией")
    public void updateUserNameWithAuthIsSuccessful() {
        String newName = UserGenerator.randomName();
        User updatedUserData = new User(null, null, newName);

        Response response = UserClient.updateUser(updatedUserData, accessToken);

        response.then().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("user.name", equalTo(newName));
    }

    @Test
    @Story("Изменение email авторизованным пользователем")
    @Description("Проверка успешного изменения email пользователя с авторизацией")
    public void updateUserEmailWithAuthIsSuccessful() {
        String newEmail = UserGenerator.randomEmail();
        User updatedUserData = new User(newEmail, null, null);

        Response response = UserClient.updateUser(updatedUserData, accessToken);

        response.then().statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("user.email", equalTo(newEmail.toLowerCase()));
    }

    @Test
    @Story("Изменение данных без авторизации")
    @Description("Проверка, что данные пользователя нельзя изменить без токена")
    public void updateUserWithoutAuthReturnsError() {
        User updatedUserData = new User(null, null, "HackerName");

        // Передаем пустой токен
        Response response = UserClient.updateUser(updatedUserData, "");

        response.then().statusCode(401)
                .and().body("message", equalTo("You should be authorised"));
    }

    @Test
    @Story("Изменение email на уже существующий")
    @Description("Проверка, что система вернет ошибку при попытке изменить email на уже существующий")
    public void updateUserEmailToExistingEmailReturnsError() {
        // Создаем второго пользователя, чей email мы "украдем"
        User secondUser = UserGenerator.randomUser();
        Response registerResponse = UserClient.registerUser(secondUser);
        accessTokenForSecondUser = registerResponse.then().extract().path("accessToken");

        // Пытаемся первому пользователю установить email второго
        User updatedUserData = new User(secondUser.getEmail(), null, null);
        Response response = UserClient.updateUser(updatedUserData, accessToken);

        response.then().statusCode(403)
                .and().body("message", equalTo("User with such email already exists"));
    }
}