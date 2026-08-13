package vn.edu.eaut.lab5.dal;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.model.ChiTietHoaDon;
import vn.edu.eaut.lab5.model.HoaDon;

public class HoaDonDAL {
    public int insertHoaDon(int maKh, List<ChiTietHoaDon> chiTietList) throws SQLException {
        String sqlHoaDon = "INSERT INTO hoa_don(ngay_lap, ma_kh, tong_tien) VALUES (?, ?, ?)";
        String sqlChiTiet =
                "INSERT INTO chi_tiet_hoa_don(ma_hd, ma_sp, so_luong, don_gia, thanh_tien) " +
                        "VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE san_pham SET so_luong = so_luong - ? WHERE ma_sp = ?";

        Connection conn = null;
        try {
            conn = DBHelper.getConnection();
            conn.setAutoCommit(false);

            for (ChiTietHoaDon ct : chiTietList) {
                int currentStock = getStockByProductId(conn, ct.getMaSp());
                if (ct.getSoLuong() > currentStock) {
                    throw new SQLException("Số lượng mua vượt quá tồn kho");
                }
            }

            BigDecimal tongTien = tinhTongTien(chiTietList);
            int maHd;

            try (PreparedStatement ps = conn.prepareStatement(sqlHoaDon, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, Date.valueOf(LocalDate.now()));
                ps.setInt(2, maKh);
                ps.setBigDecimal(3, tongTien);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        maHd = rs.getInt(1);
                    } else {
                        throw new SQLException("Khong lay duoc ma hoa don");
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlChiTiet)) {
                for (ChiTietHoaDon ct : chiTietList) {
                    ps.setInt(1, maHd);
                    ps.setInt(2, ct.getMaSp());
                    ps.setInt(3, ct.getSoLuong());
                    ps.setBigDecimal(4, ct.getDonGia());
                    ps.setBigDecimal(5, ct.getThanhTien());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateStock)) {
                for (ChiTietHoaDon ct : chiTietList) {
                    ps.setInt(1, ct.getSoLuong());
                    ps.setInt(2, ct.getMaSp());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            return maHd;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public int getStockByProductId(int maSp) throws SQLException {
        String sql = "SELECT so_luong FROM san_pham WHERE ma_sp = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSp);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("so_luong");
                }
                return 0;
            }
        }
    }

    private int getStockByProductId(Connection conn, int maSp) throws SQLException {
        String sql = "SELECT so_luong FROM san_pham WHERE ma_sp = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSp);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("so_luong");
                }
                return 0;
            }
        }
    }

    public List<HoaDon> findByDateAndCustomer(LocalDate tuNgay, LocalDate denNgay, int maKh)
            throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        String sql = """
                SELECT hd.ma_hd, hd.ngay_lap, hd.ma_kh, kh.ten_kh, hd.tong_tien
                FROM hoa_don hd
                JOIN khach_hang kh ON hd.ma_kh = kh.ma_kh
                WHERE hd.ngay_lap BETWEEN ? AND ?
                AND (? = 0 OR hd.ma_kh = ?)
                ORDER BY hd.ngay_lap DESC, hd.ma_hd DESC
                """;
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));
            ps.setInt(3, maKh);
            ps.setInt(4, maKh);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMaHd(rs.getInt("ma_hd"));
                    hd.setNgayLap(rs.getDate("ngay_lap").toLocalDate());
                    hd.setMaKh(rs.getInt("ma_kh"));
                    hd.setTenKh(rs.getString("ten_kh"));
                    hd.setTongTien(rs.getBigDecimal("tong_tien"));
                    list.add(hd);
                }
            }
        }
        return list;
    }

    private BigDecimal tinhTongTien(List<ChiTietHoaDon> chiTietList) {
        BigDecimal tong = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : chiTietList) {
            tong = tong.add(ct.getThanhTien());
        }
        return tong;
    }
}
