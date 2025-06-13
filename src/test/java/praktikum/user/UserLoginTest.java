package praktikum.user;

import io.qameta.allure.junit4.AllureJunit4;
import io.restassured.response.Response;
import org.junit.Before;
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

public class UserLoginTest {

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

    private String email;
    private String password;

    @Before
    public void setUp() {
        email = UserGenerator.randomEmail();
        password = UserGenerator.randomPassword();

        Map<String, String> user = new HashMap<>();
        user.put("email", email);
        user.put("password", password);
        user.put("name", UserGenerator.randomName());

        UserClient.registerUser(user);
    }

    @Test
    public void login_success() {
        Map<String, String> creds = new HashMap<>();
        creds.put("email", email);
        creds.put("password", password);

        Response response = UserClient.loginUser(creds);
        response.then().statusCode(200)
                .body("accessToken", notNullValue())
                .body("user.email", equalTo(email));
    }

    @Test
    public void login_invalidCredentials() {
        Map<String, String> creds = new HashMap<>();
        creds.put("email", email);
        creds.put("password", "wrongpass");

        Response response = UserClient.loginUser(creds);
        response.then().statusCode(401)
                .body("message", containsString("incorrect"));
    }
}


