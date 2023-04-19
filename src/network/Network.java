package network;
import core.*;
import java.util.*;
import java.io.File;


public class Network {
    public Map<Integer, Node> nodes;
    
    public Network(Blockchain blockchain) {
        //Generates new hashmap
        this.nodes = new HashMap<>();
        
        //Initializes String for the name of the json file.
        String file;
        
        //For loop populates hashmap with n number of nodes, each with their own unique json
        /*for(int id = 0; id < n; id++) {
            file = String.format("blockchaincopy%d.json", id);
            //if node is not already in our gson node file do this
            nodes.put(id, new Node(id, this, file, blockchain));
            //else
            //look into gson file for said node and insert that one
        }*/
    }
    
    public void startNetwork() {
        //Goes through each node in the hashmap and calls their startNode() function
        for(Node node : nodes.values()) {
            node.startNode();
        }
        System.out.println("Network started.");
    }
    
    public void addNode(String username, String password, Network network, Blockchain blockchain) {
        //Work in progress
        
        //Gets size of hashmap in order to give Node a sequential # at the end of it's name
        int id = nodes.size();
        
        //Generates a name for the Node's json file based on the Node's id
        String file = String.format("blockchaincopy%d.json", id);
        
        //Adds a new node to the hashmap
        nodes.put(id, new Node(id, username, password, network, file, blockchain));
        
        System.out.printf("Node %d added.\n", id);
    }
    
    public void removeNode(int id) {
        //Work in progress
        
        //Generates a name for the Node's json file based on the Node's id so that the correct json can be removed
        String filePath = String.format("blockchaincopy%d.json", id);
        
        //New File object needs to be created in order to use the file.delete() function in the if statement below
        File file = new File(filePath);
        
        //containsKey(id) searches hashmap for the specified id and returns T/F if found/not found. 
        //If true, node is removed from hashmap, it's json is deleted, and success message printed
        //If false, failure message is printed.
        if(nodes.containsKey(id)) {
            nodes.remove(id);
            file.delete();
            System.out.printf("Node %d removed.\n", id);
        }
        else {
            System.out.printf("Node %d does not exist.\n", id);
        }
    }
    
    public Map<Integer, Node> getNodes() {
        return this.nodes;
    }
    
    public Node getNodeByUsername(String username) {
        for(Map.Entry<Integer, Node> entry : nodes.entrySet()) {
            Node node = entry.getValue();
            if(node.getUsername().equals(username)) {
                return node;
            }
        }
        return null;
    }
    
    public void sendMessage(int senderId, int receiverId, String message) {
        //Placeholder
        //Needs to be removed or reworked to fit with what we're trying to implement
        Node receiver = nodes.get(receiverId);
        receiver.receiveMessage(senderId, message);
    }
    
    public void updateBlockChain(int senderId, int receiverId, String message) {
        //Work in progress
        Node receiver = nodes.get(receiverId);
    }
    
    public void printNodes() {
        System.out.print("Nodes present in network: ");
        for(Node node : nodes.values()) {
            //Goes through each node in the hashmap and prints their IDs
            System.out.print(node.getId() + " ");
        }
        System.out.print("\n");
    }
}
