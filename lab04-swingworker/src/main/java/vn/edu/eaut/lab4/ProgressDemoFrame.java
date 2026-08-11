package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CancellationException;

public class ProgressDemoFrame extends JFrame {
    private JButton btnLoad;
    private JButton btnCancel;
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private SwingWorker<Void, Integer> worker;

    public ProgressDemoFrame() {
        setTitle("Mô phỏng tiến trình tải dữ liệu");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        btnLoad = new JButton("Tải dữ liệu");
        btnCancel = new JButton("Hủy");
        btnCancel.setEnabled(false);
        topPanel.add(btnLoad);
        topPanel.add(btnCancel);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        lblStatus = new JLabel("Trạng thái: Chưa bắt đầu", SwingConstants.CENTER);

        centerPanel.add(progressBar);
        centerPanel.add(lblStatus);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        btnLoad.addActionListener(e -> startLoading());

        btnCancel.addActionListener(e -> {
            if (worker != null && !worker.isDone()) {
                worker.cancel(true);
            }
        });
    }

    private void startLoading() {
        btnLoad.setEnabled(false);
        btnCancel.setEnabled(true);
        progressBar.setValue(0);
        lblStatus.setText("Trạng thái: Đang tải...");

        worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i <= 100; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    Thread.sleep(100); // Mô phỏng tải chậm
                    publish(i);
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                int progress = chunks.get(chunks.size() - 1);
                progressBar.setValue(progress);
            }

            @Override
            protected void done() {
                btnLoad.setEnabled(true);
                btnCancel.setEnabled(false);
                try {
                    get(); // Nếu bị hủy, hàm get() sẽ throw CancellationException
                    lblStatus.setText("Trạng thái: Hoàn tất!");
                    JOptionPane.showMessageDialog(ProgressDemoFrame.this, "Tải dữ liệu xong!");
                } catch (CancellationException ex) {
                    lblStatus.setText("Trạng thái: Đã hủy tác vụ");
                    JOptionPane.showMessageDialog(ProgressDemoFrame.this, "Đã hủy tác vụ!");
                } catch (Exception ex) {
                    lblStatus.setText("Trạng thái: Lỗi xảy ra");
                }
            }
        };

        worker.execute();
    }
}
