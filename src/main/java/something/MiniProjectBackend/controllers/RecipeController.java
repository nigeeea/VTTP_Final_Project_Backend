package something.MiniProjectBackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import something.MiniProjectBackend.services.RecipeService;

@Controller
@RequestMapping(path = "/api")
public class RecipeController {

    @Autowired RecipeService recipeSvc;
    

    @GetMapping(path = "/testing")
    public ResponseEntity<String> testing(){

        JsonObject response = Json.createObjectBuilder().add("somethingdeadaedzzz", "ntohing").build();

        return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response.toString());
    }

    @GetMapping(path = "/testdb")
    public ResponseEntity<String> getByName(
        @RequestParam String firstName
    ){

        JsonObject response = recipeSvc.getByName(firstName);

        return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response.toString());
    }
}
