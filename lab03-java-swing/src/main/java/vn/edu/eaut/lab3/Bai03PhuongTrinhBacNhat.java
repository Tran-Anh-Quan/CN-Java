package vn.edu.eaut.lab3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bai03PhuongTrinhBacNhat extends JFrame {

    private JTextField txtHeSoA;
    private JTextField txtHeSoB;
    private JLabel lblKetQua;
    private JButton btnGiaiPT;
    private JButton btnLamMoi;

    public Bai03PhuongTrinhBacNhat() {
        setTitle("Giải phương trình bậc nhất (ax + b = 0)");
        setSize(450, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel chứa các ô nhập liệu và nhãn
        JPanel pnlCenter = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pnlCenter.add(new JLabel("Hệ số a:"));
        txtHeSoA = new JTextField();
        pnlCenter.add(txtHeSoA);

        pnlCenter.add(new JLabel("Hệ số b:"));
        txtHeSoB = new JTextField();
        pnlCenter.add(txtHeSoB);

        pnlCenter.add(new JLabel("Kết quả:"));
        lblKetQua = new JLabel("");
        lblKetQua.setForeground(Color.BLUE);
        lblKetQua.setFont(new Font("Arial", Font.BOLD, 14));
        pnlCenter.add(lblKetQua);

        add(pnlCenter, BorderLayout.CENTER);

        // Panel chứa các nút bấm
        JPanel pnlBottom = new JPanel();
        btnGiaiPT = new JButton("Giải phương trình");
        btnLamMoi = new JButton("Làm mới");
        pnlBottom.add(btnGiaiPT);
        pnlBottom.add(btnLamMoi);

        add(pnlBottom, BorderLayout.SOUTH);

        // Xử lý sự kiện nút Giải phương trình
        btnGiaiPT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                giaiPhuongTrinh();
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

    private void giaiPhuongTrinh() {
        try {
            String strA = txtHeSoA.getText().trim();
            String strB = txtHeSoB.getText().trim();

            if (strA.isEmpty() || strB.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ hệ số a và b!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double a = Double.parseDouble(strA);
            double b = Double.parseDouble(strB);

            if (a == 0) {
                if (b == 0) {
                    lblKetQua.setText("Vô số nghiệm");
                } else {
                    lblKetQua.setText("Vô nghiệm");
                }
            } else {
                double x = -b / a;
                // Hiển thị x = ... với 4 chữ số thập phân (như ví dụ 3.0000)
                lblKetQua.setText(String.format(java.util.Locale.US, "x = %.4f", x));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng nhập đúng định dạng số!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            lblKetQua.setText("");
        }
    }

    private void lamMoi() {
        txtHeSoA.setText("");
        txtHeSoB.setText("");
        lblKetQua.setText("");
        txtHeSoA.requestFocus();
    }

    public static void main(String[] args) {
        // Đảm bảo GUI được tạo trên Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Bai03PhuongTrinhBacNhat().setVisible(true);
            }
        });
    }
}
