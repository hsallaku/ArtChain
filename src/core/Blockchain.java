package core;

import java.util.*;

public class Blockchain {
    private List<Block> chain;
    private List<Transaction> pendingTransactions;
    private List<Transaction> blockTransactions = new ArrayList<Transaction>();

    public Blockchain() {
        chain = new ArrayList<>();
        pendingTransactions = new ArrayList<Transaction>();
        blockTransactions = new ArrayList<Transaction>();
        chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        List<Transaction> emptyTransactions = new ArrayList<Transaction>();
        return new Block("0", emptyTransactions);
    }

    public void addBlock(Block newBlock) {
        chain.add(newBlock);
    }
    
    public int getLength() {
        return chain.size();
    }

    public boolean isChainValid() {
        for (int i = 1; i < getLength(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current hash does not match calculated hash for block " + i);
                return false;
            }

            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                System.out.println("Previous hash does not match for block " + i);
                return false;
            }
        }
        return true;
    }

    public void addTransaction(Transaction transaction) {
        pendingTransactions.add(transaction);
    }

    public void acceptPendingTransaction(Transaction transaction, int index) {
        // If the blockTransactions list doesn't exist or is full, create a new list
        if (blockTransactions == null || blockTransactions.size() >= 2) {
            blockTransactions = new ArrayList<>();
        }

        //add the transaction to the future block's verified transaction queue
        blockTransactions.add(transaction);

        //when max transaction capacity is reached, create a new block from the queue
        //and empty the queue
        if (blockTransactions.size() >= 2) {
            Block newBlock = new Block(getLatestBlock().getHash(), blockTransactions);
            chain.add(newBlock);
            blockTransactions = null; // Set to null to create a new list for the next block
            System.out.println("added new block");
        }

        //remove block from pending transactions list
        pendingTransactions.remove(index);
    }
    
    public void rejectPendingTransaction(int index){
        pendingTransactions.remove(index);     
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public List<Block> getChain() {
        return chain;
    }
    
    public List<Transaction> getPendingTransactions(){
        return pendingTransactions;
    }
    
    public void printPendingTransactions(){
        System.out.println(Arrays.toString(pendingTransactions.toArray()));
    }
    
    public void printTransactions(int blocknr){
        System.out.println(Arrays.toString(chain.get(blocknr).getTransactions().toArray()));
    }
}
