package vn.edu.eaut.lab4;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class StudentGradeFrame extends JFrame {
    private JButton btnSelectFile;
    private JLabel lblStatus;
    private JProgressBar progressBar;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea txtStats;
    private File selectedFile;

    public StudentGradeFrame() {
        setTitle("Đọc và Thống kê Điểm Sinh Viên (CSV)");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnSelectFile = new JButton("Chọn file CSV");
        topPanel.add(btnSelectFile);

        tableModel = new DefaultTableModel(new String[]{"Mã SV", "Họ Tên", "Điểm"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        lblStatus = new JLabel("Chọn một file CSV để bắt đầu đọc.");
        txtStats = new JTextArea(4, 20);
        txtStats.setEditable(false);
        txtStats.setFont(new Font("Arial", Font.BOLD, 12));
        
        JPanel bottomInfoPanel = new JPanel(new BorderLayout());
        bottomInfoPanel.add(progressBar, BorderLayout.NORTH);
        bottomInfoPanel.add(lblStatus, BorderLayout.CENTER);
        
        bottomPanel.add(bottomInfoPanel, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(txtStats), BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnSelectFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                processCSV();
            }
        });
    }
    
    private static class Student {
        String id;
        String name;
        double score;
        Student(String id, String name, double score) {
            this.id = id; this.name = name; this.score = score;
        }
    }

    private void processCSV() {
        btnSelectFile.setEnabled(false);
        tableModel.setRowCount(0);
        txtStats.setText("");
        progressBar.setValue(0);
        lblStatus.setText("Đang đọc file...");

        SwingWorker<Void, Student> worker = new SwingWorker<Void, Student>() {
            double totalScore = 0;
            double maxScore = -1;
            int count = 0;
            Student topStudent = null;

            @Override
            protected Void doInBackground() throws Exception {
                long totalBytes = selectedFile.length();
                long readBytes = 0;
                
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(selectedFile), StandardCharsets.UTF_8))) {
                    String line;
                    boolean isFirstLine = true;
                    while ((line = reader.readLine()) != null) {
                        readBytes += line.getBytes(StandardCharsets.UTF_8).length + System.lineSeparator().getBytes().length;
                        if (isFirstLine) { // Bỏ qua header
                            isFirstLine = false;
                            continue;
                        }
                        
                        String[] parts = line.split(",");
                        if (parts.length >= 3) {
                            try {
                                String id = parts[0].trim();
                                String name = parts[1].trim();
                                double score = Double.parseDouble(parts[2].trim());
                                Student s = new Student(id, name, score);
                                
                                count++;
                                totalScore += score;
                                if (score > maxScore) {
                                    maxScore = score;
                                    topStudent = s;
                                }
                                
                                publish(s);
                                // Giả lập độ trễ nếu file quá nhỏ để thấy thanh tiến trình chạy
                                Thread.sleep(20);
                            } catch (NumberFormatException ignored) {}
                        }
                        
                        int progress = (int) ((double) readBytes / totalBytes * 100);
                        setProgress(Math.min(100, progress));
                    }
                }
                return null;
            }

            @Override
            protected void process(List<Student> chunks) {
                for (Student s : chunks) {
                    tableModel.addRow(new Object[]{s.id, s.name, s.score});
                }
            }

            @Override
            protected void done() {
                btnSelectFile.setEnabled(true);
                progressBar.setValue(100);
                try {
                    get();
                    lblStatus.setText("Hoàn thành đọc file.");
                    
                    if (count > 0) {
                        double avg = totalScore / count;
                        String stats = String.format("Tổng số sinh viên: %d\nĐiểm trung bình: %.2f\nSinh viên điểm cao nhất: %s (%.2f điểm)",
                                count, avg, topStudent.name, topStudent.score);
                        txtStats.setText(stats);
                    } else {
                        txtStats.setText("Không có dữ liệu hợp lệ.");
                    }
                } catch (Exception ex) {
                    lblStatus.setText("Lỗi đọc file: " + ex.getMessage());
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
