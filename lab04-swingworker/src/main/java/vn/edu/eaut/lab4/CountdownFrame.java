package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CountdownFrame extends JFrame {
    private JTextField txtSeconds;
    private JButton btnStart;
    private JLabel lblTime;

    public CountdownFrame() {
        setTitle("Đồng hồ đếm ngược");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout());

        JLabel lblInput = new JLabel("Nhập số giây:");
        txtSeconds = new JTextField(10);
        btnStart = new JButton("Bắt đầu");
        lblTime = new JLabel("Thời gian còn lại: 0", SwingConstants.CENTER);
        lblTime.setPreferredSize(new Dimension(250, 30));
        lblTime.setFont(new Font("Arial", Font.BOLD, 14));

        add(lblInput);
        add(txtSeconds);
        add(btnStart);
        add(lblTime);

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startCountdown();
            }
        });
    }

    private void startCountdown() {
        try {
            int seconds = Integer.parseInt(txtSeconds.getText().trim());
            if (seconds <= 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số giây lớn hơn 0!");
                return;
            }

            btnStart.setEnabled(false);
            txtSeconds.setEnabled(false);

            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                @Override
                protected Void doInBackground() throws Exception {
                    for (int i = seconds; i >= 0; i--) {
                        publish(i);
                        Thread.sleep(1000);
                    }
                    return null;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    int remaining = chunks.get(chunks.size() - 1);
                    lblTime.setText("Thời gian còn lại: " + remaining);
                }

                @Override
                protected void done() {
                    btnStart.setEnabled(true);
                    txtSeconds.setEnabled(true);
                    JOptionPane.showMessageDialog(CountdownFrame.this, "Đã hết thời gian!");
                }
            };
            
            worker.execute();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập một số nguyên hợp lệ!");
        }
    }
}
