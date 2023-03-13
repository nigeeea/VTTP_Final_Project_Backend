package something.MiniProjectBackend.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.hash.Hashing;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import something.MiniProjectBackend.models.User;
import something.MiniProjectBackend.services.RecipeService;

@RestController
@RequestMapping(path = "/api")
public class RecipeController {

    @Autowired RecipeService recipeSvc;
    

    @GetMapping(path = "/testdb")
    public ResponseEntity<String> getByName(
        @RequestParam String firstName
    ){

        JsonObject response = recipeSvc.getByName(firstName);

        return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response.toString());
    }

    @GetMapping(path = "/testapi")
    public ResponseEntity<String> getRecipe(
        @RequestParam(name = "userInput", defaultValue = "italian") String cuisine,
        @RequestParam(name = "maxCalorieInput", defaultValue = "1000") String calories
    ){

        JsonObject response = recipeSvc.getRecipe(cuisine, calories);

        return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response.toString());

    }

    //METHOD TO REGISTER/SIGNUP USER - METHOD 1
    @PostMapping(path = "/register")
    public ResponseEntity<String> registerUser(
        @RequestParam String username,
        @RequestParam String password
    ){

        //get the params
        //sha256 hash the password
        //create the User DTO
        password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        //insert into mongoDB Users collections
        boolean registrationStatus = recipeSvc.registerUser(newUser);

        //Turn user DTO into JSON to see response
        JsonObject response = Json.createObjectBuilder()
                                    .add("registered", registrationStatus)
                                    .build();
        
        System.out.println(response.toString());

        return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response.toString());
    }

    //METHOD TO AUTHENTICATE USER LOGIN - METHOD 2
    @GetMapping(path = "/userAuth")
    public ResponseEntity<String> authenticateUser(
        @RequestParam String username,
        @RequestParam String password
    ){

        //create the user and send to the db
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        boolean authenticationResponse = recipeSvc.authenticateUser(user);

        //note that the response is a boolean not string
        JsonObject response = Json.createObjectBuilder().add("authenticated", authenticationResponse)
                                    .build();

        //if false then angular will tell user the usernmae or password is wrong
        return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response.toString());
    }
}
