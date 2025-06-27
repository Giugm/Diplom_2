package praktikum.models;

import java.util.List;

// POJO класс для тела запроса на создание заказа
public class OrderPayload {
    private List<String> ingredients;

    public OrderPayload(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    // Геттеры и сеттеры
    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
