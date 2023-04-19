package Main;

import core.*;
import network.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Initialize the blockchain and set the mining difficulty
        // Load blockchain from file, or create a new one if the file doesn't exist
        String filePath = "blockchain.json";
        Blockchain blockchain = BlockchainIO.loadBlockchain(filePath);
        if (blockchain == null) {
            blockchain = new Blockchain(4);
        }
        
        
        // Save the blockchain to a JSON file
        BlockchainIO.saveBlockchain(filePath, blockchain);

        // Load the blockchain from the JSON file
        Blockchain loadBlockchain = BlockchainIO.loadBlockchain(filePath);

        // Create Artwork instances
        Artwork artwork1 = new Artwork("Mona Lisa", "Leonardo da Vinci", "1503", "Louvre Museum, Paris");
        Artwork artwork2 = new Artwork("The Starry Night", "Vincent van Gogh", "1889", "Museum of Modern Art, New York");

        // Create and sign Transactions
        Transaction transaction1 = new Transaction(artwork1, "Owner1", "Buyer1", "signature1");
        Transaction transaction2 = new Transaction(artwork2, "Owner2", "Buyer2", "signature2");

        // Add Transactions to the blockchain
        blockchain.addTransaction(transaction1);
        blockchain.addTransaction(transaction2);

        // Process pending transactions, create a new block, mine it, and add it to the chain
        blockchain.processPendingTransactions("MinerAddress");
        
        //Commented this out to reduce clutter when testing network implementation
        // Validate the blockchain and display its contents
        /*if (blockchain.isChainValid()) {
            System.out.println("Blockchain is valid");
            for (Block block : blockchain.getChain()) {
                System.out.println("Block: " + block.toString());
            }
        } else {
            System.out.println("Blockchain is invalid");
        }*/
        
        // Save the blockchain to a file before exiting
        BlockchainIO.saveBlockchain(filePath, blockchain);
        
        //Creates a new network object that generates a hashmap that includes 4 nodes
        Network network = new Network(blockchain);
        
        //startNetwork() function goes through each node within the hashmap and calls their startNode() function
        network.startNetwork();
        network.addNode("LouvreMuseum", "12345", network, blockchain);
        network.addNode("MoMA", "abc123", network, blockchain);
        
        Scanner input = new Scanner(System.in);
        String choice;
        boolean loggedIn = false;
        menuNonUser(input, network, blockchain);
        
    }
    
    public static Node menuLogin(Scanner input, Network network) {
        boolean correctUsername = false;
        boolean correctPassword = false;
        boolean loggedIn = false;
        Node user = null;

        while (!correctUsername || !correctPassword) {
            System.out.print("Enter username: ");
            String username = input.nextLine();
            user = network.getNodeByUsername(username);

            if (user == null) {
                System.out.println("Username not found.");
            } 
            else {
                correctUsername = true;
                while (!correctPassword) {
                    System.out.print("Enter password: ");
                    String password = input.nextLine();
                    if (!user.getPassword().equals(password)) {
                        System.out.println("Incorrect password.");
                    } 
                    else {
                        System.out.println("Successfully logged in as " + username);
                        correctPassword = true;
                        loggedIn = true;
                        return user;
                    }
                }
            }
        }
        return null;
    }
    
    public static void menuSignUp(Scanner input, Network network, Blockchain blockchain) {
        boolean uniqueUsername = false;
        boolean confirmedPassword = false;

        System.out.print("Enter username: ");
        String username = input.nextLine();

        while (!uniqueUsername) {
            if (network.nodes.containsValue(username)) {
                System.out.println("Username already exists.");
                continue;
            } else {
                uniqueUsername = true;
            }
        }

        System.out.print("Enter password: ");
        String password = input.nextLine();

        while (!confirmedPassword) {
            System.out.print("Confirm password: ");
            String confirmPassword = input.next();

            if (confirmPassword.equals(password)) {
                network.addNode(username, password, network, blockchain);
                System.out.println("Congratulations, your ArtChain account has been created!");
                confirmedPassword = true;
            } else {
                System.out.println("Passwords do not match.");
                continue;
            }
        }
    }
    
    public static void menuNonUser(Scanner input, Network network, Blockchain blockchain) {
        boolean loggedIn = false;
        String choice;
        while(true) {
            System.out.println("\nWelcome to ArtChain");
            System.out.println("1. Login");
            System.out.println("2. Sign up");
            System.out.println("3. Quit");
            System.out.print("Enter menu option: ");
            choice = input.next();
            input.nextLine();
            
            switch(choice) {
                case "1": {
                    Node user = menuLogin(input, network);
                    if(user != null) {
                        loggedIn = true;
                        menuUser(input, network, blockchain, user, loggedIn);
                    }
                }
                case "2": {
                    menuSignUp(input, network, blockchain);
                    break;
                }
                case "3": {
                    System.exit(0);
                }
            }
        }
    }
    
    public static void menuUser(Scanner input, Network network, Blockchain blockchain, Node user, boolean loggedIn) {
        String choice;
        while(true) {
            System.out.println("\nWelcome " + user.getUsername());
            System.out.println("1. a");
            System.out.println("2. b");
            System.out.println("3. Logout");
            System.out.println("4. Exit");
            System.out.print("Enter menu option: ");
            choice = input.nextLine();
            input.nextLine();
            
            switch(choice) {
                case "1": {
                    
                }
                case "2": {
                    
                }
                case "3": {
                    menuNonUser(input, network, blockchain);
                }
                case "4": {
                    System.exit(0);
                }
                default: {
                    System.out.println("Invalid choice.");
                }
            }
        }
    }
}
