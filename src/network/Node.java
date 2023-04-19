package network;
import core.*;
import java.util.*;

public class Node {
    private int id;
    private String username;
    private String password;
    private Network network;
    private static List<String> messages;
    private String filePath;
    
    public Node(int id, String username, String password, Network network, String filePath, Blockchain blockchain) {
        //Basic constructor variables
        this.id = id;
        this.username = username;
        this.password = password;
        this.network = network;
        this.filePath = filePath;
        
        //ArrayList for storing all messages received by this node. Will need to be reworked.
        messages = new ArrayList<>();
        
        //Creates new json file for the node
        BlockchainIO.saveBlockchain(filePath, blockchain);
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setNetwork(Network network) {
        this.network = network;
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
    
    public Network getNetwork() {
        return this.network;
    }
    
    public String getFilePath() {
        return this.filePath;
    }
    
    
    public void startNode() {
        //Placeholder
        //Will need to be reworked for what we're trying to implement
        
        //String to return for when node is started
        System.out.printf("Node %d connected to network\n", id);
        
        for(int i = 0; i < 1; i++) {
            //Try/catch for simulated network delay
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
            
            if(Math.random() < 0.5) {
                int receiverId = (int) (Math.random() * network.nodes.size());
                if(receiverId != id) {
                    String message = String.format("Hello from node %d!", id);
                    network.sendMessage(id, receiverId, message);
                }
            }
        }
    }
    
    public void createTransaction(Artwork artwork, String sender, String receiver, String signature) {
        Transaction transaction = new Transaction(artwork, sender, receiver, signature);
    }
    
    public void receiveTransaction() {
        
    }
    
    public void sendTransaction() {
        
    }
    
    public boolean isChainValid() {
        return true;
    }
    
    public void receiveMessage(int senderId, String message) {
        //Placeholder
        //Will need to be removed or reworked for what we're trying to implement
        System.out.printf("Node %d received message '%s' from node %d\n", id, message, senderId);
        messages.add(message);
    }

    public static void printMessages() {
        System.out.println(messages.toString());
    }
}
