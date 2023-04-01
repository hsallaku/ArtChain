package core;

/**
 *
 * @author Haki
 */
public class Artwork {
    private String title;
    private String artist;
    private String creationDate;
    private String provenance;

    // Constructor
    public Artwork(String title, String artist, String creationDate, String provenance) {
        this.title = title;
        this.artist = artist;
        this.creationDate = creationDate;
        this.provenance = provenance;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    @Override
    public String toString() {
        return "Artwork{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", provenance='" + provenance + '\'' +
                '}';
    }
}