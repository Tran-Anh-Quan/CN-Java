package vn.edu.eaut.lab3;

public class SinhVien {
    private String id;
    private String name;
    private String major;

    public SinhVien(String id, String name, String major) {
        this.id = id;
        this.name = name;
        this.major = major;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
}
