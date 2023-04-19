package core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import utils.StringUtil;

public class Block {
    private String hash;
    private String previousHash;
    private String timeStamp;
    private List<Transaction> transactions;
    private int nonce;

    public Block(String previousHash, List<Transaction> transactions) {
        this.previousHash = previousHash;
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.transactions = transactions;
        this.hash = calculateHash();
    }


    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                timeStamp +
                Integer.toString(nonce) +
                transactions.toString()
        );
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + hash);
    }

    // Getters and setters
    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public int getNonce() {
        return nonce;
    }

    @Override
    public String toString() {
        return "Block{" +
                "hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", timeStamp=" + timeStamp +
                ", transactions=" + transactions +
                ", nonce=" + nonce +
                '}';
    }
}
