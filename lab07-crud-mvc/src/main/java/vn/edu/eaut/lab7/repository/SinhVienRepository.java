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
        if (page < 1 || pageSize < 1) {
            return new ArrayList<>();
        }
        int fromIndex = (page - 1) * pageSize;
        if (fromIndex >= list.size()) {
            return new ArrayList<>();
        }
        return list.subList(fromIndex, Math.min(fromIndex + pageSize, list.size()));
    }

    public int getTotalPages(int pageSize) {
        return (int) Math.ceil((double) list.size() / pageSize);
    }

    public SinhVien findById(String id) {
        if (id == null) return null;
        for (SinhVien sv : list) {
            if (id.equals(sv.getId())) return sv;
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
        if (id != null) {
            list.removeIf(sv -> id.equals(sv.getId()));
        }
    }
}
