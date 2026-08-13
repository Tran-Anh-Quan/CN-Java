package vn.edu.eaut.lab5.model;

public class SanPhamBanChay {
    private int maSp;
    private String tenSp;
    private long tongSoLuong;

    public SanPhamBanChay() {
    }

    public SanPhamBanChay(int maSp, String tenSp, long tongSoLuong) {
        this.maSp = maSp;
        this.tenSp = tenSp;
        this.tongSoLuong = tongSoLuong;
    }

    public int getMaSp() {
        return maSp;
    }

    public void setMaSp(int maSp) {
        this.maSp = maSp;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public long getTongSoLuong() {
        return tongSoLuong;
    }

    public void setTongSoLuong(long tongSoLuong) {
        this.tongSoLuong = tongSoLuong;
    }
}
