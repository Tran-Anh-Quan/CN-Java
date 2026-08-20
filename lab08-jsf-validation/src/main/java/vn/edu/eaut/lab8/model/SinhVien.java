package vn.edu.eaut.lab8.model;

import java.io.Serializable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SinhVien implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Mã sinh viên không được để trống!")
    @Pattern(regexp = "^SV\\d{3,}$", message = "Mã sinh viên phải có định dạng SVxxx (ví dụ: SV001)!")
    private String maSV;

    @NotBlank(message = "Họ và tên không được để trống!")
    @Size(min = 2, max = 50, message = "Họ và tên phải có độ dài từ 2 đến 50 ký tự!")
    private String hoTen;

    @NotBlank(message = "Email không được để trống!")
    @Email(message = "Email không đúng định dạng chuẩn (ví dụ: student@eaut.edu.vn)!")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống!")
    @Pattern(regexp = "^0\\d{9,10}$", message = "Số điện thoại phải bắt đầu bằng số 0 và gồm 10 đến 11 chữ số!")
    private String soDienThoai;

    @NotNull(message = "Điểm trung bình không được để trống!")
    @DecimalMin(value = "0.0", message = "Điểm trung bình không được nhỏ hơn 0.0!")
    @DecimalMax(value = "10.0", message = "Điểm trung bình không được lớn hơn 10.0!")
    private Double diemTB;

    @NotBlank(message = "Vui lòng chọn chuyên ngành!")
    private String chuyenNganh;

    @NotBlank(message = "Vui lòng chọn lớp học!")
    private String lopHoc;

    @NotBlank(message = "Vui lòng chọn giới tính!")
    private String gioiTinh;

    public SinhVien() {
    }

    public SinhVien(String maSV, String hoTen, String email, String soDienThoai, Double diemTB, String chuyenNganh, String lopHoc, String gioiTinh) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diemTB = diemTB;
        this.chuyenNganh = chuyenNganh;
        this.lopHoc = lopHoc;
        this.gioiTinh = gioiTinh;
    }

    // Helper methods for classification
    public String getXepLoai() {
        if (diemTB == null) return "Chưa xếp loại";
        if (diemTB >= 8.5) return "Xuất sắc";
        if (diemTB >= 7.0) return "Giỏi";
        if (diemTB >= 5.5) return "Khá";
        if (diemTB >= 4.0) return "Trung bình";
        return "Yếu";
    }

    public String getBadgeClass() {
        if (diemTB == null) return "badge-secondary";
        if (diemTB >= 8.5) return "badge-success";
        if (diemTB >= 7.0) return "badge-info";
        if (diemTB >= 5.5) return "badge-primary";
        if (diemTB >= 4.0) return "badge-warning";
        return "badge-danger";
    }

    // Getters & Setters
    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public Double getDiemTB() {
        return diemTB;
    }

    public void setDiemTB(Double diemTB) {
        this.diemTB = diemTB;
    }

    public String getChuyenNganh() {
        return chuyenNganh;
    }

    public void setChuyenNganh(String chuyenNganh) {
        this.chuyenNganh = chuyenNganh;
    }

    public String getLopHoc() {
        return lopHoc;
    }

    public void setLopHoc(String lopHoc) {
        this.lopHoc = lopHoc;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }
}
