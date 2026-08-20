package vn.edu.eaut.lab8.bean;

import vn.edu.eaut.lab8.model.Sach;
import vn.edu.eaut.lab8.repository.SachRepository;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "sachBean")
@SessionScoped
public class SachBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private SachRepository repository;
    private Sach sach;
    private List<Sach> danhSachSach;
    private String searchKeyword;
    private boolean editMode;

    @PostConstruct
    public void init() {
        repository = new SachRepository();
        sach = new Sach();
        editMode = false;
        loadData();
    }

    public void loadData() {
        danhSachSach = repository.search(searchKeyword);
    }

    public String prepareAdd() {
        sach = new Sach();
        editMode = false;
        return "sach-form?faces-redirect=true";
    }

    public String prepareEdit(String maSach) {
        Sach existing = repository.findById(maSach);
        if (existing != null) {
            sach = new Sach(existing.getMaSach(), existing.getTenSach(), existing.getTacGia(), existing.getNamXuatBan(), existing.getGiaBan(), existing.getTheLoai());
            editMode = true;
            return "sach-form?faces-redirect=true";
        }
        return null;
    }

    public String save() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!editMode && repository.findById(sach.getMaSach()) != null) {
            context.addMessage("sachForm:maSach", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mã sách đã tồn tại!", null));
            return null;
        }

        repository.save(sach);
        String msg = editMode ? "Cập nhật thông tin sách thành công!" : "Thêm sách mới thành công!";
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Thành công", msg));
        context.getExternalContext().getFlash().setKeepMessages(true);
        sach = new Sach();
        editMode = false;
        loadData();
        return "sach-list?faces-redirect=true";
    }

    public String delete(String maSach) {
        repository.delete(maSach);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Thành công", "Đã xóa sách mã " + maSach));
        context.getExternalContext().getFlash().setKeepMessages(true);
        loadData();
        return "sach-list?faces-redirect=true";
    }

    public String search() { loadData(); return null; }
    public String cancel() { sach = new Sach(); editMode = false; return "sach-list?faces-redirect=true"; }

    // Getters & Setters
    public Sach getSach() { return sach; }
    public void setSach(Sach sach) { this.sach = sach; }

    public List<Sach> getDanhSachSach() { return danhSachSach; }
    public void setDanhSachSach(List<Sach> danhSachSach) { this.danhSachSach = danhSachSach; }

    public String getSearchKeyword() { return searchKeyword; }
    public void setSearchKeyword(String searchKeyword) { this.searchKeyword = searchKeyword; }

    public boolean isEditMode() { return editMode; }
    public boolean getEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }
}
