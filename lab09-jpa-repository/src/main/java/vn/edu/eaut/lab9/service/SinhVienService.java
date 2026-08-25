package vn.edu.eaut.lab9.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.eaut.lab9.config.JPAUtil;
import vn.edu.eaut.lab9.model.Diem;
import vn.edu.eaut.lab9.model.MonHoc;
import vn.edu.eaut.lab9.model.SinhVien;
import vn.edu.eaut.lab9.repository.SinhVienRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class SinhVienService {
    
    private final SinhVienRepository repository = new SinhVienRepository();
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    public List<SinhVien> getAllSinhVien() {
        return repository.findAll();
    }

    public SinhVien getSinhVienById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void addSinhVien(SinhVien sinhVien) throws Exception {
        validateSinhVien(sinhVien, true);
        repository.save(sinhVien);
    }

    public void updateSinhVien(SinhVien sinhVien) throws Exception {
        validateSinhVien(sinhVien, false);
        repository.update(sinhVien);
    }

    public void deleteSinhVien(String id) {
        repository.delete(id);
    }

    public List<SinhVien> searchSinhVien(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll();
        }
        return repository.searchByName(keyword.trim());
    }

    public List<SinhVien> searchAndPaginate(String keyword, int page, int size) {
        return repository.searchAndPaginate(keyword, page, size);
    }

    // Bài 10: Ràng buộc dữ liệu & trùng mã
    private void validateSinhVien(SinhVien sinhVien, boolean isNew) throws Exception {
        if (sinhVien.getMaSv() == null || sinhVien.getMaSv().trim().isEmpty()) {
            throw new Exception("Mã sinh viên không được để trống!");
        }
        if (sinhVien.getHoTen() == null || sinhVien.getHoTen().trim().isEmpty()) {
            throw new Exception("Họ tên không được để trống!");
        }
        if (sinhVien.getEmail() == null || !Pattern.matches(EMAIL_PATTERN, sinhVien.getEmail())) {
            throw new Exception("Email không hợp lệ!");
        }

        if (isNew) {
            Optional<SinhVien> existing = repository.findById(sinhVien.getMaSv());
            if (existing.isPresent()) {
                throw new Exception("Mã sinh viên đã tồn tại trong hệ thống!");
            }
        }
    }

    // Bài 11: Transaction thêm sinh viên & điểm mặc định
    public void addSinhVienWithDefaultDiem(SinhVien sinhVien, MonHoc defaultMonHoc) throws Exception {
        validateSinhVien(sinhVien, true);

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // 1. Thêm sinh viên
            em.persist(sinhVien);

            // 2. Thêm điểm mặc định (Giả sử 0.0)
            if (defaultMonHoc != null) {
                Diem diem = new Diem();
                diem.setSinhVien(sinhVien);
                diem.setMonHoc(defaultMonHoc);
                diem.setDiemThi(0.0);
                em.persist(diem);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new Exception("Lỗi khi thêm sinh viên và điểm: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
