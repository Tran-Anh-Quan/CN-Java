package vn.edu.eaut.lab5;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

import vn.edu.eaut.lab5.bus.HoaDonBUS;
import vn.edu.eaut.lab5.bus.KhachHangBUS;
import vn.edu.eaut.lab5.bus.SanPhamBUS;
import vn.edu.eaut.lab5.dal.SanPhamDAL;
import vn.edu.eaut.lab5.model.ChiTietHoaDon;
import vn.edu.eaut.lab5.model.KhachHang;
import vn.edu.eaut.lab5.model.SanPham;

public class AppTest {
    @Test
    public void sanPhamValidationShouldRejectEmptyName() {
        SanPhamBUS bus = new SanPhamBUS();
        SanPham sp = new SanPham();
        sp.setTenSp("");
        sp.setDonGia(new BigDecimal("1000"));
        sp.setSoLuong(1);
        try {
            bus.save(sp);
            fail("Expected validation error");
        } catch (Exception ex) {
            assertEquals("Ten san pham khong duoc rong", ex.getMessage());
        }
    }

    @Test
    public void khachHangValidationShouldRejectInvalidPhone() {
        KhachHangBUS bus = new KhachHangBUS();
        KhachHang kh = new KhachHang();
        kh.setTenKh("Test");
        kh.setSdt("abc");
        try {
            bus.save(kh);
            fail("Expected validation error");
        } catch (Exception ex) {
            assertEquals("So dien thoai chi gom so va toi da 10 ky tu", ex.getMessage());
        }
    }

    @Test
    public void invoiceShouldRejectQuantityExceedingStock() throws Exception {
        String uniqueName = "TestSP_" + System.currentTimeMillis();
        SanPhamDAL sanPhamDAL = new SanPhamDAL();
        SanPham sp = new SanPham();
        sp.setTenSp(uniqueName);
        sp.setDonGia(new BigDecimal("1000"));
        sp.setSoLuong(2);
        assertTrue(sanPhamDAL.insert(sp));

        List<SanPham> list = new SanPhamBUS().searchByName(uniqueName);
        SanPham saved = list.get(0);
        ChiTietHoaDon ct = new ChiTietHoaDon(saved.getMaSp(), saved.getTenSp(), 3, saved.getDonGia());

        try {
            new HoaDonBUS().createInvoice(1, Collections.singletonList(ct));
            fail("Expected stock validation error");
        } catch (IllegalArgumentException ex) {
            assertEquals("Số lượng mua vượt quá tồn kho", ex.getMessage());
        }

        sanPhamDAL.delete(saved.getMaSp());
    }

    @Test
    public void invoiceShouldReduceStockAfterSuccessfulSale() throws Exception {
        String uniqueName = "TestSPSell_" + System.currentTimeMillis();
        SanPhamDAL sanPhamDAL = new SanPhamDAL();
        SanPham sp = new SanPham();
        sp.setTenSp(uniqueName);
        sp.setDonGia(new BigDecimal("1000"));
        sp.setSoLuong(5);
        assertTrue(sanPhamDAL.insert(sp));

        List<SanPham> list = new SanPhamBUS().searchByName(uniqueName);
        SanPham saved = list.get(0);
        ChiTietHoaDon ct = new ChiTietHoaDon(saved.getMaSp(), saved.getTenSp(), 2, saved.getDonGia());

        int maHd = new HoaDonBUS().createInvoice(1, Collections.singletonList(ct));
        assertTrue(maHd > 0);

        SanPham updated = new SanPhamBUS().searchByName(uniqueName).stream()
                .filter(x -> x.getMaSp() == saved.getMaSp())
                .findFirst()
                .orElseThrow(() -> new AssertionError("Product not found after invoice"));
        assertEquals(3, updated.getSoLuong());

        try (var conn = java.sql.DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/minishop_db?useUnicode=true&characterEncoding=UTF-8",
                "root",
                "Tranmanhquan@21")) {
            try (var ps = conn.prepareStatement("DELETE FROM chi_tiet_hoa_don WHERE ma_hd = ?")) {
                ps.setInt(1, maHd);
                ps.executeUpdate();
            }
            try (var ps = conn.prepareStatement("DELETE FROM hoa_don WHERE ma_hd = ?")) {
                ps.setInt(1, maHd);
                ps.executeUpdate();
            }
        }

        sanPhamDAL.delete(saved.getMaSp());
    }
}
