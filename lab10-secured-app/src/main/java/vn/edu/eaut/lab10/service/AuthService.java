package vn.edu.eaut.lab10.service;

import org.mindrot.jbcrypt.BCrypt;
import vn.edu.eaut.lab10.model.AuditLog;
import vn.edu.eaut.lab10.model.Role;
import vn.edu.eaut.lab10.model.User;
import vn.edu.eaut.lab10.repository.AuditLogRepository;
import vn.edu.eaut.lab10.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();
    private final AuditLogRepository auditLogRepository = new AuditLogRepository();

    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        Optional<User> userOpt = userRepository.findByUsername(username.trim());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isActive() && checkPassword(password, user.getPassword())) {
                logAction(user.getUsername(), "LOGIN", "Đăng nhập thành công vào hệ thống");
                return Optional.of(user);
            } else if (!user.isActive()) {
                logAction(user.getUsername(), "LOGIN_FAILED", "Thất bại: Tài khoản đã bị khóa");
            } else {
                logAction(username, "LOGIN_FAILED", "Thất bại: Mật khẩu không chính xác");
            }
        } else {
            logAction(username, "LOGIN_FAILED", "Thất bại: Username không tồn tại");
        }
        return Optional.empty();
    }

    public User registerUser(String username, String rawPassword, String fullName, String email, String roleName) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Tên đăng nhập '" + username + "' đã tồn tại.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email '" + email + "' đã tồn tại.");
        }

        Role role = userRepository.findRoleByName(roleName)
                .orElseGet(() -> userRepository.saveRole(new Role(roleName, "Role " + roleName)));

        User user = User.builder()
                .username(username.trim())
                .password(hashPassword(rawPassword))
                .fullName(fullName.trim())
                .email(email.trim())
                .active(true)
                .roles(new HashSet<>())
                .build();

        user.getRoles().add(role);
        User savedUser = userRepository.save(user);

        logAction(username, "REGISTER", "Tạo tài khoản mới thành công với vai trò: " + roleName);
        return savedUser;
    }

    public void updateProfile(Long userId, String fullName, String email, String actorUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng."));

        Optional<User> existingEmailUser = userRepository.findByEmail(email.trim());
        if (existingEmailUser.isPresent() && !existingEmailUser.get().getId().equals(userId)) {
            throw new IllegalArgumentException("Email '" + email + "' đã được sử dụng bởi tài khoản khác.");
        }

        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        userRepository.save(user);

        logAction(actorUsername, "UPDATE_PROFILE", "Cập nhật thông tin cá nhân cho " + user.getUsername());
    }

    public void changePassword(Long userId, String oldPassword, String newPassword, String confirmPassword, String actorUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng."));

        if (!checkPassword(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu hiện tại không chính xác.");
        }

        if (newPassword == null || newPassword.length() < 4) {
            throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 4 ký tự.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không trùng khớp.");
        }

        user.setPassword(hashPassword(newPassword));
        userRepository.save(user);

        logAction(actorUsername, "CHANGE_PASSWORD", "Đổi mật khẩu thành công cho tài khoản " + user.getUsername());
    }

    public void updateUserByAdmin(Long userId, String fullName, String email, String roleName, boolean active, String adminUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng id=" + userId));

        Optional<User> existingEmailUser = userRepository.findByEmail(email.trim());
        if (existingEmailUser.isPresent() && !existingEmailUser.get().getId().equals(userId)) {
            throw new IllegalArgumentException("Email '" + email + "' đã được sử dụng bởi tài khoản khác.");
        }

        Role role = userRepository.findRoleByName(roleName)
                .orElseGet(() -> userRepository.saveRole(new Role(roleName, "Role " + roleName)));

        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        user.setActive(active);
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        if ("ROLE_ADMIN".equals(roleName)) {
            userRepository.findRoleByName("ROLE_USER").ifPresent(roles::add);
        }
        user.setRoles(roles);

        userRepository.save(user);
        logAction(adminUsername, "ADMIN_UPDATE_USER", "Admin cập nhật tài khoản #" + userId + " (" + user.getUsername() + ")");
    }

    public void toggleUserActiveStatus(Long userId, String adminUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng."));

        user.setActive(!user.isActive());
        userRepository.save(user);

        String actionName = user.isActive() ? "UNLOCK_USER" : "LOCK_USER";
        logAction(adminUsername, actionName, (user.isActive() ? "Mở khóa" : "Khóa") + " tài khoản " + user.getUsername());
    }

    public String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }

    public boolean checkPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || hashedPassword == null) {
            return false;
        }

        // 1. Check plain text match
        if (plainTextPassword.equals(hashedPassword)) {
            return true;
        }

        // 2. Check BCrypt hash match
        try {
            if (hashedPassword.startsWith("$2a$") || hashedPassword.startsWith("$2b$") || hashedPassword.startsWith("$2y$")) {
                return BCrypt.checkpw(plainTextPassword, hashedPassword);
            }
        } catch (Exception e) {
            // Ignore BCrypt parse error
        }

        return false;
    }

    public void logAction(String username, String action, String details) {
        try {
            AuditLog log = AuditLog.builder()
                    .username(username != null ? username : "ANONYMOUS")
                    .action(action)
                    .details(details)
                    .build();
            auditLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Audit log error: " + e.getMessage());
        }
    }

    public void initSeedData() {
        try {
            Role adminRole = userRepository.findRoleByName("ROLE_ADMIN")
                    .orElseGet(() -> userRepository.saveRole(new Role("ROLE_ADMIN", "Quyền Quản trị viên hệ thống")));

            Role staffRole = userRepository.findRoleByName("ROLE_STAFF")
                    .orElseGet(() -> userRepository.saveRole(new Role("ROLE_STAFF", "Quyền Nhân viên nghiệp vụ")));

            Role userRole = userRepository.findRoleByName("ROLE_USER")
                    .orElseGet(() -> userRepository.saveRole(new Role("ROLE_USER", "Quyền Người dùng thông thường")));

            // Seed / Sync ADMIN (admin / admin123)
            Optional<User> adminOpt = userRepository.findByUsername("admin");
            if (adminOpt.isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .password(hashPassword("admin123"))
                        .fullName("Quản Trị Viên Hợp Nhất")
                        .email("admin@eaut.edu.vn")
                        .active(true)
                        .roles(new HashSet<>())
                        .build();
                admin.getRoles().add(adminRole);
                admin.getRoles().add(staffRole);
                admin.getRoles().add(userRole);
                userRepository.save(admin);
                logAction("SYSTEM", "SEED_DATA", "Tạo tài khoản ADMIN mặc định: admin / admin123");
            } else {
                User admin = adminOpt.get();
                if (!checkPassword("admin123", admin.getPassword())) {
                    admin.setPassword(hashPassword("admin123"));
                    userRepository.save(admin);
                }
            }

            // Seed / Sync STAFF (staff / staff123)
            Optional<User> staffOpt = userRepository.findByUsername("staff");
            if (staffOpt.isEmpty()) {
                User staff = User.builder()
                        .username("staff")
                        .password(hashPassword("staff123"))
                        .fullName("Nhân Viên Nghiệp Vụ")
                        .email("staff@eaut.edu.vn")
                        .active(true)
                        .roles(new HashSet<>())
                        .build();
                staff.getRoles().add(staffRole);
                staff.getRoles().add(userRole);
                userRepository.save(staff);
                logAction("SYSTEM", "SEED_DATA", "Tạo tài khoản STAFF mặc định: staff / staff123");
            } else {
                User staff = staffOpt.get();
                if (!checkPassword("staff123", staff.getPassword())) {
                    staff.setPassword(hashPassword("staff123"));
                    userRepository.save(staff);
                }
            }

            // Seed / Sync USER (user / user123)
            Optional<User> userOpt = userRepository.findByUsername("user");
            if (userOpt.isEmpty()) {
                User user = User.builder()
                        .username("user")
                        .password(hashPassword("user123"))
                        .fullName("Nguyễn Văn A (User)")
                        .email("user@eaut.edu.vn")
                        .active(true)
                        .roles(new HashSet<>())
                        .build();
                user.getRoles().add(userRole);
                userRepository.save(user);
                logAction("SYSTEM", "SEED_DATA", "Tạo tài khoản USER mặc định: user / user123");
            } else {
                User user = userOpt.get();
                if (!checkPassword("user123", user.getPassword())) {
                    user.setPassword(hashPassword("user123"));
                    userRepository.save(user);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi seed dữ liệu ban đầu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
