package network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SynchronizationSimulator implements Runnable {

    private int nodesCount;

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            nodesCount = getTotalNumberOfNodes();
            if (nodesCount >= 3) {
                int nodeId1 = random.nextInt(nodesCount) + 1;
                int nodeId2 = random.nextInt(nodesCount) + 1;
                if (nodeId1 != nodeId2) {
                    try {
                        compareNodes(nodeId1, nodeId2);
                    } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException ex) {
                        Logger.getLogger(SynchronizationSimulator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void compareNodes(int nodeId1, int nodeId2) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException {
        Blockchain blockchain1 = BlockchainIO.loadBlockchain(String.format("blockchain%d.json", nodeId1));
        Blockchain blockchain2 = BlockchainIO.loadBlockchain(String.format("blockchain%d.json", nodeId2));
        int length1 = blockchain1.getLength();
        int length2 = blockchain2.getLength();
        int size1 = blockchain1.getPendingTransactions().size();
        int size2 = blockchain2.getPendingTransactions().size();

        BNode node1 = UsersIO.loadUsers("users.json").get(nodeId1 - 1);
        BNode node2 = UsersIO.loadUsers("users.json").get(nodeId2 - 1);

        if (length1 != length2) {
            if (length1 > length2) {
                Block testBlock = blockchain1.getLatestBlock();
                if (testBlock.verifySignature()) {
                    System.out.println("Signature verification passed");
                    BlockchainIO.saveBlockchain(String.format("blockchain%d.json", nodeId2), blockchain1);
                    System.out.printf("%s updated their blockchain based on %s's%n", node2.getUsername(), node1.getUsername());
                } else {
                    System.out.println("Signature verification failed");
                }
            } else {
                Block testBlock = blockchain1.getLatestBlock();
                if (testBlock.verifySignature()) {
                    System.out.println("Signature verification passed");
                    BlockchainIO.saveBlockchain(String.format("blockchain%d.json", nodeId1), blockchain2);
                    System.out.printf("%s updated their blockchain based on %s's%n", node1.getUsername(), node2.getUsername());
                } else {
                    System.out.println("Signature verification failed");
                }
            }
        } else {
            String hash1 = blockchain1.getLatestBlock().getHash();
            String hash2 = blockchain2.getLatestBlock().getHash();

            if (hash1.equals(hash2)) {
                System.out.printf("%s is in sync with %s%n", node1.getUsername(), node2.getUsername());
            } else {
                verifyNode(node1);
                verifyNode(node2);
            }
        }
        /*if (size1 != size2) {	
            if (size1 > size2) {	
                blockchain2.setPendingTransactions(blockchain1.getPendingTransactions());	
                BlockchainIO.saveBlockchain(String.format("blockchain%d.json", nodeId2), blockchain1);	
                System.out.printf("%s updated their pending transactions based on %s's%n", node2.getUsername(), node1.getUsername());	
            } else {	
                blockchain1.setPendingTransactions(blockchain2.getPendingTransactions());	
                BlockchainIO.saveBlockchain(String.format("blockchain%d.json", nodeId1), blockchain2);	
                System.out.printf("%s updated their pending transactions based on %s's%n", node1.getUsername(), node2.getUsername());	
            }
    }*/
    }

    

    private void verifyNode(BNode node) {
        System.out.printf("Verifying node %d, known as %s%n", node.getId(), node.getUsername());
    }

    // Return the number of nodes located in the database
    public int getTotalNumberOfNodes() {
        File usersFile = new File("users.json");
        Gson gson = new Gson();
        List<BNode> users;

        if (usersFile.exists()) {
            try ( FileReader reader = new FileReader(usersFile)) {
                Type userListType = new TypeToken<ArrayList<BNode>>() {
                }.getType();
                users = gson.fromJson(reader, userListType);
            } catch (IOException e) {
                e.printStackTrace();
                users = new ArrayList<>();
            }
        } else {
            users = new ArrayList<>();
        }

        return users.size();
    }

}
