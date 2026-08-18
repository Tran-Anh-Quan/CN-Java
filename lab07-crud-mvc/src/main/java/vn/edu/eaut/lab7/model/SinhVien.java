package vn.edu.eaut.lab7.model;

public class SinhVien {
    private String id;
    private String name;
    private String email;
    private String className;

    public SinhVien() {}

    public SinhVien(String id, String name, String email, String className) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.className = className;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
}
