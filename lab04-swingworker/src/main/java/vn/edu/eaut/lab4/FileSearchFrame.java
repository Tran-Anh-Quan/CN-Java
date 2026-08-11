package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileSearchFrame extends JFrame {
    private JButton btnSelectFile;
    private JTextField txtKeyword;
    private JButton btnSearch;
    private JLabel lblStatus;
    private JProgressBar progressBar;
    private JTextArea txtResult;
    private File selectedFile;
    private SwingWorker<Void, String> worker;

    public FileSearchFrame() {
        setTitle("Tìm kiếm từ khóa trong file");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnSelectFile = new JButton("Chọn file");
        txtKeyword = new JTextField(15);
        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setEnabled(false);
        
        topPanel.add(btnSelectFile);
        topPanel.add(new JLabel("Từ khóa:"));
        topPanel.add(txtKeyword);
        topPanel.add(btnSearch);
        
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        txtResult = new JTextArea();
        txtResult.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResult);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        lblStatus = new JLabel("Chọn một file để bắt đầu");
        bottomPanel.add(progressBar, BorderLayout.NORTH);
        bottomPanel.add(lblStatus, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        btnSelectFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                lblStatus.setText("Đã chọn file: " + selectedFile.getName());
                btnSearch.setEnabled(true);
            }
        });
        
        btnSearch.addActionListener(e -> startSearch());
    }
    
    private void startSearch() {
        String keyword = txtKeyword.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa");
            return;
        }
        
        btnSelectFile.setEnabled(false);
        btnSearch.setEnabled(false);
        txtKeyword.setEnabled(false);
        txtResult.setText("");
        progressBar.setValue(0);
        lblStatus.setText("Đang tìm kiếm...");
        
        String lowerKeyword = keyword.toLowerCase();
        
        worker = new SwingWorker<Void, String>() {
            int matchCount = 0;
            
            @Override
            protected Void doInBackground() throws Exception {
                long totalBytes = selectedFile.length();
                long readBytes = 0;
                
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(selectedFile), StandardCharsets.UTF_8))) {
                    String line;
                    int lineNumber = 0;
                    while ((line = reader.readLine()) != null) {
                        lineNumber++;
                        readBytes += line.getBytes(StandardCharsets.UTF_8).length + System.lineSeparator().getBytes().length;
                        
                        if (line.toLowerCase().contains(lowerKeyword)) {
                            matchCount++;
                            publish("Dòng " + lineNumber + ": " + line);
                        }
                        
                        int progress = (int) ((double) readBytes / totalBytes * 100);
                        setProgress(Math.min(100, progress));
                    }
                }
                return null;
            }
            
            @Override
            protected void process(List<String> chunks) {
                for (String chunk : chunks) {
                    txtResult.append(chunk + "\n");
                }
            }
            
            @Override
            protected void done() {
                btnSelectFile.setEnabled(true);
                btnSearch.setEnabled(true);
                txtKeyword.setEnabled(true);
                progressBar.setValue(100);
                
                try {
                    get();
                    lblStatus.setText("Tìm thấy " + matchCount + " kết quả.");
                } catch (Exception ex) {
                    lblStatus.setText("Lỗi trong quá trình đọc file.");
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
