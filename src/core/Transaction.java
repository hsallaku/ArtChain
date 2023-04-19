package core;

/**
 *
 * @author Haki
 */
public class Transaction {
    private Artwork artwork;
    private String sender;
    private String receiver;
    private String signature;  //need to build a validation function to verify sender signature

    // Constructor
    public Transaction(Artwork artwork, String sender, String receiver, String signature) {
        this.artwork = artwork;
        this.sender = sender;
        this.receiver = receiver;
        this.signature = signature;
    }

    // Getters and setters
    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "artwork=" + artwork +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
