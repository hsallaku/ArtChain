package blockchainproject_v1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Block 
{
    private String hash;
    private String prevHash;
    private String data;
    private long timeStamp;
    private int index;
    private int nonce;

    public Block(){}
    public Block(String prevHash, String data, long timeStamp, int index) {
        this.prevHash = prevHash;
        this.data = data;
        this.timeStamp = timeStamp;
        this.index = index;
        this.nonce = 0;
        this.hash = Block.calculateHash(this);
    }

    public String getHash() {
        return hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public String getData() {
        return data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getIndex() {
        return index;
    }
    
    public String str() {
        return index + timeStamp + prevHash + data + nonce; 
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Block #").append(index).append(", ").append("previous Hash: ").append(prevHash).append(", ").append("timestamp: ").append
        (new Date(timeStamp)).append(", ").append("data: ").append(data).append(", ").append("Hash: ").append(hash).append("]");
        
        return builder.toString();
    }

    public static String calculateHash(Block block) {
        if (block != null) {            
            MessageDigest digest = null;
            
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException ex) {
                return null;               
            }
            
            String txt = block.str();
            final byte bytes[] = digest.digest(txt.getBytes());
            final StringBuilder builder = new StringBuilder();
            
            for (final byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1) {
                    builder.append('0');
                } 
                builder.append(hex);
            }
            return builder.toString();
        }
        return null;
    }

    
    public void mineBlock(int difficulty) {
        nonce = 0;
        
        while (!getHash().substring(0, difficulty).equals(Utils.zeros(difficulty))) {
            nonce++;
            hash = Block.calculateHash(this);
        }
    }   
    
    
    
}
