package vn.edu.eaut.lab7.model;

public class Sach {
    private String id;
    private String name;
    private String author;
    private String publisher;
    private int publishYear;

    public Sach() {}

    public Sach(String id, String name, String author, String publisher, int publishYear) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.publishYear = publishYear;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public int getPublishYear() { return publishYear; }
    public void setPublishYear(int publishYear) { this.publishYear = publishYear; }
}
