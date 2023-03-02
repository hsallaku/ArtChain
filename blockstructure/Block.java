package blockstructure;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Block {

    private int index;
    private String data;
    private String prevHash;
    private String hash;
    private String time;
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy.HH:mm:ss");

    public Block() {
        this.index = 0;
        this.data = "";
        this.prevHash = "";
        this.hash = "";
        this.time = df.format(new Date());
    }

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

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return this.data;
    }

    public void setPrevHash(String prevHash) {
        this.prevHash = prevHash;
    }

    public String getPrevHash() {
        return this.prevHash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return this.hash;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public String getTime() {
        return this.time;
    }
}
