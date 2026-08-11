package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FibonacciFrame extends JFrame {
    private JTextField txtN;
    private JButton btnFind;
    private JTextArea txtResult;
    private JProgressBar progressBar;

    public FibonacciFrame() {
        setTitle("Tìm số Fibonacci");
        setSize(450, 300);
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
        btnFind = new JButton("Tìm");
        topPanel.add(btnFind);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        txtResult = new JTextArea(5, 20);
        txtResult.setLineWrap(true);
        txtResult.setWrapStyleWord(true);
        txtResult.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResult);

        centerPanel.add(progressBar, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        btnFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findFibonacci();
            }
        });
    }

    private void findFibonacci() {
        try {
            int n = Integer.parseInt(txtN.getText().trim());
            if (n < 0) {
                JOptionPane.showMessageDialog(this, "N phải >= 0");
                return;
            }

            btnFind.setEnabled(false);
            txtN.setEnabled(false);
            progressBar.setValue(0);
            txtResult.setText("Đang tính toán...");

            SwingWorker<BigInteger, Integer> worker = new SwingWorker<BigInteger, Integer>() {
                private Map<Integer, BigInteger> memo = new HashMap<>();

                @Override
                protected BigInteger doInBackground() throws Exception {
                    BigInteger a = BigInteger.ZERO;
                    BigInteger b = BigInteger.ONE;
                    memo.put(0, a);
                    memo.put(1, b);
                    if (n == 0) return a;
                    if (n == 1) return b;
                    
                    for (int i = 2; i <= n; i++) {
                        BigInteger c = a.add(b);
                        memo.put(i, c);
                        a = b;
                        b = c;
                        
                        // Cập nhật tiến độ
                        int progress = (int) (((double) i / n) * 100);
                        if (i % 1000 == 0 || i == n) {
                            publish(progress);
                        }
                    }
                    return memo.get(n);
                }

                @Override
                protected void process(List<Integer> chunks) {
                    int progress = chunks.get(chunks.size() - 1);
                    progressBar.setValue(progress);
                }

                @Override
                protected void done() {
                    btnFind.setEnabled(true);
                    txtN.setEnabled(true);
                    progressBar.setValue(100);
                    try {
                        BigInteger result = get();
                        txtResult.setText("Fibonacci(" + n + ") = \n" + result.toString());
                    } catch (Exception ex) {
                        txtResult.setText("Lỗi xảy ra!");
                    }
                }
            };

            worker.execute();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên hợp lệ!");
        }
    }
}
