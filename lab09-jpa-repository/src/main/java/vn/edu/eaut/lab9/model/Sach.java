package vn.edu.eaut.lab9.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sach")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sach {
    @Id
    @Column(name = "ma_sach", length = 20)
    private String maSach;

    @Column(name = "ten_sach", nullable = false)
    private String tenSach;

    @Column(name = "tac_gia")
    private String tacGia;

    @Column(name = "gia")
    private double gia;
}
