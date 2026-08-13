package vn.edu.eaut.lab5.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("MiniShop - Quản lý bán hàng");
        setSize(980, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        SanPhamPanel sanPhamPanel = new SanPhamPanel();
        KhachHangPanel khachHangPanel = new KhachHangPanel();
        HoaDonPanel hoaDonPanel = new HoaDonPanel();
        ThongKePanel thongKePanel = new ThongKePanel();

        hoaDonPanel.setSanPhamPanel(sanPhamPanel);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Sản phẩm", sanPhamPanel);
        tabbedPane.addTab("Khách hàng", khachHangPanel);
        tabbedPane.addTab("Hóa đơn", hoaDonPanel);
        tabbedPane.addTab("Thống kê", thongKePanel);

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == hoaDonPanel) {
                hoaDonPanel.refreshComboData();
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
}
