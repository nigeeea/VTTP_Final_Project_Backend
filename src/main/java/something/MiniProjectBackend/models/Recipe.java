package something.MiniProjectBackend.models;

import jakarta.json.JsonObject;

public class Recipe {
    
    //instance variables
    private Integer id;

    private String recipeName;

    private String maxCalories;

    private String image;

    private String url;

    private Integer calories;

    //getter and setters

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecipeName() {
        return this.recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getMaxCalories() {
        return this.maxCalories;
    }

    public void setMaxCalories(String maxCalories) {
        this.maxCalories = maxCalories;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCalories() {
        return this.calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    //method to convert JSONObject to food object with the set instance variables
    public Recipe fromJSONToFood(JsonObject foodJSON){
        Recipe myFood = new Recipe();
        myFood.setRecipeName(foodJSON.getString("recipeName"));
        myFood.setImage(foodJSON.getString("image"));
        myFood.setId(foodJSON.getInt("id"));
        myFood.setUrl(foodJSON.getString("url"));
        myFood.setCalories(foodJSON.getInt("calories"));
        return myFood;
    }
}
