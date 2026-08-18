package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.SinhVien;
import java.util.ArrayList;
import java.util.List;

public class SinhVienRepository {
    private static List<SinhVien> list = new ArrayList<>();

    static {
        for(int i=1; i<=12; i++) {
            list.add(new SinhVien("SV" + (i < 10 ? "0" + i : i), "Sinh Vien " + i, "sv" + i + "@gmail.com", "IT01"));
        }
    }

    public List<SinhVien> findAll() {
        return list;
    }

    public List<SinhVien> findAll(int page, int pageSize) {
        int fromIndex = (page - 1) * pageSize;
        if (list == null || list.size() <= fromIndex) {
            return new ArrayList<>();
        }
        return list.subList(fromIndex, Math.min(fromIndex + pageSize, list.size()));
    }

    public int getTotalPages(int pageSize) {
        return (int) Math.ceil((double) list.size() / pageSize);
    }

    public SinhVien findById(String id) {
        for (SinhVien sv : list) {
            if (sv.getId().equals(id)) return sv;
        }
        return null;
    }

    public void save(SinhVien sv) {
        SinhVien exist = findById(sv.getId());
        if (exist == null) {
            list.add(sv);
        } else {
            exist.setName(sv.getName());
            exist.setEmail(sv.getEmail());
            exist.setClassName(sv.getClassName());
        }
    }

    public void delete(String id) {
        list.removeIf(sv -> sv.getId().equals(id));
    }
}
