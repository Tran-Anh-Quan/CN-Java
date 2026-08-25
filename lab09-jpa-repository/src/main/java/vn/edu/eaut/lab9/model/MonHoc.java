package vn.edu.eaut.lab9.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mon_hoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonHoc {
    @Id
    @Column(name = "ma_mon", length = 20)
    private String maMon;

    @Column(name = "ten_mon", nullable = false)
    private String tenMon;

    @Column(name = "so_tin_chi")
    private int soTinChi;
}
