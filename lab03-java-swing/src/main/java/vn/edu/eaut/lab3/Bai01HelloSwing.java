package vn.edu.eaut.lab3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bai01HelloSwing extends JFrame {

    public Bai01HelloSwing() {
        setTitle("Hello Swing");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JLabel label = new JLabel("Nhập tên của bạn:");
        JTextField textField = new JTextField(15);
        JButton button = new JButton("Hiển thị");

        add(label);
        add(textField);
        add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(Bai01HelloSwing.this, "Vui lòng nhập tên!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(Bai01HelloSwing.this, "Xin chào, " + name + "!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Bai01HelloSwing().setVisible(true);
        });
    }
}
