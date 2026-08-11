package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileLineCounterFrame extends JFrame {
    private JButton btnSelectFile;
    private JButton btnCount;
    private JLabel lblFilePath;
    private JLabel lblResult;
    private JProgressBar progressBar;
    private File selectedFile;

    public FileLineCounterFrame() {
        setTitle("Đếm số dòng trong file");
        setSize(500, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        
        JPanel buttonPanel = new JPanel();
        btnSelectFile = new JButton("Chọn file");
        btnCount = new JButton("Đếm dòng");
        btnCount.setEnabled(false);
        buttonPanel.add(btnSelectFile);
        buttonPanel.add(btnCount);

        lblFilePath = new JLabel("Đường dẫn file: Chưa chọn file");
        
        topPanel.add(buttonPanel);
        topPanel.add(lblFilePath);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        lblResult = new JLabel("Số dòng: 0", SwingConstants.CENTER);
        lblResult.setFont(new Font("Arial", Font.BOLD, 14));

        centerPanel.add(progressBar);
        centerPanel.add(lblResult);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        btnSelectFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(FileLineCounterFrame.this) == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    lblFilePath.setText("Đường dẫn file: " + selectedFile.getAbsolutePath());
                    btnCount.setEnabled(true);
                    lblResult.setText("Số dòng: 0");
                    progressBar.setValue(0);
                }
            }
        });

        btnCount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    countLines();
                }
            }
        });
    }

    private void countLines() {
        btnSelectFile.setEnabled(false);
        btnCount.setEnabled(false);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        lblResult.setText("Đang đếm...");

        SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {
            @Override
            protected Integer doInBackground() throws Exception {
                int lines = 0;
                long totalBytes = selectedFile.length();
                long readBytes = 0;
                
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(selectedFile), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines++;
                        readBytes += line.getBytes(StandardCharsets.UTF_8).length + System.lineSeparator().getBytes().length;
                        int progress = (int) ((double) readBytes / totalBytes * 100);
                        setProgress(Math.min(100, progress));
                    }
                }
                return lines;
            }

            @Override
            protected void done() {
                btnSelectFile.setEnabled(true);
                btnCount.setEnabled(true);
                progressBar.setValue(100);
                try {
                    int lines = get();
                    lblResult.setText("Số dòng: " + lines);
                } catch (Exception ex) {
                    lblResult.setText("Lỗi khi đọc file!");
                }
            }
        };

        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((Integer) evt.getNewValue());
            }
        });

        worker.execute();
    }
}
