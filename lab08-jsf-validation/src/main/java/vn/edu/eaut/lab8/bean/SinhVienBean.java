package vn.edu.eaut.lab8.bean;

import vn.edu.eaut.lab8.model.SinhVien;
import vn.edu.eaut.lab8.repository.SinhVienRepository;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "sinhVienBean")
@SessionScoped
public class SinhVienBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private SinhVienRepository repository;
    private SinhVien sinhVien;
    private List<SinhVien> danhSachSinhVien;
    private String searchKeyword;
    private boolean editMode;

    @PostConstruct
    public void init() {
        repository = new SinhVienRepository();
        sinhVien = new SinhVien();
        editMode = false;
        loadData();
    }

    public void loadData() {
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            danhSachSinhVien = repository.search(searchKeyword);
        } else {
            danhSachSinhVien = repository.findAll();
        }
    }

    public String prepareAdd() {
        sinhVien = new SinhVien();
        editMode = false;
        return "sinhvien-form?faces-redirect=true";
    }

    public String prepareEdit(String maSV) {
        SinhVien existing = repository.findById(maSV);
        if (existing != null) {
            // Create a clone/copy so editing can be cancelled without affecting list
            sinhVien = new SinhVien(
                    existing.getMaSV(),
                    existing.getHoTen(),
                    existing.getEmail(),
                    existing.getSoDienThoai(),
                    existing.getDiemTB(),
                    existing.getChuyenNganh(),
                    existing.getLopHoc(),
                    existing.getGioiTinh()
            );
            editMode = true;
            return "sinhvien-form?faces-redirect=true";
        }
        addMessage(FacesMessage.SEVERITY_ERROR, "Lỗi", "Không tìm thấy sinh viên có mã: " + maSV);
        return null;
    }

    public String save() {
        FacesContext context = FacesContext.getCurrentInstance();

        // Check duplicate maSV if adding new
        if (!editMode && repository.findById(sinhVien.getMaSV()) != null) {
            context.addMessage("sinhVienForm:maSV",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mã sinh viên đã tồn tại!", "Mã sinh viên " + sinhVien.getMaSV() + " đã tồn tại trong hệ thống."));
            return null;
        }

        boolean success = repository.save(sinhVien);
        if (success) {
            String msg = editMode ? "Cập nhật sinh viên thành công!" : "Thêm mới sinh viên thành công!";
            addMessage(FacesMessage.SEVERITY_INFO, "Thành công", msg);
            context.getExternalContext().getFlash().setKeepMessages(true);
            sinhVien = new SinhVien();
            editMode = false;
            loadData();
            return "sinhvien-list?faces-redirect=true";
        } else {
            addMessage(FacesMessage.SEVERITY_ERROR, "Thất bại", "Không thể lưu thông tin sinh viên!");
            return null;
        }
    }

    public String delete(String maSV) {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean deleted = repository.delete(maSV);
        if (deleted) {
            addMessage(FacesMessage.SEVERITY_INFO, "Thành công", "Đã xóa sinh viên mã " + maSV + "!");
            context.getExternalContext().getFlash().setKeepMessages(true);
            loadData();
        } else {
            addMessage(FacesMessage.SEVERITY_ERROR, "Lỗi", "Không thể xóa sinh viên mã " + maSV + "!");
        }
        return "sinhvien-list?faces-redirect=true";
    }

    public String search() {
        loadData();
        return null;
    }

    public String clearSearch() {
        searchKeyword = null;
        loadData();
        return null;
    }

    public String cancel() {
        sinhVien = new SinhVien();
        editMode = false;
        return "sinhvien-list?faces-redirect=true";
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    // Metrics for dashboard view
    public int getTotalStudents() {
        return repository.findAll().size();
    }

    public double getAverageGpa() {
        List<SinhVien> list = repository.findAll();
        if (list.isEmpty()) return 0.0;
        double sum = list.stream().filter(sv -> sv.getDiemTB() != null).mapToDouble(SinhVien::getDiemTB).sum();
        return Math.round((sum / list.size()) * 100.0) / 100.0;
    }

    public long getExcellentCount() {
        return repository.findAll().stream().filter(sv -> sv.getDiemTB() != null && sv.getDiemTB() >= 8.5).count();
    }

    // Getters & Setters
    public SinhVien getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }

    public List<SinhVien> getDanhSachSinhVien() {
        return danhSachSinhVien;
    }

    public void setDanhSachSinhVien(List<SinhVien> danhSachSinhVien) {
        this.danhSachSinhVien = danhSachSinhVien;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public boolean getEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
}
