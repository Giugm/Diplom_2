package praktikum.orders;

import io.qameta.allure.*;
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

import java.util.*;

import static org.hamcrest.Matchers.*;

@Epic("Создание заказов")
@Feature("POST /orders")
@Owner("qa.team")
public class CreateOrderTest {

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
    private final String validIngredient = "61c0c5a71d1f82001bdaaa6d";
    private final String invalidIngredient = "invalid";

    @Before
    public void setUp() {
        Map<String, String> user = generateTestUser();
        accessToken = UserClient.registerUser(user).then().extract().path("accessToken");
    }

    @Test
    @Story("Создание заказа с авторизацией")
    @Severity(SeverityLevel.BLOCKER)
    public void createOrder_withAuth() {
        Map<String, Object> body = createOrderBody(validIngredient);
        Response response = OrderClient.createOrder(body, accessToken);
        response.then().statusCode(200).body("success", is(true));
    }

    @Test
    @Story("Создание заказа без авторизации")
    @Severity(SeverityLevel.CRITICAL)
    public void createOrder_withoutAuth() {
        Map<String, Object> body = createOrderBody(validIngredient);
        Response response = OrderClient.createOrder(body);
        response.then().statusCode(200).body("success", is(true));
    }

    @Test
    @Story("Создание заказа без ингредиентов")
    @Severity(SeverityLevel.NORMAL)
    public void createOrder_withoutIngredients() {
        Map<String, Object> body = new HashMap<>();
        body.put("ingredients", new ArrayList<>());
        Response response = OrderClient.createOrder(body, accessToken);
        response.then().statusCode(400)
                .body("message", containsString("must be provided"));
    }

    @Test
    @Story("Создание заказа с невалидным ингредиентом")
    @Severity(SeverityLevel.MINOR)
    public void createOrder_withInvalidIngredient() {
        Map<String, Object> body = createOrderBody(invalidIngredient);
        Response response = OrderClient.createOrder(body, accessToken);
        response.then().statusCode(500);
    }

    @Step("Генерация случайного пользователя")
    private Map<String, String> generateTestUser() {
        Map<String, String> user = new HashMap<>();
        user.put("email", UserGenerator.randomEmail());
        user.put("password", UserGenerator.randomPassword());
        user.put("name", UserGenerator.randomName());
        return user;
    }

    @Step("Создание тела запроса с ингредиентом: {ingredient}")
    private Map<String, Object> createOrderBody(String ingredient) {
        Map<String, Object> body = new HashMap<>();
        body.put("ingredients", List.of(ingredient));
        return body;
    }
}

