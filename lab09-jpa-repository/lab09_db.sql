CREATE DATABASE IF NOT EXISTS lab09_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE lab09_db;

-- 1. Bảng Lớp học
CREATE TABLE lop_hoc (
    ma_lop VARCHAR(20) PRIMARY KEY,
    ten_lop VARCHAR(255) NOT NULL
);

-- 2. Bảng Sinh viên
CREATE TABLE sinh_vien (
    ma_sv VARCHAR(20) PRIMARY KEY,
    ho_ten VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    chuyen_nganh VARCHAR(255),
    diem_tb DOUBLE,
    ma_lop VARCHAR(20),
    FOREIGN KEY (ma_lop) REFERENCES lop_hoc(ma_lop) ON DELETE SET NULL
);

-- 3. Bảng Môn học
CREATE TABLE mon_hoc (
    ma_mon VARCHAR(20) PRIMARY KEY,
    ten_mon VARCHAR(255) NOT NULL,
    so_tin_chi INT
);

-- 4. Bảng Điểm
CREATE TABLE diem (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_sv VARCHAR(20),
    ma_mon VARCHAR(20),
    diem_thi DOUBLE,
    FOREIGN KEY (ma_sv) REFERENCES sinh_vien(ma_sv) ON DELETE CASCADE,
    FOREIGN KEY (ma_mon) REFERENCES mon_hoc(ma_mon) ON DELETE CASCADE
);

-- 5. Bảng Sách (Từ yêu cầu Bài 13)
CREATE TABLE sach (
    ma_sach VARCHAR(20) PRIMARY KEY,
    ten_sach VARCHAR(255) NOT NULL,
    tac_gia VARCHAR(255),
    gia DOUBLE
);

-- 6. Bảng Sản phẩm (Từ yêu cầu Bài 13)
CREATE TABLE san_pham (
    ma_sp VARCHAR(20) PRIMARY KEY,
    ten_sp VARCHAR(255) NOT NULL,
    gia DOUBLE
);
