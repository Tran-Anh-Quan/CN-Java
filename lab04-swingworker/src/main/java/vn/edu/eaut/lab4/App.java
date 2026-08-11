package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    public App() {
        setTitle("Lab 4 - SwingWorker");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JButton btn1 = new JButton("Bài 1: Đồng hồ đếm ngược");
        btn1.addActionListener(e -> new CountdownFrame().setVisible(true));
        
        JButton btn2 = new JButton("Bài 2 & 6: Tiến trình tải dữ liệu (Có nút Hủy)");
        btn2.addActionListener(e -> new ProgressDemoFrame().setVisible(true));
        
        JButton btn3 = new JButton("Bài 3: Tổng số nguyên tố");
        btn3.addActionListener(e -> new PrimeSumFrame().setVisible(true));
        
        JButton btn4 = new JButton("Bài 4: Fibonacci");
        btn4.addActionListener(e -> new FibonacciFrame().setVisible(true));
        
        JButton btn5 = new JButton("Bài 5: Đếm dòng File");
        btn5.addActionListener(e -> new FileLineCounterFrame().setVisible(true));
        
        JButton btn7 = new JButton("Bài 7: Tìm kiếm trong File");
        btn7.addActionListener(e -> new FileSearchFrame().setVisible(true));
        
        JButton btn8 = new JButton("Bài 8: Đọc và thống kê điểm Sinh viên");
        btn8.addActionListener(e -> new StudentGradeFrame().setVisible(true));
        
        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);
        panel.add(btn4);
        panel.add(btn5);
        panel.add(btn7);
        panel.add(btn8);
        
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new App().setVisible(true);
        });
    }
}
