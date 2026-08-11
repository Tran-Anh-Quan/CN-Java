package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PrimeSumFrame extends JFrame {
    private JTextField txtN;
    private JButton btnCalculate;
    private JLabel lblResult;
    private JProgressBar progressBar;

    public PrimeSumFrame() {
        setTitle("Tính tổng số nguyên tố");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Nhập N:"));
        txtN = new JTextField(10);
        topPanel.add(txtN);
        btnCalculate = new JButton("Tính");
        topPanel.add(btnCalculate);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        lblResult = new JLabel("Kết quả: ", SwingConstants.CENTER);

        centerPanel.add(progressBar);
        centerPanel.add(lblResult);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        btnCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculatePrimeSum();
            }
        });
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private void calculatePrimeSum() {
        try {
            int n = Integer.parseInt(txtN.getText().trim());
            if (n <= 0) {
                JOptionPane.showMessageDialog(this, "N phải lớn hơn 0");
                return;
            }

            btnCalculate.setEnabled(false);
            txtN.setEnabled(false);
            progressBar.setValue(0);
            lblResult.setText("Đang tính toán...");

            SwingWorker<Long, Integer> worker = new SwingWorker<Long, Integer>() {
                @Override
                protected Long doInBackground() throws Exception {
                    long sum = 0;
                    for (int i = 0; i < n; i++) {
                        if (isPrime(i)) {
                            sum += i;
                        }
                        int progress = (int) (((double) i / n) * 100);
                        publish(progress);
                    }
                    publish(100);
                    return sum;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    int progress = chunks.get(chunks.size() - 1);
                    progressBar.setValue(progress);
                }

                @Override
                protected void done() {
                    btnCalculate.setEnabled(true);
                    txtN.setEnabled(true);
                    try {
                        long result = get();
                        lblResult.setText("Tổng: " + result);
                    } catch (Exception ex) {
                        lblResult.setText("Lỗi xảy ra!");
                    }
                }
            };

            worker.execute();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
        }
    }
}
