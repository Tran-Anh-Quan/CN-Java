package vn.edu.eaut.lab5.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import vn.edu.eaut.lab5.bus.HoaDonBUS;
import vn.edu.eaut.lab5.bus.ThongKeBUS;
import vn.edu.eaut.lab5.model.HoaDon;
import vn.edu.eaut.lab5.model.SanPhamBanChay;
import vn.edu.eaut.lab5.ui.worker.DoanhThuWorker;
import vn.edu.eaut.lab5.ui.worker.HoaDonSearchWorker;
import vn.edu.eaut.lab5.util.MessageUtil;

public class ThongKePanel extends JPanel {
    private final ThongKeBUS thongKeBUS = new ThongKeBUS();
    private final HoaDonBUS hoaDonBUS = new HoaDonBUS();

    private final JTextField txtTuNgay = new JTextField(12);
    private final JTextField txtDenNgay = new JTextField(12);
    private final JTextField txtMaKh = new JTextField(8);
    private final JLabel lblDoanhThu = new JLabel("Doanh thu: ...");
    private final JLabel lblHoaDonCaoNhat = new JLabel("Hóa đơn cao nhất: ...");
    private final JLabel lblSanPhamBanChay = new JLabel("Sản phẩm bán chạy: ...");

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Ma HD", "Ngay lap", "Ma KH", "Ten KH", "Tong tien"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public ThongKePanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtTuNgay.setText(LocalDate.now().withDayOfMonth(1).toString());
        txtDenNgay.setText(LocalDate.now().toString());

        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm và thống kê"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(new JLabel("Từ ngày (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        filterPanel.add(txtTuNgay, gbc);
        gbc.gridx = 2;
        filterPanel.add(new JLabel("Đến ngày:"), gbc);
        gbc.gridx = 3;
        filterPanel.add(txtDenNgay, gbc);
        gbc.gridx = 4;
        filterPanel.add(new JLabel("Mã KH (0 = tất cả):"), gbc);
        gbc.gridx = 5;
        filterPanel.add(txtMaKh, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTimHoaDon = new JButton("Tìm hóa đơn");
        JButton btnDoanhThu = new JButton("Tính doanh thu");
        JButton btnHoaDonCaoNhat = new JButton("Hóa đơn cao nhất");
        JButton btnSanPhamBanChay = new JButton("Sản phẩm bán chạy");
        buttonPanel.add(btnTimHoaDon);
        buttonPanel.add(btnDoanhThu);
        buttonPanel.add(btnHoaDonCaoNhat);
        buttonPanel.add(btnSanPhamBanChay);

        JPanel resultPanel = new JPanel(new GridBagLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Kết quả thống kê"));
        GridBagConstraints rgbc = new GridBagConstraints();
        rgbc.insets = new Insets(4, 4, 4, 4);
        rgbc.anchor = GridBagConstraints.WEST;
        rgbc.gridx = 0;
        rgbc.gridy = 0;
        resultPanel.add(lblDoanhThu, rgbc);
        rgbc.gridy = 1;
        resultPanel.add(lblHoaDonCaoNhat, rgbc);
        rgbc.gridy = 2;
        resultPanel.add(lblSanPhamBanChay, rgbc);

        btnTimHoaDon.addActionListener(e -> searchInvoices());
        btnDoanhThu.addActionListener(e -> calculateRevenue());
        btnHoaDonCaoNhat.addActionListener(e -> loadHighestInvoice());
        btnSanPhamBanChay.addActionListener(e -> loadBestSellingProduct());

        JPanel north = new JPanel(new BorderLayout());
        north.add(filterPanel, BorderLayout.NORTH);
        north.add(buttonPanel, BorderLayout.CENTER);
        north.add(resultPanel, BorderLayout.SOUTH);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private LocalDate[] readDateRange() {
        try {
            LocalDate tuNgay = LocalDate.parse(txtTuNgay.getText().trim());
            LocalDate denNgay = LocalDate.parse(txtDenNgay.getText().trim());
            if (tuNgay.isAfter(denNgay)) {
                throw new IllegalArgumentException("Từ ngày không được sau đến ngày");
            }
            return new LocalDate[]{tuNgay, denNgay};
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Định dạng ngày phải là yyyy-MM-dd");
        }
    }

    private int readMaKh() {
        String value = txtMaKh.getText().trim();
        if (value.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    private void searchInvoices() {
        try {
            LocalDate[] range = readDateRange();
            int maKh = readMaKh();
            lblDoanhThu.setText("Doanh thu: đang tìm kiếm...");
            new HoaDonSearchWorker(
                    hoaDonBUS,
                    range[0],
                    range[1],
                    maKh,
                    tableModel,
                    ex -> MessageUtil.showError(ThongKePanel.this, "Lỗi tìm kiếm: " + ex.getMessage())
            ).execute();
        } catch (Exception ex) {
            MessageUtil.showError(this, ex.getMessage());
        }
    }

    private void calculateRevenue() {
        try {
            LocalDate[] range = readDateRange();
            lblDoanhThu.setText("Doanh thu: đang tính...");
            new DoanhThuWorker(range[0], range[1], thongKeBUS, lblDoanhThu).execute();
        } catch (Exception ex) {
            MessageUtil.showError(this, ex.getMessage());
        }
    }

    private void loadHighestInvoice() {
        lblHoaDonCaoNhat.setText("Hóa đơn cao nhất: đang tải...");
        new SwingWorker<Optional<HoaDon>, Void>() {
            @Override
            protected Optional<HoaDon> doInBackground() throws Exception {
                return thongKeBUS.hoaDonCaoNhat();
            }

            @Override
            protected void done() {
                try {
                    Optional<HoaDon> result = get();
                    if (result.isPresent()) {
                        HoaDon hd = result.get();
                        lblHoaDonCaoNhat.setText(String.format(
                                "Hóa đơn cao nhất: #%d - %s - KH: %s - %s VND",
                                hd.getMaHd(),
                                hd.getNgayLap(),
                                hd.getTenKh(),
                                hd.getTongTien()
                        ));
                    } else {
                        lblHoaDonCaoNhat.setText("Hóa đơn cao nhất: chưa có dữ liệu");
                    }
                } catch (Exception ex) {
                    MessageUtil.showError(ThongKePanel.this, "Lỗi thống kê: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void loadBestSellingProduct() {
        lblSanPhamBanChay.setText("Sản phẩm bán chạy: đang tải...");
        new SwingWorker<Optional<SanPhamBanChay>, Void>() {
            @Override
            protected Optional<SanPhamBanChay> doInBackground() throws Exception {
                return thongKeBUS.sanPhamBanChayNhat();
            }

            @Override
            protected void done() {
                try {
                    Optional<SanPhamBanChay> result = get();
                    if (result.isPresent()) {
                        SanPhamBanChay sp = result.get();
                        lblSanPhamBanChay.setText(String.format(
                                "Sản phẩm bán chạy: #%d - %s - đã bán %d",
                                sp.getMaSp(),
                                sp.getTenSp(),
                                sp.getTongSoLuong()
                        ));
                    } else {
                        lblSanPhamBanChay.setText("Sản phẩm bán chạy: chưa có dữ liệu");
                    }
                } catch (Exception ex) {
                    MessageUtil.showError(ThongKePanel.this, "Lỗi thống kê: " + ex.getMessage());
                }
            }
        }.execute();
    }
}
