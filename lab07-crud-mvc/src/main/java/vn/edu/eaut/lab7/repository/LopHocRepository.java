package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.LopHoc;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LopHocRepository {
    private static List<LopHoc> list = new ArrayList<>();

    static {
        list.add(new LopHoc("LH01", "KTPM1", "Thầy A", 50));
        list.add(new LopHoc("LH02", "KTPM2", "Cô B", 45));
    }

    public List<LopHoc> findAll() { return list; }

    public LopHoc findById(String id) {
        return list.stream().filter(l -> l.getClassId().equals(id)).findFirst().orElse(null);
    }

    public List<LopHoc> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return list;
        String kw = keyword.toLowerCase();
        return list.stream()
                .filter(l -> l.getClassId().toLowerCase().contains(kw) || l.getClassName().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public void save(LopHoc l) {
        LopHoc exist = findById(l.getClassId());
        if (exist == null) {
            list.add(l);
        } else {
            exist.setClassName(l.getClassName());
            exist.setAdvisor(l.getAdvisor());
            exist.setStudentCount(l.getStudentCount());
        }
    }

    public void delete(String id) {
        list.removeIf(l -> l.getClassId().equals(id));
    }
}
