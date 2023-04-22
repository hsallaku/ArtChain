package core;

import java.util.*;

public class Blockchain {
    private List<Block> chain;
    private List<Transaction> pendingTransactions;

    public Blockchain() {
        this.chain = new ArrayList<>();
        this.pendingTransactions = new ArrayList<>();
        this.chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        List<Transaction> emptyTransactions = new ArrayList<>();
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

    public void processPendingTransactions(String minerAddress) {
        List<Transaction> blockTransactions = new ArrayList<>(pendingTransactions);
        Block newBlock = new Block(getLatestBlock().getHash(), blockTransactions);
        chain.add(newBlock);
        pendingTransactions.clear();
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public List<Block> getChain() {
        return chain;
    }
    
    public Blockchain copy() {
        Blockchain newBlockchain = new Blockchain();
        newBlockchain.chain = new ArrayList<>(chain);
        newBlockchain.pendingTransactions = new ArrayList<>(pendingTransactions);
        return newBlockchain;
    }
    
    public void printPendingTransactions(){
        System.out.println(Arrays.toString(pendingTransactions.toArray()));
    }
}
