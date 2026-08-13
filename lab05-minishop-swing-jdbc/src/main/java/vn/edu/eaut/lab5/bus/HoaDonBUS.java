package vn.edu.eaut.lab5.bus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import vn.edu.eaut.lab5.dal.HoaDonDAL;
import vn.edu.eaut.lab5.model.ChiTietHoaDon;
import vn.edu.eaut.lab5.model.HoaDon;

public class HoaDonBUS {
    private final HoaDonDAL hoaDonDAL = new HoaDonDAL();

    public int createInvoice(int maKh, List<ChiTietHoaDon> chiTietList) throws SQLException {
        if (maKh <= 0) {
            throw new IllegalArgumentException("Phai chon khach hang");
        }
        if (chiTietList == null || chiTietList.isEmpty()) {
            throw new IllegalArgumentException("Hoa don phai co it nhat mot san pham");
        }
        for (ChiTietHoaDon ct : chiTietList) {
            if (ct == null) {
                throw new IllegalArgumentException("San pham khong hop le");
            }
            if (ct.getSoLuong() <= 0) {
                throw new IllegalArgumentException("So luong ban phai lon hon 0");
            }
            int currentStock = hoaDonDAL.getStockByProductId(ct.getMaSp());
            if (ct.getSoLuong() > currentStock) {
                throw new IllegalArgumentException("Số lượng mua vượt quá tồn kho");
            }
        }
        return hoaDonDAL.insertHoaDon(maKh, chiTietList);
    }

    public List<HoaDon> findByDateAndCustomer(LocalDate tuNgay, LocalDate denNgay, int maKh)
            throws SQLException {
        if (tuNgay == null || denNgay == null) {
            throw new IllegalArgumentException("Phai nhap khoang ngay hop le");
        }
        if (tuNgay.isAfter(denNgay)) {
            throw new IllegalArgumentException("Tu ngay khong duoc sau den ngay");
        }
        return hoaDonDAL.findByDateAndCustomer(tuNgay, denNgay, maKh);
    }
}
