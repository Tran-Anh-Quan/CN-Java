package vn.edu.eaut.lab3;

import javax.swing.*;
import java.awt.*;

public class Bai07MayTinhMini extends JFrame {
    private JTextField txtSo1, txtSo2, txtKetQua;
    private JTextArea txtHistory;

    public Bai07MayTinhMini() {
        setTitle("Máy tính mini");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel pnlInput = new JPanel(new GridLayout(3, 2, 5, 5));
        pnlInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        pnlInput.add(new JLabel("Số thứ 1:"));
        txtSo1 = new JTextField();
        pnlInput.add(txtSo1);
        
        pnlInput.add(new JLabel("Số thứ 2:"));
        txtSo2 = new JTextField();
        pnlInput.add(txtSo2);
        
        pnlInput.add(new JLabel("Kết quả:"));
        txtKetQua = new JTextField();
        txtKetQua.setEditable(false);
        pnlInput.add(txtKetQua);
        
        JPanel pnlButtons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("+");
        JButton btnSub = new JButton("-");
        JButton btnMul = new JButton("*");
        JButton btnDiv = new JButton("/");
        JButton btnClear = new JButton("Clear");
        
        pnlButtons.add(btnAdd);
        pnlButtons.add(btnSub);
        pnlButtons.add(btnMul);
        pnlButtons.add(btnDiv);
        pnlButtons.add(btnClear);
        
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.add(pnlInput, BorderLayout.CENTER);
        pnlTop.add(pnlButtons, BorderLayout.SOUTH);
        
        add(pnlTop, BorderLayout.NORTH);
        
        txtHistory = new JTextArea();
        txtHistory.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtHistory);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lịch sử tính toán"));
        add(scrollPane, BorderLayout.CENTER);
        
        btnAdd.addActionListener(e -> calculate("+"));
        btnSub.addActionListener(e -> calculate("-"));
        btnMul.addActionListener(e -> calculate("*"));
        btnDiv.addActionListener(e -> calculate("/"));
        btnClear.addActionListener(e -> clear());
    }

    private void calculate(String operator) {
        try {
            double a = Double.parseDouble(txtSo1.getText().trim());
            double b = Double.parseDouble(txtSo2.getText().trim());
            double res = 0;
            
            if (operator.equals("/") && b == 0) {
                JOptionPane.showMessageDialog(this, "Lỗi: Không thể chia cho 0!", "Lỗi chia cho 0", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            switch (operator) {
                case "+": res = a + b; break;
                case "-": res = a - b; break;
                case "*": res = a * b; break;
                case "/": res = a / b; break;
            }
            
            txtKetQua.setText(String.format("%s", res));
            txtHistory.append(String.format("%s %s %s = %s\n", a, operator, b, res));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clear() {
        txtSo1.setText("");
        txtSo2.setText("");
        txtKetQua.setText("");
        txtSo1.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bai07MayTinhMini().setVisible(true));
    }
}
