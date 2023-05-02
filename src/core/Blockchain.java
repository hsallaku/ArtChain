package core;

import network.*;
import java.util.*;

public class Blockchain {

    private List<Block> chain;
    private List<Transaction> pendingTransactions;
    private List<Transaction> blockTransactions = new ArrayList<>();

    public Blockchain() {
        chain = new ArrayList<>();
        pendingTransactions = new ArrayList<>();
        blockTransactions = new ArrayList<>();
        chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        List<Transaction> emptyTransactions = new ArrayList<>();
        Block genBlock = new Block("0", emptyTransactions, null, null);
        genBlock.setGenBlockHash();
        return genBlock;
    }

    public void addBlock(Block newBlock, int nodeId) {
        chain.add(newBlock);
        BlockchainIO.saveBlockchain("blockchain" + nodeId + ".json", this);
    }

    public int getLength() {
        return chain.size();
    }

    public void setPendingTransactions(List<Transaction> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public void addTransaction(Transaction transaction) {
        pendingTransactions.add(transaction);
    }

    public void acceptPendingTransaction(Transaction transaction, int index, byte[] signature, String publicString, int nodeId) {
        // If the blockTransactions list doesn't exist or is full, create a new list
        if (blockTransactions == null || blockTransactions.size() >= 2) {
            blockTransactions = new ArrayList<>();

        }

        //add the transaction to the future block's verified transaction queue
        blockTransactions.add(transaction);

        //when max transaction capacity is reached, create a new block from the queue
        //and empty the queue
        if (blockTransactions.size() >= 2) {
            Block newBlock = new Block(getLatestBlock().getHash(), blockTransactions, signature, publicString);
            newBlock.setSignatureString();
            addBlock(newBlock, nodeId);
            blockTransactions = null; // Set to null to create a new list for the next block
            System.out.println("added new block");
        }
        
        //update the state of the  transaction
        pendingTransactions.get(index).setState("processed");

        //remove block from pending transactions list
        pendingTransactions.remove(index);
    }

    public void rejectPendingTransaction(int index) {
        pendingTransactions.get(index).setState("processed");
        pendingTransactions.remove(index);
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public List<Block> getChain() {
        return chain;
    }

    public List<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }

    public void printPendingTransactions() {
        System.out.println(Arrays.toString(pendingTransactions.toArray()));
    }
    
    public void printTransactions(int blocknr) {
        System.out.println(Arrays.toString(chain.get(blocknr).getTransactions().toArray()));
    }
}
