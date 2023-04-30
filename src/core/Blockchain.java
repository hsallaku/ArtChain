package core;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import utils.StringUtil;

public class Block {
    private String hash;
    private String previousHash;
    private String timeStamp;
    private String publicString;
    private String signatureString;
    private transient byte[] signature;
    private transient byte[] publicKey;
    private List<Transaction> transactions;

    public Block(String previousHash, List<Transaction> transactions, byte[] signature, String publicString) {
        this.previousHash = previousHash;
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.transactions = transactions;
        this.hash = calculateHash();
        this.signature = signature;
        this.publicString = publicString;
    }


    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                timeStamp +
                transactions.toString()
        );
    }

    public void mineBlock() {
        hash = calculateHash();
        System.out.println("Block mined: " + hash);
    }
    
    // Set the Hash of genesis Block
    public void setGenBlockHash(){
        this.hash = "4427766ec2a0fa4f182b3d5c88c8b9d6730ce0e54d5e5d09bc7d2b7bf01b7df3";
    }
    public void setSignatureString()
    {
        signatureString = Base64.getEncoder().encodeToString(signature);
    }
    public String getSignatureString()
    {
        return signatureString;
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
    public byte[] getSignature()
    {
        return signature;
    }
    public String getPublicString()
    {
        return publicString;
    }
    public boolean verifySignature() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException {
        byte[] publicBytes = Base64.getDecoder().decode(getPublicString());
        byte[] decodedSignature = Base64.getDecoder().decode(getSignatureString());
        PublicKey publicKey = KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(publicBytes));
        //System.out.println("public key: " + publicKey);
        //System.out.println("public key str: " + getPublicString());
        //System.out.println("public key bytes: " + Arrays.toString(publicBytes));
        //System.out.println("signature: " + Arrays.toString(decodedSignature));
        Signature verify = Signature.getInstance("SHA256withECDSA");
        verify.initVerify(publicKey);
        return verify.verify(decodedSignature);
    }
    
    @Override
    public String toString() {
        return "Block{" +
                "hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", timeStamp=" + timeStamp +
                ", transactions=" + transactions +
                '}';
    }
}
