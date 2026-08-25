package vn.edu.eaut.lab9.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "san_pham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPham {
    @Id
    @Column(name = "ma_sp", length = 20)
    private String maSp;

    @Column(name = "ten_sp", nullable = false)
    private String tenSp;

    @Column(name = "gia")
    private double gia;
}
