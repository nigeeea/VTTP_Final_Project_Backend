package something.MiniProjectBackend.services;

import java.io.Reader;
import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import something.MiniProjectBackend.models.User;
import something.MiniProjectBackend.repositories.RecipeRepository;

@Service
public class RecipeService {

    private static final String URL = "https://api.spoonacular.com/recipes/complexSearch";

    @Value("${API_KEY}")
    private String key;
    
    @Autowired RecipeRepository recipeRepo;

    public JsonObject getByName(String firstName){
        return recipeRepo.getByName(firstName);
    }

    //METHOD TO REGISTER/SIGNUP USER - METHOD 1
    public boolean registerUser(User user){
        return recipeRepo.registerUser(user);
    }


    //METHOD TO AUTHENTICATE USER LOGIN - METHOD 2
    public boolean authenticateUser(User user){
        return recipeRepo.authenticateUser(user);
    }


    //METHOD TO CALL API
    public JsonObject getRecipe(String input, String maxCalInput){

        String url = UriComponentsBuilder.fromUriString(URL)
        .queryParam("cuisine", input)
        .queryParam("number", "7") //hardset to 10 due to API call limitss...
        .queryParam("maxCalories", maxCalInput)
        .queryParam("apiKey", key)
        .queryParam("instructionsRequired", "true")
        .queryParam("addRecipeInformation", "true")
        .toUriString();

        //create the GET Request
        RequestEntity<Void> req = RequestEntity.get(url).build();

        //Make the call to the API
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);

        //Check the response
        System.out.println(resp.getStatusCode());
        String payload = resp.getBody();
        System.out.println(payload);

        //convert the payload into a JSONObject
        Reader myStrReader = new StringReader(payload);
        JsonReader myJsonReader = Json.createReader(myStrReader);
        JsonObject initialJsonObject = myJsonReader.readObject();
        JsonArray initialJsonArray = initialJsonObject.getJsonArray("results");

        //this method is to get a random recipe out of the results returned (can get from the size of the JSON Array)
        int randomNumber = (int)(Math.random() * initialJsonArray.size() + 1);

        //get the recipe calories
        JsonObject initialJsonCaloriesObject = initialJsonArray.getJsonObject(randomNumber).getJsonObject("nutrition");
        Integer recipeCalories = initialJsonCaloriesObject.getJsonArray("nutrients").getJsonObject(0).getInt("amount");

        //create a new JsonObject to contain the information that i want
        JsonObjectBuilder myJsonObjectBuilder = Json.createObjectBuilder();
        myJsonObjectBuilder
        .add("id", initialJsonArray.getJsonObject(randomNumber).getInt("id"))
        .add("image", initialJsonArray.getJsonObject(randomNumber).getString("image"))
        .add("recipeName", initialJsonArray.getJsonObject(randomNumber).getString("title"))
        .add("url", initialJsonArray.getJsonObject(randomNumber).getString("spoonacularSourceUrl"))
        .add("calories", recipeCalories);
        
        JsonObject myJsonObject = myJsonObjectBuilder.build();

        return myJsonObject;
        // //take values from myJsonObject and add them to a Food object then add the Food object to a list
        // Recipe myFood = new Recipe();
        // myFood = myFood.fromJSONToFood(myJsonObject);
        
        // List<Recipe> myFoodList = new ArrayList<>();
        // myFoodList.add(myFood);

        // System.out.println(">>>>>>>>>>>>>>"+myJsonObject.getString("recipeName")+">>>>>>>>>>>>>>");

        // return myFood;
    }
}
