package vn.edu.eaut.lab5.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.text.AbstractDocument;

import vn.edu.eaut.lab5.bus.KhachHangBUS;
import vn.edu.eaut.lab5.model.KhachHang;
import vn.edu.eaut.lab5.util.MessageUtil;
import vn.edu.eaut.lab5.util.PhoneDocumentFilter;

public class KhachHangPanel extends JPanel {
    private final KhachHangBUS khachHangBUS = new KhachHangBUS();

    private final JTextField txtMaKh = new JTextField(8);
    private final JTextField txtTenKh = new JTextField(20);
    private final JTextField txtSdt = new JTextField(12);
    private final JTextField txtDiaChi = new JTextField(25);
    private final JTextField txtTimKiem = new JTextField(15);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Ma KH", "Ten KH", "SDT", "Dia chi"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public KhachHangPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtMaKh.setEditable(false);
        ((AbstractDocument) txtSdt.getDocument()).setDocumentFilter(new PhoneDocumentFilter());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Mã KH:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMaKh, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Tên KH:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtTenKh, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("SDT:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtSdt, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtDiaChi, gbc);

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

        btnThem.addActionListener(e -> saveCustomer(false));
        btnSua.addActionListener(e -> saveCustomer(true));
        btnXoa.addActionListener(e -> deleteCustomer());
        btnLamMoi.addActionListener(e -> clearForm());
        btnTimKiem.addActionListener(e -> searchCustomers());

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadDataAsync();
    }

    public List<KhachHang> loadCustomersSync() throws Exception {
        return khachHangBUS.findAll();
    }

    private void loadDataAsync() {
        new SwingWorker<List<KhachHang>, Void>() {
            @Override
            protected List<KhachHang> doInBackground() throws Exception {
                return khachHangBUS.findAll();
            }

            @Override
            protected void done() {
                try {
                    fillTable(get());
                } catch (Exception ex) {
                    MessageUtil.showError(KhachHangPanel.this, "Lỗi tải dữ liệu: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void fillTable(List<KhachHang> list) {
        tableModel.setRowCount(0);
        for (KhachHang kh : list) {
            tableModel.addRow(new Object[]{
                    kh.getMaKh(),
                    kh.getTenKh(),
                    kh.getSdt(),
                    kh.getDiaChi()
            });
        }
    }

    private void fillFormFromTable(int row) {
        txtMaKh.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        txtTenKh.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtSdt.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        txtDiaChi.setText(String.valueOf(tableModel.getValueAt(row, 3)));
    }

    private KhachHang readForm(boolean requireId) {
        KhachHang kh = new KhachHang();
        if (requireId && !txtMaKh.getText().trim().isEmpty()) {
            kh.setMaKh(Integer.parseInt(txtMaKh.getText().trim()));
        }
        kh.setTenKh(txtTenKh.getText().trim());
        kh.setSdt(txtSdt.getText().trim());
        kh.setDiaChi(txtDiaChi.getText().trim());
        return kh;
    }

    private void saveCustomer(boolean isUpdate) {
        try {
            KhachHang kh = readForm(isUpdate);
            if (khachHangBUS.save(kh)) {
                MessageUtil.showInfo(this, isUpdate ? "Cập nhật khách hàng thành công" : "Thêm khách hàng thành công");
                clearForm();
                loadDataAsync();
            }
        } catch (Exception ex) {
            MessageUtil.showError(this, ex.getMessage());
        }
    }

    private void deleteCustomer() {
        if (txtMaKh.getText().trim().isEmpty()) {
            MessageUtil.showError(this, "Hãy chọn khách hàng cần xóa");
            return;
        }
        if (!MessageUtil.confirm(this, "Bạn có chắc muốn xóa khách hàng này?")) {
            return;
        }
        try {
            int maKh = Integer.parseInt(txtMaKh.getText().trim());
            if (khachHangBUS.delete(maKh)) {
                MessageUtil.showInfo(this, "Xóa khách hàng thành công");
                clearForm();
                loadDataAsync();
            }
        } catch (Exception ex) {
            MessageUtil.showError(this, ex.getMessage());
        }
    }

    private void searchCustomers() {
        new SwingWorker<List<KhachHang>, Void>() {
            @Override
            protected List<KhachHang> doInBackground() throws Exception {
                return khachHangBUS.search(txtTimKiem.getText().trim());
            }

            @Override
            protected void done() {
                try {
                    fillTable(get());
                } catch (Exception ex) {
                    MessageUtil.showError(KhachHangPanel.this, "Lỗi tìm kiếm: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void clearForm() {
        txtMaKh.setText("");
        txtTenKh.setText("");
        txtSdt.setText("");
        txtDiaChi.setText("");
        table.clearSelection();
    }
}
