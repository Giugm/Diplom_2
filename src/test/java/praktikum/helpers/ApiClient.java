package praktikum.helpers;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ApiClient {

    static {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    // Базовый метод для подготовки спецификации запроса
    private static RequestSpecification prepareRequest() {
        return given().header("Content-Type", "application/json");
    }

    // POST запрос без авторизации
    public static Response post(String path, Object body) {
        return prepareRequest()
                .body(body)
                .post(path);
    }

    // POST запрос с авторизацией
    public static Response post(String path, Object body, String token) {
        return prepareRequest()
                .header("Authorization", token)
                .body(body)
                .post(path);
    }

    // PATCH запрос с авторизацией
    public static Response patch(String path, Object body, String token) {
        return prepareRequest()
                .header("Authorization", token)
                .body(body)
                .patch(path);
    }

    // PATCH запрос без авторизации
    public static Response patch(String path, Object body) {
        return prepareRequest()
                .body(body)
                .patch(path);
    }

    // GET запрос с авторизацией
    public static Response get(String path, String token) {
        return prepareRequest()
                .header("Authorization", token)
                .get(path);
    }

    // GET запрос без авторизации
    public static Response get(String path) {
        return prepareRequest()
                .get(path);
    }

    // DELETE запрос с авторизацией
    public static Response delete(String path, String token) {
        return prepareRequest()
                .header("Authorization", token)
                .delete(path);
    }
}
