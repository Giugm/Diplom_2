package praktikum.helpers;

import praktikum.models.User;
import java.util.UUID;

public class UserGenerator {
    public static String randomEmail() {
        return "user_" + UUID.randomUUID() + "@test.com";
    }

    public static String randomPassword() {
        return "pass" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String randomName() {
        return "User_" + UUID.randomUUID().toString().substring(0, 5);
    }


    public static User randomUser() {
        return new User(randomEmail(), randomPassword(), randomName());
    }
}