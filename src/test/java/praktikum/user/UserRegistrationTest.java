package praktikum.user;

import io.qameta.allure.junit4.AllureJunit4;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import praktikum.helpers.UserClient;
import praktikum.helpers.UserGenerator;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class UserRegistrationTest {
    String accessToken;

    @Rule
    public TestWatcher allure = new TestWatcher() {
        private final AllureJunit4 delegate = new AllureJunit4();

        @Override
        protected void starting(Description description) {
            delegate.testStarted(description);
            super.starting(description);
        }

        @Override
        protected void succeeded(Description description) {
            delegate.testFinished(description);
            super.succeeded(description);
        }

        @Override
        protected void failed(Throwable e, Description description) {
            delegate.testFailure(new Failure(description, e));
            super.failed(e, description);
        }
    };

    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken);
        }
    }

    @Test
    public void createNewUser_success() {
        Map<String, String> user = new HashMap<>();
        user.put("email", UserGenerator.randomEmail());
        user.put("password", UserGenerator.randomPassword());
        user.put("name", UserGenerator.randomName());

        Response response = UserClient.registerUser(user);
        accessToken = response.then().extract().path("accessToken");

        response.then().statusCode(200)
                .body("success", is(true));
    }

    @Test
    public void createUser_existingUser() {
        Map<String, String> user = new HashMap<>();
        user.put("email", "test@test.com");
        user.put("password", "123456");
        user.put("name", "Test");

        UserClient.registerUser(user); // регистрация 1 раз
        Response response = UserClient.registerUser(user); // повторная регистрация

        response.then().statusCode(403)
                .body("message", containsString("already exists"));
    }

    @Test
    public void createUser_missingField() {
        Map<String, String> user = new HashMap<>();
        user.put("email", UserGenerator.randomEmail());
        user.put("password", ""); // отсутствие обязательного поля

        Response response = UserClient.registerUser(user);

        response.then().statusCode(403)
                .body("message", containsString("required fields"));
    }
}


