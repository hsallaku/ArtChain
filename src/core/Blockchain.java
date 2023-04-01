package core;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;
    private List<Transaction> pendingTransactions;
    private int difficulty;

    public Blockchain(int difficulty) {
        this.chain = new ArrayList<>();
        this.pendingTransactions = new ArrayList<>();
        this.difficulty = difficulty;
        this.chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        List<Transaction> emptyTransactions = new ArrayList<>();
        return new Block("0", emptyTransactions);
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
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
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
        pendingTransactions.clear();
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public List<Block> getChain() {
        return chain;
    }

    public boolean isValidTransaction(Transaction transaction) {
        // Add your custom validation logic for a transaction
        // For now, we'll assume all transactions are valid
        return true;
    }

    public boolean isValidNewBlock(Block newBlock) {
        // Add your custom validation logic for a new block
        // For now, we'll assume all new blocks are valid
        return true;
    }

    public boolean isValidReceivedBlockchain(Blockchain receivedBlockchain) {
        // Add your custom validation logic for a received blockchain
        // For now, we'll assume all received blockchains are valid
        return true;
    }
}