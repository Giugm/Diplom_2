package praktikum.helpers;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiClient {
    static {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    public static Response post(String path, Object body) {
        return given().header("Content-Type", "application/json")
                .body(body).post(path);
    }

    public static Response post(String path, Object body, String token) {
        return given().header("Authorization", token)
                .header("Content-Type", "application/json")
                .body(body).post(path);
    }

    public static Response patch(String path, Object body, String token) {
        return given().header("Authorization", token)
                .header("Content-Type", "application/json")
                .body(body).patch(path);
    }

    public static Response get(String path, String token) {
        return given().header("Authorization", token)
                .get(path);
    }

    public static Response delete(String path, String token) {
        return given().header("Authorization", token)
                .delete(path);
    }
}

