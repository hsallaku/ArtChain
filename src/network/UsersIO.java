package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.google.gson.reflect.TypeToken;

public class UsersIO {
    
    public static void saveUsers(String filePath, List<Node> users) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(users);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<Node> loadUsers(String filePath) {
        Gson gson = new Gson();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return gson.fromJson(reader, new TypeToken<List<Node>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}