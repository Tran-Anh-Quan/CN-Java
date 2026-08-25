package vn.edu.eaut.lab9.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "sinh_vien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SinhVien {
    
    @Id
    @Column(name = "ma_sv", length = 20)
    private String maSv;
    
    @Column(name = "ho_ten", nullable = false)
    private String hoTen;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "chuyen_nganh")
    private String chuyenNganh;
    
    @Column(name = "diem_tb")
    private double diemTb;

    @ManyToOne
    @JoinColumn(name = "ma_lop")
    private LopHoc lopHoc;

    @OneToMany(mappedBy = "sinhVien", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Diem> diemList;

    public void tinhDiemTb() {
        if (diemList == null || diemList.isEmpty()) {
            this.diemTb = 0.0;
            return;
        }
        double sum = 0;
        int count = 0;
        for (Diem d : diemList) {
            sum += d.getDiemThi();
            count++;
        }
        this.diemTb = sum / count;
    }
    
    // Custom toString to prevent recursion with Lombok's @Data
    @Override
    public String toString() {
        return "SinhVien{" + "maSv='" + maSv + '\'' + ", hoTen='" + hoTen + '\'' + '}';
    }
}
