package blockstructure;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Block {

    //Initialization of variables
    private int index;
    private String data;
    private String prevHash;
    private String hash;
    private String time;
    
    //Initialization of built-in Java object SimpleDateFormat 'df'
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy.HH:mm:ss");

    //Empty constructor
    public Block() {
        this.index = 0;
        this.data = "";
        this.prevHash = "";
        this.hash = "";
        this.time = df.format(new Date());
    }

    //Constructor
    public Block(int number, String data, String prevHash, String hash) {
        this.index = number;
        this.data = data;
        this.prevHash = prevHash;
        this.hash = hash;
        this.time = df.format(new Date());
    }

    public void setNumber(int number) {
        this.index = number;
    }

    public int getNumber() {
        return this.index;
    }

    //'data' String setter
    public void setData(String data) {
        this.data = data;
    }

    //'data' String getter
    public String getData() {
        return this.data;
    }

    //'prevHash' String setter
    public void setPrevHash(String prevHash) {
        this.prevHash = prevHash;
    }

    //'prevHash' String getter
    public String getPrevHash() {
        return this.prevHash;
    }

    //'hash' String setter
    public void setHash(String hash) {
        this.hash = hash;
    }

    //'hash' String getter
    public String getHash() {
        return this.hash;
    }
    
    //'time' String setter
    public void setTime(String time) {
        this.time = time;
    }
    
    //'time' String getter 
    public String getTime() {
        return this.time;
    }
}
