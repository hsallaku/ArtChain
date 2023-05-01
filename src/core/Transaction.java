package core;

public class Transaction {

    private Artwork artwork;
    private String sender;
    private String receiver;
    private String amount;
    private String state;

    // Constructor
    public Transaction(Artwork artwork, String sender, String receiver, String amount) {
        this.artwork = artwork;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.state = "unprocessed";
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Transaction{"
                + "artwork=" + artwork
                + ", sender='" + sender + '\''
                + ", receiver='" + receiver + '\''
                + ", amount='" + amount + '\''
                + '}';
    }
}
