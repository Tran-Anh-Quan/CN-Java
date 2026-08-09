package vn.edu.eaut.lab3;

import javax.swing.*;
import java.awt.*;

public class Bai05FibonacciSwing extends JFrame {
    private JTextField txtN;
    private JTextArea txtAreaResult;

    public Bai05FibonacciSwing() {
        setTitle("Dãy số Fibonacci");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout(10, 10));
        
        JPanel pnlTop = new JPanel();
        pnlTop.add(new JLabel("Nhập n (<= 92):"));
        txtN = new JTextField(10);
        pnlTop.add(txtN);
        JButton btnShow = new JButton("Hiển thị");
        pnlTop.add(btnShow);
        
        add(pnlTop, BorderLayout.NORTH);
        
        txtAreaResult = new JTextArea();
        txtAreaResult.setEditable(false);
        txtAreaResult.setLineWrap(true);
        txtAreaResult.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtAreaResult);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Kết quả"));
        add(scrollPane, BorderLayout.CENTER);
        
        btnShow.addActionListener(e -> showFibonacci());
    }

    private void showFibonacci() {
        try {
            int n = Integer.parseInt(txtN.getText().trim());
            if (n <= 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập n là số nguyên dương!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (n > 92) {
                JOptionPane.showMessageDialog(this, "n không được vượt quá 92 để tránh tràn kiểu long!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            long[] f = new long[n];
            if (n >= 1) f[0] = 0;
            if (n >= 2) f[1] = 1;
            
            StringBuilder sb = new StringBuilder();
            if (n >= 1) sb.append(f[0]);
            if (n >= 2) sb.append(" ").append(f[1]);
            
            for (int i = 2; i < n; i++) {
                f[i] = f[i-1] + f[i-2];
                sb.append(" ").append(f[i]);
            }
            
            txtAreaResult.setText(sb.toString());
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bai05FibonacciSwing().setVisible(true));
    }
}
