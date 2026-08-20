package vn.edu.eaut.lab8.repository;

import vn.edu.eaut.lab8.model.Sach;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SachRepository implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final List<Sach> list = new ArrayList<>();

    static {
        list.add(new Sach("MS001", "Lập Trình Java Căn Bản", "Nguyễn Văn Hùng", 2021, 150000.0, "Công nghệ thông tin"));
        list.add(new Sach("MS002", "Cấu Trúc Dữ Liệu & Giải Thuật", "Trần Minh Tâm", 2022, 185000.0, "Công nghệ thông tin"));
        list.add(new Sach("MS003", "Thiết Kế Web Với JSF & Jakarta EE", "Phạm Hoàng Nam", 2023, 210000.0, "Lập trình Web"));
    }

    public List<Sach> findAll() { return new ArrayList<>(list); }

    public Sach findById(String maSach) {
        if (maSach == null) return null;
        return list.stream().filter(s -> s.getMaSach().equalsIgnoreCase(maSach.trim())).findFirst().orElse(null);
    }

    public boolean save(Sach s) {
        if (s == null || s.getMaSach() == null) return false;
        Sach existing = findById(s.getMaSach());
        if (existing != null) {
            existing.setTenSach(s.getTenSach());
            existing.setTacGia(s.getTacGia());
            existing.setNamXuatBan(s.getNamXuatBan());
            existing.setGiaBan(s.getGiaBan());
            existing.setTheLoai(s.getTheLoai());
        } else {
            list.add(s);
        }
        return true;
    }

    public boolean delete(String maSach) {
        if (maSach == null) return false;
        return list.removeIf(s -> s.getMaSach().equalsIgnoreCase(maSach.trim()));
    }

    public List<Sach> search(String kw) {
        if (kw == null || kw.trim().isEmpty()) return findAll();
        String k = kw.trim().toLowerCase();
        return list.stream()
                .filter(s -> s.getTenSach().toLowerCase().contains(k) || s.getTacGia().toLowerCase().contains(k) || s.getTheLoai().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }
}
