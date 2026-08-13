package vn.edu.eaut.lab5.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import vn.edu.eaut.lab5.bus.SanPhamBUS;
import vn.edu.eaut.lab5.model.SanPham;
import vn.edu.eaut.lab5.util.MessageUtil;

public class SanPhamPanel extends JPanel {
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS();

    private final JTextField txtMaSp = new JTextField(8);
    private final JTextField txtTenSp = new JTextField(20);
    private final JTextField txtDonGia = new JTextField(12);
    private final JTextField txtSoLuong = new JTextField(8);
    private final JTextField txtTimKiem = new JTextField(15);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Ma SP", "Ten SP", "Don gia", "So luong"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public SanPhamPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtMaSp.setEditable(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Mã SP:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMaSp, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Tên SP:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtTenSp, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Đơn giá:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtDonGia, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtSoLuong, gbc);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                fillFormFromTable(table.getSelectedRow());
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");
        JButton btnTimKiem = new JButton("Tìm kiếm");
        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);
        buttonPanel.add(new JLabel("Từ khóa:"));
        buttonPanel.add(txtTimKiem);
        buttonPanel.add(btnTimKiem);

        btnThem.addActionListener(e -> saveProduct(false));
        btnSua.addActionListener(e -> saveProduct(true));
        btnXoa.addActionListener(e -> deleteProduct());
        btnLamMoi.addActionListener(e -> clearForm());
        btnTimKiem.addActionListener(e -> searchProducts());

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadDataAsync();
    }

    public void refreshData() {
        loadDataAsync();
    }

    private void loadDataAsync() {
        new SwingWorker<List<SanPham>, Void>() {
            @Override
            protected List<SanPham> doInBackground() throws Exception {
                return sanPhamBUS.findAll();
            }

            @Override
            protected void done() {
                try {
                    fillTable(get());
                } catch (Exception ex) {
                    MessageUtil.showError(SanPhamPanel.this, "Lỗi tải dữ liệu: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void fillTable(List<SanPham> list) {
        tableModel.setRowCount(0);
        for (SanPham sp : list) {
            tableModel.addRow(new Object[]{
                    sp.getMaSp(),
                    sp.getTenSp(),
                    sp.getDonGia(),
                    sp.getSoLuong()
            });
        }
    }

    private void fillFormFromTable(int row) {
        txtMaSp.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        txtTenSp.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtDonGia.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        txtSoLuong.setText(String.valueOf(tableModel.getValueAt(row, 3)));
    }

    private SanPham readForm(boolean requireId) {
        SanPham sp = new SanPham();
        if (requireId && !txtMaSp.getText().trim().isEmpty()) {
            sp.setMaSp(Integer.parseInt(txtMaSp.getText().trim()));
        }
        sp.setTenSp(txtTenSp.getText().trim());
        sp.setDonGia(new BigDecimal(txtDonGia.getText().trim()));
        sp.setSoLuong(Integer.parseInt(txtSoLuong.getText().trim()));
        return sp;
    }

    private void saveProduct(boolean isUpdate) {
        try {
            SanPham sp = readForm(isUpdate);
            if (sanPhamBUS.save(sp)) {
                MessageUtil.showInfo(this, isUpdate ? "Cập nhật sản phẩm thành công" : "Thêm sản phẩm thành công");
                clearForm();
                loadDataAsync();
            }
        } catch (NumberFormatException ex) {
            MessageUtil.showError(this, "Đơn giá và số lượng phải là số hợp lệ");
        } catch (Exception ex) {
            MessageUtil.showError(this, ex.getMessage());
        }
    }

    private void deleteProduct() {
        if (txtMaSp.getText().trim().isEmpty()) {
            MessageUtil.showError(this, "Hãy chọn sản phẩm cần xóa");
            return;
        }
        if (!MessageUtil.confirm(this, "Bạn có chắc muốn xóa sản phẩm này?")) {
            return;
        }
        try {
            int maSp = Integer.parseInt(txtMaSp.getText().trim());
            if (sanPhamBUS.delete(maSp)) {
                MessageUtil.showInfo(this, "Xóa sản phẩm thành công");
                clearForm();
                loadDataAsync();
            }
        } catch (Exception ex) {
            MessageUtil.showError(this, ex.getMessage());
        }
    }

    private void searchProducts() {
        new SwingWorker<List<SanPham>, Void>() {
            @Override
            protected List<SanPham> doInBackground() throws Exception {
                return sanPhamBUS.searchByName(txtTimKiem.getText().trim());
            }

            @Override
            protected void done() {
                try {
                    fillTable(get());
                } catch (Exception ex) {
                    MessageUtil.showError(SanPhamPanel.this, "Lỗi tìm kiếm: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void clearForm() {
        txtMaSp.setText("");
        txtTenSp.setText("");
        txtDonGia.setText("");
        txtSoLuong.setText("");
        table.clearSelection();
    }
}
