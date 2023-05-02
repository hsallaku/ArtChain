package network;

import core.*;
import utils.*;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.annotations.Expose;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class BNode {

    @Expose
    private int id;
    @Expose
    private String username;
    @Expose
    private String password;
    @Expose
    private String status;
    @Expose
    private String filePath;
    @Expose
    private String privateKeyString;
    @Expose
    private String publicKeyString;
    private transient PrivateKey privateKey;
    private transient PublicKey publicKey;
    private Blockchain blockchain;

    public BNode() {
        id = 0;
        username = "";
        password = "";
        status = "";
        filePath = "";
        privateKeyString = "";
        publicKeyString = "";
        blockchain = new Blockchain();
    }

    public BNode(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.username = username;
        this.password = StringUtil.applySha256(password);
        this.status = "s"; // stands for standard node, validators will have the letter v
        blockchain = new Blockchain();
        //generate the keyPair for new nodes
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        SecureRandom random = SecureRandom.getInstanceStrong();
        keyPairGenerator.initialize(256, random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = (ECPrivateKey) keyPair.getPrivate();
        privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        // Check if the nodes.json file exists, and if not, create an empty one
        File usersFile = new File("users.json");
        if (!usersFile.exists()) {
            try ( FileWriter writer = new FileWriter(usersFile)) {
                writer.write("[]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Read the list of nodes from the JSON file
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        List<BNode> userList;
        try ( FileReader reader = new FileReader(usersFile)) {
            userList = gson.fromJson(reader, new TypeToken<List<BNode>>() {
            }.getType());
            if (userList == null) {
                userList = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            userList = new ArrayList<>();
        }

        // Assign an ID to the new node based on the last entry
        int lastId = userList.isEmpty() ? 0 : userList.get(userList.size() - 1).getId();
        this.id = lastId + 1;
        this.filePath = String.format("blockchain%d.json", id);

        //Creates new json file for the node
        BlockchainIO.saveBlockchain(filePath, blockchain);

        // Add the new node to the list
        BNode currentUser = this;
        userList.add(currentUser);

        // Save the updated list of nodes to the JSON file
        try ( BufferedWriter writer = new BufferedWriter(new FileWriter(usersFile))) {
            gson.toJson(userList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Node %d connected to network\n", id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = StringUtil.applySha256(password);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getStatus() {
        return status;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(Blockchain blockchain) {

        this.blockchain = blockchain;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public String getPrivateString() {
        return privateKeyString;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public String getPublicString() {
        return publicKeyString;
    }

    public void restoreKeysFromLoad() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateBytes = Base64.getDecoder().decode(getPrivateString());
        byte[] publicBytes = Base64.getDecoder().decode(getPublicString());
        privateKey = KeyFactory.getInstance("EC").generatePrivate(new PKCS8EncodedKeySpec(privateBytes));
        publicKey = KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(publicBytes));
    }

    public byte[] signBlock() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(getPrivateKey());
        return signature.sign();
    }

    public boolean verifyBlock(PublicKey publicKey, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature verify = Signature.getInstance("SHA256withECDSA");
        verify.initVerify(publicKey);
        return verify.verify(signature);
    }

}
