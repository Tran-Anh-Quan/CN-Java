package vn.edu.eaut.lab5.dal;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.model.HoaDon;
import vn.edu.eaut.lab5.model.SanPhamBanChay;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class ThongKeDAL {
    public BigDecimal tinhDoanhThu(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        String sql = """
                SELECT COALESCE(SUM(tong_tien), 0) AS doanh_thu
                FROM hoa_don
                WHERE ngay_lap BETWEEN ? AND ?
                """;
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("doanh_thu");
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public Optional<HoaDon> hoaDonCaoNhat() throws SQLException {
        String sql = """
                SELECT hd.ma_hd, hd.ngay_lap, hd.ma_kh, kh.ten_kh, hd.tong_tien
                FROM hoa_don hd
                JOIN khach_hang kh ON hd.ma_kh = kh.ma_kh
                ORDER BY hd.tong_tien DESC
                LIMIT 1
                """;
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHd(rs.getInt("ma_hd"));
                hd.setNgayLap(rs.getDate("ngay_lap").toLocalDate());
                hd.setMaKh(rs.getInt("ma_kh"));
                hd.setTenKh(rs.getString("ten_kh"));
                hd.setTongTien(rs.getBigDecimal("tong_tien"));
                return Optional.of(hd);
            }
        }
        return Optional.empty();
    }

    public Optional<SanPhamBanChay> sanPhamBanChayNhat() throws SQLException {
        String sql = """
                SELECT sp.ma_sp, sp.ten_sp, SUM(ct.so_luong) AS tong_so_luong
                FROM chi_tiet_hoa_don ct
                JOIN san_pham sp ON ct.ma_sp = sp.ma_sp
                GROUP BY sp.ma_sp, sp.ten_sp
                ORDER BY tong_so_luong DESC
                LIMIT 1
                """;
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return Optional.of(new SanPhamBanChay(
                        rs.getInt("ma_sp"),
                        rs.getString("ten_sp"),
                        rs.getLong("tong_so_luong")
                ));
            }
        }
        return Optional.empty();
    }
}
