package main;

import core.*;
import network.*;

public class Main {
    public static void main(String[] args) {
        // Initialize the blockchain and set the mining difficulty
        // Load blockchain from file, or create a new one if the file doesn't exist
        String filePath = "blockchain.json";
        Blockchain blockchain = BlockchainIO.loadBlockchain(filePath);
        if (blockchain == null) {
            blockchain = new Blockchain(4);
        }

        // Create a P2P node and pass the blockchain instance
        P2PNode node = new P2PNode(8080, blockchain);

        // Start the P2P node
        new Thread(() -> node.start()).start();
        
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

        // Validate the blockchain and display its contents
        if (blockchain.isChainValid()) {
            System.out.println("Blockchain is valid");
            for (Block block : blockchain.getChain()) {
                System.out.println("Block: " + block.toString());
            }
        } else {
            System.out.println("Blockchain is invalid");
        }
        
        // Save the blockchain to a file before exiting
        BlockchainIO.saveBlockchain(filePath, blockchain);
    }
}
