package vn.edu.eaut.lab5.bus;

import vn.edu.eaut.lab5.dal.ThongKeDAL;
import vn.edu.eaut.lab5.model.HoaDon;
import vn.edu.eaut.lab5.model.SanPhamBanChay;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class ThongKeBUS {
    private final ThongKeDAL thongKeDAL = new ThongKeDAL();

    public BigDecimal tinhDoanhThu(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
        if (tuNgay == null || denNgay == null) {
            throw new IllegalArgumentException("Phai nhap khoang ngay hop le");
        }
        if (tuNgay.isAfter(denNgay)) {
            throw new IllegalArgumentException("Tu ngay khong duoc sau den ngay");
        }
        return thongKeDAL.tinhDoanhThu(tuNgay, denNgay);
    }

    public Optional<HoaDon> hoaDonCaoNhat() throws SQLException {
        return thongKeDAL.hoaDonCaoNhat();
    }

    public Optional<SanPhamBanChay> sanPhamBanChayNhat() throws SQLException {
        return thongKeDAL.sanPhamBanChayNhat();
    }
}
