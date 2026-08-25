package vn.edu.eaut.lab9.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ma_sv")
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "ma_mon")
    private MonHoc monHoc;

    @Column(name = "diem_thi")
    private double diemThi;
}
