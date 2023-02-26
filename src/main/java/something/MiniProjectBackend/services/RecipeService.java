package something.MiniProjectBackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.JsonObject;
import something.MiniProjectBackend.repositories.RecipeRepository;

@Service
public class RecipeService {
    
    @Autowired RecipeRepository recipeRepo;

    public JsonObject getByName(String firstName){
        return recipeRepo.getByName(firstName);
    }
}
