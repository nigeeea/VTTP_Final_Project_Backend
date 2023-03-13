package something.MiniProjectBackend.repositories;


import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.google.common.hash.Hashing;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import something.MiniProjectBackend.models.User;

import static something.MiniProjectBackend.repositories.Queries.*;

import java.nio.charset.StandardCharsets;

@Repository
public class RecipeRepository {
    
    @Autowired JdbcTemplate jdbcTemplate;

    @Autowired MongoTemplate mongoTemplate;


    //METHOD TO REGISTER/SIGNUP USER - METHOD 1
    public boolean registerUser(User user){

        try {
            User userResponse = mongoTemplate.insert(user, "users");

            if(userResponse.equals(user)){
                return true;
            }
            else{
                return false;
            } 
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("this is the error>>>"+e.toString());
            return   false;
        }
        
    }

    //METHOD TO AUTHENTICATE USER LOGIN - METHOD 2
    public boolean authenticateUser(User user){

        String username = user.getUsername();
        String password = Hashing.sha256().hashString(user.getPassword(), StandardCharsets.UTF_8).toString();

        Query query = new Query();

        query.addCriteria(Criteria.where("username").is(username));
        
        try {
            Document doc = mongoTemplate.findOne(query,Document.class, "users");

            String returnedPw = doc.getString("password");

            if(password.equals(returnedPw)){
            return true;
        }
        else{return false;}

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        
    } 


    //METHOD FOR SQL TESTING
    public JsonObject getByName(String firstName){
        SqlRowSet rs = null;

        rs=jdbcTemplate.queryForRowSet(SQL_SELECT_BY_NAME, firstName);

        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        while(rs.next()){
            objectBuilder.add("lastName", rs.getString("lastName"));
        }

        JsonObject response = objectBuilder.build();

        return response;
        
    }
}
