package vn.edu.eaut.lab3;

import javax.swing.*;
import java.awt.*;

public class Bai04TamGiacSwing extends JFrame {
    private JTextField txtA, txtB, txtC;
    private JLabel lblResult;

    public Bai04TamGiacSwing() {
        setTitle("Kiểm tra và phân loại tam giác");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());

        JPanel pnlCenter = new JPanel(new GridLayout(4, 2, 10, 10));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        pnlCenter.add(new JLabel("Cạnh a:"));
        txtA = new JTextField();
        pnlCenter.add(txtA);
        
        pnlCenter.add(new JLabel("Cạnh b:"));
        txtB = new JTextField();
        pnlCenter.add(txtB);
        
        pnlCenter.add(new JLabel("Cạnh c:"));
        txtC = new JTextField();
        pnlCenter.add(txtC);
        
        pnlCenter.add(new JLabel("Kết quả:"));
        lblResult = new JLabel("");
        lblResult.setForeground(Color.RED);
        pnlCenter.add(lblResult);
        
        add(pnlCenter, BorderLayout.CENTER);
        
        JPanel pnlBottom = new JPanel();
        JButton btnCheck = new JButton("Kiểm tra");
        pnlBottom.add(btnCheck);
        add(pnlBottom, BorderLayout.SOUTH);
        
        btnCheck.addActionListener(e -> checkTriangle());
    }

    private void checkTriangle() {
        try {
            double a = Double.parseDouble(txtA.getText().trim());
            double b = Double.parseDouble(txtB.getText().trim());
            double c = Double.parseDouble(txtC.getText().trim());
            
            if (a <= 0 || b <= 0 || c <= 0) {
                lblResult.setText("Các cạnh phải lớn hơn 0");
                return;
            }
            
            if (a + b > c && a + c > b && b + c > a) {
                boolean eqAB = Math.abs(a - b) < 1e-9;
                boolean eqBC = Math.abs(b - c) < 1e-9;
                boolean eqCA = Math.abs(c - a) < 1e-9;
                
                boolean isRight = Math.abs(a * a + b * b - c * c) < 1e-9 || 
                                  Math.abs(a * a + c * c - b * b) < 1e-9 || 
                                  Math.abs(b * b + c * c - a * a) < 1e-9;

                if (eqAB && eqBC) {
                    lblResult.setText("Tam giác đều");
                } else if (eqAB || eqBC || eqCA) {
                    if (isRight) {
                        lblResult.setText("Tam giác vuông cân");
                    } else {
                        lblResult.setText("Tam giác cân");
                    }
                } else if (isRight) {
                    lblResult.setText("Tam giác vuông");
                } else {
                    lblResult.setText("Tam giác thường");
                }
            } else {
                lblResult.setText("Không phải tam giác");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bai04TamGiacSwing().setVisible(true));
    }
}
