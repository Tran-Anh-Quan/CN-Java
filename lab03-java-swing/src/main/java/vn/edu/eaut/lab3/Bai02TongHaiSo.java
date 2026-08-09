package vn.edu.eaut.lab3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bai02TongHaiSo extends JFrame {

    private JTextField txtSoThuNhat;
    private JTextField txtSoThuHai;
    private JLabel lblKetQua;
    private JButton btnTinhTong;
    private JButton btnLamMoi;

    public Bai02TongHaiSo() {
        setTitle("Tính tổng hai số");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel chứa các ô nhập liệu và nhãn
        JPanel pnlCenter = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pnlCenter.add(new JLabel("Số thứ nhất:"));
        txtSoThuNhat = new JTextField();
        pnlCenter.add(txtSoThuNhat);

        pnlCenter.add(new JLabel("Số thứ hai:"));
        txtSoThuHai = new JTextField();
        pnlCenter.add(txtSoThuHai);

        pnlCenter.add(new JLabel("Kết quả:"));
        lblKetQua = new JLabel("");
        lblKetQua.setForeground(Color.BLUE);
        lblKetQua.setFont(new Font("Arial", Font.BOLD, 14));
        pnlCenter.add(lblKetQua);

        add(pnlCenter, BorderLayout.CENTER);

        // Panel chứa các nút bấm
        JPanel pnlBottom = new JPanel();
        btnTinhTong = new JButton("Tính tổng");
        btnLamMoi = new JButton("Làm mới");
        pnlBottom.add(btnTinhTong);
        pnlBottom.add(btnLamMoi);

        add(pnlBottom, BorderLayout.SOUTH);

        // Xử lý sự kiện nút Tính tổng
        btnTinhTong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tinhTong();
            }
        });

        // Xử lý sự kiện nút Làm mới
        btnLamMoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lamMoi();
            }
        });
    }

    private void tinhTong() {
        try {
            String str1 = txtSoThuNhat.getText().trim();
            String str2 = txtSoThuHai.getText().trim();

            if (str1.isEmpty() || str2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ hai số!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double so1 = Double.parseDouble(str1);
            double so2 = Double.parseDouble(str2);
            double tong = so1 + so2;
            
            lblKetQua.setText(String.valueOf(tong));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng nhập đúng định dạng số!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            lblKetQua.setText("");
        }
    }

    private void lamMoi() {
        txtSoThuNhat.setText("");
        txtSoThuHai.setText("");
        lblKetQua.setText("");
        txtSoThuNhat.requestFocus();
    }

    public static void main(String[] args) {
        // Đảm bảo GUI được tạo trên Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Bai02TongHaiSo().setVisible(true);
            }
        });
    }
}
