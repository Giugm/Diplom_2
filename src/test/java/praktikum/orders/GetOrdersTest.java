package praktikum.orders;

import io.qameta.allure.junit4.AllureJunit4;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import praktikum.helpers.OrderClient;
import praktikum.helpers.UserClient;
import praktikum.helpers.UserGenerator;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class GetOrdersTest {

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
    public void setUp() {
        Map<String, String> user = new HashMap<>();
        user.put("email", UserGenerator.randomEmail());
        user.put("password", UserGenerator.randomPassword());
        user.put("name", UserGenerator.randomName());

        accessToken = UserClient.registerUser(user).then().extract().path("accessToken");
    }

    @Test
    public void getUserOrders_withAuth() {
        Response response = OrderClient.getUserOrders(accessToken);
        response.then().statusCode(200).body("success", is(true));
    }

    @Test
    public void getUserOrders_withoutAuth() {
        Response response = OrderClient.getUserOrders(" ");
        response.then().statusCode(401).body("message", containsString("authorised"));
    }
}


