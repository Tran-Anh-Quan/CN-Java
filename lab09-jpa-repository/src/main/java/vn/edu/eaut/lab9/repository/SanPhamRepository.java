package vn.edu.eaut.lab9.repository;

import vn.edu.eaut.lab9.model.SanPham;

public class SanPhamRepository extends BaseRepository<SanPham, String> {
    public SanPhamRepository() {
        super(SanPham.class);
    }
}
