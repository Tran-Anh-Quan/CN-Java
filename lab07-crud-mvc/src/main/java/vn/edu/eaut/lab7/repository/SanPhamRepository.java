package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.SanPham;
import java.util.ArrayList;
import java.util.List;

public class SanPhamRepository {
    private static List<SanPham> list = new ArrayList<>();

    static {
        list.add(new SanPham("SP01", "Laptop Dell", "Core i5, 8GB RAM", 15000000, 10));
        list.add(new SanPham("SP02", "Chuột Logitech", "Không dây", 300000, 50));
    }

    public List<SanPham> findAll() { return list; }

    public SanPham findById(String id) {
        return list.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public void save(SanPham p) {
        SanPham exist = findById(p.getId());
        if (exist == null) {
            list.add(p);
        } else {
            exist.setName(p.getName());
            exist.setDescription(p.getDescription());
            exist.setPrice(p.getPrice());
            exist.setQuantity(p.getQuantity());
        }
    }

    public void delete(String id) {
        list.removeIf(p -> p.getId().equals(id));
    }
}
