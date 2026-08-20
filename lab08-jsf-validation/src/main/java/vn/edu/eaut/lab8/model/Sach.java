package vn.edu.eaut.lab8.model;

import java.io.Serializable;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Sach implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Mã sách không được để trống!")
    private String maSach;

    @NotBlank(message = "Tên sách không được để trống!")
    @Size(min = 2, max = 100, message = "Tên sách phải từ 2 đến 100 ký tự!")
    private String tenSach;

    @NotBlank(message = "Tác giả không được để trống!")
    @Size(min = 2, max = 50, message = "Tên tác giả phải từ 2 đến 50 ký tự!")
    private String tacGia;

    @NotNull(message = "Năm xuất bản không được để trống!")
    @Min(value = 1800, message = "Năm xuất bản không hợp lệ (phải từ năm 1800 trở về sau)!")
    @Max(value = 2026, message = "Năm xuất bản không được vượt quá năm hiện tại (2026)!")
    private Integer namXuatBan;

    @NotNull(message = "Giá bán không được để trống!")
    @DecimalMin(value = "0.0", message = "Giá bán phải lớn hơn hoặc bằng 0!")
    private Double giaBan;

    @NotBlank(message = "Vui lòng chọn thể loại sách!")
    private String theLoai;

    public Sach() {
    }

    public Sach(String maSach, String tenSach, String tacGia, Integer namXuatBan, Double giaBan, String theLoai) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.namXuatBan = namXuatBan;
        this.giaBan = giaBan;
        this.theLoai = theLoai;
    }

    // Getters & Setters
    public String getMaSach() { return maSach; }
    public void setMaSach(String maSach) { this.maSach = maSach; }

    public String getTenSach() { return tenSach; }
    public void setTenSach(String tenSach) { this.tenSach = tenSach; }

    public String getTacGia() { return tacGia; }
    public void setTacGia(String tacGia) { this.tacGia = tacGia; }

    public Integer getNamXuatBan() { return namXuatBan; }
    public void setNamXuatBan(Integer namXuatBan) { this.namXuatBan = namXuatBan; }

    public Double getGiaBan() { return giaBan; }
    public void setGiaBan(Double giaBan) { this.giaBan = giaBan; }

    public String getTheLoai() { return theLoai; }
    public void setTheLoai(String theLoai) { this.theLoai = theLoai; }
}
