package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.DiemSinhVien;
import java.util.ArrayList;
import java.util.List;

public class DiemSinhVienRepository {
    private static List<DiemSinhVien> list = new ArrayList<>();

    static {
        list.add(new DiemSinhVien("D01", "SV01", 10, 8, 7.5));
        list.add(new DiemSinhVien("D02", "SV02", 9, 6, 5));
    }

    public List<DiemSinhVien> findAll() { return list; }

    public DiemSinhVien findById(String id) {
        return list.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    public void save(DiemSinhVien d) {
        DiemSinhVien exist = findById(d.getId());
        if (exist == null) {
            list.add(d);
        } else {
            exist.setStudentId(d.getStudentId());
            exist.setDiemCC(d.getDiemCC());
            exist.setDiemGK(d.getDiemGK());
            exist.setDiemCK(d.getDiemCK());
        }
    }

    public void delete(String id) {
        list.removeIf(d -> d.getId().equals(id));
    }
}
