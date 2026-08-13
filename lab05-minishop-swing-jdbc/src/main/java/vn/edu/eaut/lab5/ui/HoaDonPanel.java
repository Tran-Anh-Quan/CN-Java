package vn.edu.eaut.lab5.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import vn.edu.eaut.lab5.bus.HoaDonBUS;
import vn.edu.eaut.lab5.bus.KhachHangBUS;
import vn.edu.eaut.lab5.bus.SanPhamBUS;
import vn.edu.eaut.lab5.model.ChiTietHoaDon;
import vn.edu.eaut.lab5.model.KhachHang;
import vn.edu.eaut.lab5.model.SanPham;
import vn.edu.eaut.lab5.util.MessageUtil;

public class HoaDonPanel extends JPanel {
    private final HoaDonBUS hoaDonBUS = new HoaDonBUS();
    private final KhachHangBUS khachHangBUS = new KhachHangBUS();
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS();

    private final JComboBox<KhachHang> cboKhachHang = new JComboBox<>();
    private final JComboBox<SanPham> cboSanPham = new JComboBox<>();
    private final JTextField txtSoLuong = new JTextField(8);
    private final JLabel lblTongTien = new JLabel("Tong tien: 0 VND");

    private final List<ChiTietHoaDon> chiTietTam = new ArrayList<>();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Ma SP", "Ten SP", "So luong", "Don gia", "Thanh tien"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    private SanPhamPanel sanPhamPanel;

    public HoaDonPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Lập hóa đơn"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Khách hàng:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(cboKhachHang, gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Sản phẩm:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(cboSanPham, gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 3;
        formPanel.add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 4;
        formPanel.add(txtSoLuong, gbc);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnThemDong = new JButton("Thêm dòng");
        JButton btnXoaDong = new JButton("Xóa dòng");
        JButton btnLuuHoaDon = new JButton("Lưu hóa đơn");
        JButton btnLamMoi = new JButton("Làm mới");
        actionPanel.add(btnThemDong);
        actionPanel.add(btnXoaDong);
        actionPanel.add(btnLuuHoaDon);
        actionPanel.add(btnLamMoi);
        actionPanel.add(lblTongTien);

        btnThemDong.addActionListener(e -> addLine());
        btnXoaDong.addActionListener(e -> removeLine());
        btnLuuHoaDon.addActionListener(e -> saveInvoice());
        btnLamMoi.addActionListener(e -> clearInvoice());

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        loadComboDataAsync();
    }

    public void setSanPhamPanel(SanPhamPanel sanPhamPanel) {
        this.sanPhamPanel = sanPhamPanel;
    }

    public void refreshComboData() {
        loadComboDataAsync();
    }

    private void loadComboDataAsync() {
        new SwingWorker<Void, Void>() {
            private List<KhachHang> khachHangs;
            private List<SanPham> sanPhams;

            @Override
            protected Void doInBackground() throws Exception {
                khachHangs = khachHangBUS.findAll();
                sanPhams = sanPhamBUS.findAll();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    cboKhachHang.setModel(new DefaultComboBoxModel<>(khachHangs.toArray(new KhachHang[0])));
                    cboSanPham.setModel(new DefaultComboBoxModel<>(sanPhams.toArray(new SanPham[0])));
                } catch (Exception ex) {
                    MessageUtil.showError(HoaDonPanel.this, "Lỗi tải dữ liệu: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void addLine() {
        KhachHang kh = (KhachHang) cboKhachHang.getSelectedItem();
        SanPham sp = (SanPham) cboSanPham.getSelectedItem();
        if (kh == null) {
            MessageUtil.showError(this, "Hãy chọn khách hàng");
            return;
        }
        if (sp == null) {
            MessageUtil.showError(this, "Hãy chọn sản phẩm");
            return;
        }
        try {
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                MessageUtil.showError(this, "Số lượng phải lớn hơn 0");
                return;
            }

            int daChonTrongHoaDon = 0;
            for (ChiTietHoaDon ct : chiTietTam) {
                if (ct.getMaSp() == sp.getMaSp()) {
                    daChonTrongHoaDon += ct.getSoLuong();
                }
            }

            int tongSoLuongCanMua = daChonTrongHoaDon + soLuong;
            if (tongSoLuongCanMua > sp.getSoLuong()) {
                MessageUtil.showError(this, "Số lượng mua vượt quá tồn kho hiện có");
                return;
            }

            for (ChiTietHoaDon ct : chiTietTam) {
                if (ct.getMaSp() == sp.getMaSp()) {
                    ct.setSoLuong(ct.getSoLuong() + soLuong);
                    refreshTable();
                    txtSoLuong.setText("");
                    return;
                }
            }

            ChiTietHoaDon ct = new ChiTietHoaDon(sp.getMaSp(), sp.getTenSp(), soLuong, sp.getDonGia());
            chiTietTam.add(ct);
            refreshTable();
            txtSoLuong.setText("");
        } catch (NumberFormatException ex) {
            MessageUtil.showError(this, "Số lượng phải là số hợp lệ");
        }
    }

    private void removeLine() {
        int row = table.getSelectedRow();
        if (row < 0) {
            MessageUtil.showError(this, "Hãy chọn dòng cần xóa");
            return;
        }
        chiTietTam.remove(row);
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        BigDecimal tong = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : chiTietTam) {
            tableModel.addRow(new Object[]{
                    ct.getMaSp(),
                    ct.getTenSp(),
                    ct.getSoLuong(),
                    ct.getDonGia(),
                    ct.getThanhTien()
            });
            tong = tong.add(ct.getThanhTien());
        }
        lblTongTien.setText("Tổng tiền: " + tong + " VND");
    }

    private void saveInvoice() {
        KhachHang kh = (KhachHang) cboKhachHang.getSelectedItem();
        if (kh == null) {
            MessageUtil.showError(this, "Hãy chọn khách hàng");
            return;
        }
        try {
            int maHd = hoaDonBUS.createInvoice(kh.getMaKh(), new ArrayList<>(chiTietTam));
            MessageUtil.showInfo(this, "Lưu hóa đơn thành công. Mã HD: " + maHd);
            clearInvoice();
            loadComboDataAsync();
            if (sanPhamPanel != null) {
                sanPhamPanel.refreshData();
            }
        } catch (Exception ex) {
            MessageUtil.showError(this, ex.getMessage());
        }
    }

    private void clearInvoice() {
        chiTietTam.clear();
        refreshTable();
        txtSoLuong.setText("");
        table.clearSelection();
    }
}
