package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.Sach;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SachRepository {
    private static List<Sach> list = new ArrayList<>();

    static {
        list.add(new Sach("S01", "Lập trình Java", "Nguyễn Văn A", "NXB IT", 2021));
        list.add(new Sach("S02", "Cấu trúc dữ liệu", "Lê Thị B", "NXB GD", 2019));
    }

    public List<Sach> findAll() { return list; }

    public Sach findById(String id) {
        return list.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Sach> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return list;
        String kw = keyword.toLowerCase();
        return list.stream()
                .filter(s -> s.getName().toLowerCase().contains(kw) || s.getAuthor().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public void save(Sach s) {
        Sach exist = findById(s.getId());
        if (exist == null) {
            list.add(s);
        } else {
            exist.setName(s.getName());
            exist.setAuthor(s.getAuthor());
            exist.setPublisher(s.getPublisher());
            exist.setPublishYear(s.getPublishYear());
        }
    }

    public void delete(String id) {
        list.removeIf(s -> s.getId().equals(id));
    }
}
