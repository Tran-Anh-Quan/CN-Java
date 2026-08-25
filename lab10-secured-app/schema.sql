-- ============================================================
-- LAB 10: SECURED WEB APPLICATION DATABASE SCHEMA & SEED DATA
-- Database: lab10_db (MySQL 8.x)
-- ============================================================

CREATE DATABASE IF NOT EXISTS `lab10_db` 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE `lab10_db`;

-- 1. BẢNG ROLES (Vai trò hệ thống)
DROP TABLE IF EXISTS `user_roles`;
DROP TABLE IF EXISTS `audit_logs`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `roles`;

CREATE TABLE `roles` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL UNIQUE,
    `description` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. BẢNG USERS (Tài khoản người dùng)
CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) DEFAULT NULL,
    `active` TINYINT(1) NOT NULL DEFAULT 1,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. BẢNG USER_ROLES (Mối quan hệ Nhiều-Nhiều)
CREATE TABLE `user_roles` (
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    CONSTRAINT `fk_user_roles_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_user_roles_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. BẢNG AUDIT_LOGS (Ghi nhật ký hệ thống)
CREATE TABLE `audit_logs` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL,
    `action` VARCHAR(100) NOT NULL,
    `details` VARCHAR(255) DEFAULT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- SEED DATA (DỮ LIỆU MẪU BAN ĐẦU)
-- Mật khẩu mặc định:
-- admin -> admin123
-- staff -> staff123
-- user  -> user123
-- ============================================================

INSERT INTO `roles` (`id`, `name`, `description`) VALUES
(1, 'ROLE_ADMIN', 'Quyền Quản trị viên hệ thống'),
(2, 'ROLE_STAFF', 'Quyền Nhân viên nghiệp vụ'),
(3, 'ROLE_USER', 'Quyền Người dùng thông thường');

-- Thêm Users mẫu (Hệ thống hỗ trợ cả mật khẩu plain-text lẫn BCrypt)
INSERT INTO `users` (`id`, `username`, `password`, `full_name`, `email`, `active`, `created_at`) VALUES
(1, 'admin', 'admin123', 'Quản Trị Viên Hợp Nhất', 'admin@eaut.edu.vn', 1, NOW()),
(2, 'staff', 'staff123', 'Nhân Viên Nghiệp Vụ', 'staff@eaut.edu.vn', 1, NOW()),
(3, 'user',  'user123',  'Nguyễn Văn A (User)', 'user@eaut.edu.vn', 1, NOW());

-- Phân quyền cho các tài khoản mẫu
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(1, 1), (1, 2), (1, 3), -- Admin
(2, 2), (2, 3),         -- Staff
(3, 3);                 -- User

-- Log khởi tạo mẫu
INSERT INTO `audit_logs` (`username`, `action`, `details`, `created_at`) VALUES
('SYSTEM', 'SEED_DATA', 'Khởi tạo cơ sở dữ liệu và tài khoản mẫu Lab 10', NOW());
