package vn.edu.eaut.lab9.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "lop_hoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LopHoc {
    @Id
    @Column(name = "ma_lop", length = 20)
    private String maLop;

    @Column(name = "ten_lop", nullable = false)
    private String tenLop;

    @OneToMany(mappedBy = "lopHoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SinhVien> sinhVienList;
    
    // Custom toString to prevent recursion with Lombok's @Data
    @Override
    public String toString() {
        return "LopHoc{" + "maLop='" + maLop + '\'' + ", tenLop='" + tenLop + '\'' + '}';
    }
}
