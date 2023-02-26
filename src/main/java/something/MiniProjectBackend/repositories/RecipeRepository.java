package something.MiniProjectBackend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import static something.MiniProjectBackend.repositories.Queries.*;

@Repository
public class RecipeRepository {
    
    @Autowired JdbcTemplate jdbcTemplate;

    @Autowired MongoTemplate mongoTemplate;

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
