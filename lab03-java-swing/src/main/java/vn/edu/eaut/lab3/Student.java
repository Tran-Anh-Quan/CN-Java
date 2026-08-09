package vn.edu.eaut.lab3;

public class Student {
    private String id;
    private String name;
    private double diemTrungBinh;

    public Student(String id, String name, double diemTrungBinh) {
        this.id = id;
        this.name = name;
        this.diemTrungBinh = diemTrungBinh;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getDiemTrungBinh() { return diemTrungBinh; }
    public void setDiemTrungBinh(double diemTrungBinh) { this.diemTrungBinh = diemTrungBinh; }
    
    // Tự động xếp loại dựa trên điểm trung bình
    public String getXepLoai() {
        if (diemTrungBinh >= 8.5) return "Giỏi";
        if (diemTrungBinh >= 7.0) return "Khá";
        if (diemTrungBinh >= 5.0) return "Trung bình";
        return "Yếu";
    }
}
