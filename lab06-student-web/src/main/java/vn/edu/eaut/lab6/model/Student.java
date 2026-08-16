package vn.edu.eaut.lab6.model;

public class Student {
    private String id;
    private String name;
    private String clazz;
    private String email;

    public Student() {}

    public Student(String id, String name, String clazz, String email) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
        this.email = email;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getClazz() { return clazz; }
    public void setClazz(String clazz) { this.clazz = clazz; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}