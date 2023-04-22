package network;

import core.Blockchain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BlockchainIO {
    
    public static void saveBlockchain(String filePath, Blockchain blockchain) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(blockchain);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static Blockchain loadBlockchain(String filePath) {
        Gson gson = new Gson();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return gson.fromJson(reader, Blockchain.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}




