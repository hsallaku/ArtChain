package network;

import core.*;
import utils.*;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.annotations.Expose;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class BNode {
    @Expose
    private int id;
    @Expose
    private String username;
    @Expose
    private String password;
    @Expose
    private String status;
    @Expose
    private String filePath;
    private Blockchain blockchain = new Blockchain();
    
    public BNode(){
        id = 0;
        username = "";
        password = "";
        status = "";
        filePath = "";
    }
    
    public BNode(String username, String password) {
        this.username = username;
        this.password = StringUtil.applySha256(password);
        this.status = "s"; // stands for standard node, validators will have the letter v

        // Check if the nodes.json file exists, and if not, create an empty one
        File usersFile = new File("users.json");
        if (!usersFile.exists()) {
            try (FileWriter writer = new FileWriter(usersFile)) {
                writer.write("[]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Read the list of nodes from the JSON file
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        List<BNode> userList;
        try (FileReader reader = new FileReader(usersFile)) {
            userList = gson.fromJson(reader, new TypeToken<List<BNode>>(){}.getType());
            if (userList == null) {
                userList = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            userList = new ArrayList<>();
        }

        // Assign an ID to the new node based on the last entry
        int lastId = userList.isEmpty() ? 0 : userList.get(userList.size() - 1).getId();
        this.id = lastId + 1;
        this.filePath = String.format("blockchain%d.json", id);

        //Creates new json file for the node
        BlockchainIO.saveBlockchain(filePath, blockchain);

        // Add the new node to the list
        BNode currentUser = this;
            userList.add(currentUser);

        // Save the updated list of nodes to the JSON file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFile))) {
            gson.toJson(userList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.printf("BNode %d connected to network\n", id);
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = StringUtil.applySha256(password);
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }

    public String getStatus() {
        return status;
    }
    
    public String getFilePath() {
        return this.filePath;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }
    
}
