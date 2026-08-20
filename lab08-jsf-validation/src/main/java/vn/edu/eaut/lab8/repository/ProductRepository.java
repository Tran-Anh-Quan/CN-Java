package vn.edu.eaut.lab8.repository;

import vn.edu.eaut.lab8.model.Product;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductRepository implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final List<Product> list = new ArrayList<>();

    static {
        list.add(new Product("SP001", "Laptop Dell XPS 15", 35000000.0, 10, "Điện tử & Máy tính"));
        list.add(new Product("SP002", "Bàn Phím Cơ Keychron K2", 1950000.0, 25, "Phụ kiện máy tính"));
        list.add(new Product("SP003", "Chuột Logitech MX Master 3S", 2450000.0, 15, "Phụ kiện máy tính"));
    }

    public List<Product> findAll() { return new ArrayList<>(list); }

    public Product findById(String id) {
        if (id == null) return null;
        return list.stream().filter(p -> p.getId().equalsIgnoreCase(id.trim())).findFirst().orElse(null);
    }

    public boolean save(Product p) {
        if (p == null || p.getId() == null) return false;
        Product existing = findById(p.getId());
        if (existing != null) {
            existing.setName(p.getName());
            existing.setPrice(p.getPrice());
            existing.setQuantity(p.getQuantity());
            existing.setCategory(p.getCategory());
        } else {
            list.add(p);
        }
        return true;
    }

    public boolean delete(String id) {
        if (id == null) return false;
        return list.removeIf(p -> p.getId().equalsIgnoreCase(id.trim()));
    }

    public List<Product> search(String kw) {
        if (kw == null || kw.trim().isEmpty()) return findAll();
        String k = kw.trim().toLowerCase();
        return list.stream()
                .filter(p -> p.getName().toLowerCase().contains(k) || p.getCategory().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }
}
