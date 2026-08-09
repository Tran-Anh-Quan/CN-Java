package vn.edu.eaut.lab3;

import javax.swing.*;
import java.awt.*;

public class Bai06LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRole;
    private JCheckBox chkShowPassword;

    public Bai06LoginForm() {
        setTitle("Form đăng nhập");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        JPanel pnlCenter = new JPanel(new GridLayout(4, 2, 10, 10));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        pnlCenter.add(new JLabel("Tài khoản:"));
        txtUsername = new JTextField();
        pnlCenter.add(txtUsername);
        
        pnlCenter.add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        pnlCenter.add(txtPassword);
        
        pnlCenter.add(new JLabel("Vai trò:"));
        cbRole = new JComboBox<>(new String[]{"Admin", "User"});
        pnlCenter.add(cbRole);
        
        pnlCenter.add(new JLabel(""));
        chkShowPassword = new JCheckBox("Hiển thị mật khẩu");
        pnlCenter.add(chkShowPassword);
        
        add(pnlCenter, BorderLayout.CENTER);
        
        JPanel pnlBottom = new JPanel();
        JButton btnLogin = new JButton("Đăng nhập");
        pnlBottom.add(btnLogin);
        add(pnlBottom, BorderLayout.SOUTH);
        
        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•'); // Default bullet character
            }
        });
        
        btnLogin.addActionListener(e -> login());
    }

    private void login() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String role = cbRole.getSelectedItem().toString();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        boolean isSuccess = false;
        if ("admin".equals(username) && "123456".equals(password) && "Admin".equals(role)) {
            isSuccess = true;
        } else if ("user".equals(username) && "123456".equals(password) && "User".equals(role)) {
            isSuccess = true;
        }
        
        if (isSuccess) {
            JOptionPane.showMessageDialog(this, "Chào mừng " + role + " " + username + " đăng nhập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Tài khoản, mật khẩu hoặc vai trò không đúng!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bai06LoginForm().setVisible(true));
    }
}
