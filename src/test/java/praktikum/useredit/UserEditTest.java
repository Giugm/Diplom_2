package praktikum.useredit;

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

public class UserEditTest {

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

    private String accessToken;

    @Before
    public void createUser() {
        Map<String, String> user = new HashMap<>();
        user.put("email", UserGenerator.randomEmail());
        user.put("password", UserGenerator.randomPassword());
        user.put("name", UserGenerator.randomName());

        accessToken = UserClient.registerUser(user)
                .then().extract().path("accessToken");
    }

    @Test
    public void updateUser_withAuth() {
        Map<String, String> newData = new HashMap<>();
        newData.put("name", "UpdatedName");

        Response response = UserClient.updateUser(newData, accessToken);
        response.then().statusCode(200)
                .body("user.name", equalTo("UpdatedName"));
    }

    @Test
    public void updateUser_withoutAuth() {
        Map<String, String> newData = new HashMap<>();
        newData.put("name", "Hacker");

        Response response = UserClient.updateUser(newData, " ");
        response.then().statusCode(401)
                .body("message", containsString("authorised"));
    }
}


