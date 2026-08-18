package vn.edu.eaut.lab7.model;

public class DiemSinhVien {
    private String id;
    private String studentId;
    private double diemCC;
    private double diemGK;
    private double diemCK;

    public DiemSinhVien() {}

    public DiemSinhVien(String id, String studentId, double diemCC, double diemGK, double diemCK) {
        this.id = id;
        this.studentId = studentId;
        this.diemCC = diemCC;
        this.diemGK = diemGK;
        this.diemCK = diemCK;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public double getDiemCC() { return diemCC; }
    public void setDiemCC(double diemCC) { this.diemCC = diemCC; }
    public double getDiemGK() { return diemGK; }
    public void setDiemGK(double diemGK) { this.diemGK = diemGK; }
    public double getDiemCK() { return diemCK; }
    public void setDiemCK(double diemCK) { this.diemCK = diemCK; }

    public double getTongKet() {
        return (diemCC * 0.1) + (diemGK * 0.3) + (diemCK * 0.6);
    }

    public String getXepLoai() {
        double tk = getTongKet();
        if (tk >= 8.5) return "A";
        if (tk >= 7.0) return "B";
        if (tk >= 5.5) return "C";
        if (tk >= 4.0) return "D";
        return "F";
    }
}
