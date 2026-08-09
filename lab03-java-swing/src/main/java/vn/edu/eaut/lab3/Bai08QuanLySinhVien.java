package vn.edu.eaut.lab3;

import javax.swing.*;
import java.awt.*;

public class Bai08QuanLySinhVien extends JFrame {
    private JTextField txtId, txtName, txtMark;
    private JTable table;
    private StudentTableModel tableModel;

    public Bai08QuanLySinhVien() {
        setTitle("Quản lý sinh viên (MVC Cơ bản)");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // --- Input Panel ---
        JPanel pnlInput = new JPanel(new GridLayout(3, 2, 5, 5));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));
        
        pnlInput.add(new JLabel("Mã SV:"));
        txtId = new JTextField();
        pnlInput.add(txtId);
        
        pnlInput.add(new JLabel("Họ tên:"));
        txtName = new JTextField();
        pnlInput.add(txtName);
        
        pnlInput.add(new JLabel("Điểm trung bình:"));
        txtMark = new JTextField();
        pnlInput.add(txtMark);
        
        // --- Buttons Panel ---
        JPanel pnlButtons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear = new JButton("Làm mới");
        pnlButtons.add(btnAdd);
        pnlButtons.add(btnEdit);
        pnlButtons.add(btnDelete);
        pnlButtons.add(btnClear);
        
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.add(pnlInput, BorderLayout.CENTER);
        pnlTop.add(pnlButtons, BorderLayout.SOUTH);
        
        add(pnlTop, BorderLayout.NORTH);
        
        // --- Table Panel ---
        tableModel = new StudentTableModel();
        table = new JTable(tableModel);
        // Ngăn việc sửa trực tiếp trên bảng
        table.setDefaultEditor(Object.class, null);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        // --- Events ---
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Student sv = tableModel.getStudentAt(row);
                txtId.setText(sv.getId());
                txtName.setText(sv.getName());
                txtMark.setText(String.valueOf(sv.getDiemTrungBinh()));
            }
        });
        
        btnAdd.addActionListener(e -> addStudent());
        btnEdit.addActionListener(e -> editStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearForm());
    }

    private void addStudent() {
        if (txtId.getText().trim().isEmpty() || txtName.getText().trim().isEmpty() || txtMark.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin!");
            return;
        }
        try {
            double mark = Double.parseDouble(txtMark.getText().trim());
            if (mark < 0 || mark > 10) {
                JOptionPane.showMessageDialog(this, "Điểm phải nằm trong khoảng từ 0 đến 10!");
                return;
            }
            Student sv = new Student(txtId.getText().trim(), txtName.getText().trim(), mark);
            tableModel.addStudent(sv);
            clearForm();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Điểm trung bình phải là số hợp lệ!");
        }
    }

    private void editStudent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên để sửa từ bảng!");
            return;
        }
        try {
            double mark = Double.parseDouble(txtMark.getText().trim());
            if (mark < 0 || mark > 10) {
                JOptionPane.showMessageDialog(this, "Điểm phải nằm trong khoảng từ 0 đến 10!");
                return;
            }
            Student sv = new Student(txtId.getText().trim(), txtName.getText().trim(), mark);
            tableModel.updateStudent(row, sv);
            clearForm();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Điểm trung bình phải là số hợp lệ!");
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên để xóa từ bảng!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sinh viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeStudent(row);
            clearForm();
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtMark.setText("");
        table.clearSelection();
        txtId.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bai08QuanLySinhVien().setVisible(true));
    }
}
