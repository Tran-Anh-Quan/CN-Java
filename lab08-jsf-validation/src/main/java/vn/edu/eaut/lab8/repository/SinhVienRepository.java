package vn.edu.eaut.lab8.repository;

import vn.edu.eaut.lab8.model.SinhVien;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SinhVienRepository implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final List<SinhVien> list = new ArrayList<>();

    static {
        // Khởi tạo dữ liệu mẫu với Lớp học (Bài 10 & Bài 12)
        list.add(new SinhVien("SV001", "Nguyễn Văn An", "an.nguyen@eaut.edu.vn", "0912345678", 8.8, "Công nghệ thông tin", "DCNTT14-01", "Nam"));
        list.add(new SinhVien("SV002", "Trần Thị Bình", "binh.tran@eaut.edu.vn", "0987654321", 7.6, "Khoa học máy tính", "DKHMT14-01", "Nữ"));
        list.add(new SinhVien("SV003", "Lê Hoàng Cường", "cuong.le@eaut.edu.vn", "0905112233", 9.2, "Hệ thống thông tin", "DCNTT14-02", "Nam"));
        list.add(new SinhVien("SV004", "Phạm Minh Dung", "dung.pham@eaut.edu.vn", "0944556677", 6.4, "Kỹ thuật phần mềm", "DKTPM14-01", "Nữ"));
    }

    public List<SinhVien> findAll() {
        return new ArrayList<>(list);
    }

    public SinhVien findById(String maSV) {
        if (maSV == null) return null;
        return list.stream()
                .filter(sv -> sv.getMaSV().equalsIgnoreCase(maSV.trim()))
                .findFirst()
                .orElse(null);
    }

    public boolean save(SinhVien sv) {
        if (sv == null || sv.getMaSV() == null || sv.getMaSV().trim().isEmpty()) {
            return false;
        }
        
        SinhVien existing = findById(sv.getMaSV());
        if (existing != null) {
            existing.setHoTen(sv.getHoTen());
            existing.setEmail(sv.getEmail());
            existing.setSoDienThoai(sv.getSoDienThoai());
            existing.setDiemTB(sv.getDiemTB());
            existing.setChuyenNganh(sv.getChuyenNganh());
            existing.setLopHoc(sv.getLopHoc());
            existing.setGioiTinh(sv.getGioiTinh());
        } else {
            list.add(sv);
        }
        return true;
    }

    public boolean delete(String maSV) {
        if (maSV == null) return false;
        return list.removeIf(sv -> sv.getMaSV().equalsIgnoreCase(maSV.trim()));
    }

    // Bài 10: Lọc danh sách theo Họ tên hoặc Lớp học
    public List<SinhVien> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        String kw = keyword.trim().toLowerCase();
        return list.stream()
                .filter(sv -> sv.getHoTen().toLowerCase().contains(kw)
                        || (sv.getLopHoc() != null && sv.getLopHoc().toLowerCase().contains(kw))
                        || sv.getMaSV().toLowerCase().contains(kw)
                        || sv.getEmail().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }
}
