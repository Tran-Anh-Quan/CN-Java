-- =====================================================
-- Sample Data for Multi-Platform Order Management
-- Lab 16 - Java Integrated Lab
-- =====================================================

-- =====================================================
-- Insert Sample Users
-- =====================================================
INSERT INTO users (username, password, full_name, email, phone, role, platform) VALUES
-- Customers (Jakarta EE)
('customer1', '$2a$10$N9qo8uLOickgx2ZMRZoMye.G5hTg9E9P3N5.qzJdU3R5e2x9v5hKO', 'Nguyễn Văn An', 'an.nguyen@email.com', '0912345678', 'CUSTOMER', 'JAKARTA'),
('customer2', '$2a$10$N9qo8uLOickgx2ZMRZoMye.G5hTg9E9P3N5.qzJdU3R5e2x9v5hKO', 'Trần Thị Bình', 'binh.tran@email.com', '0923456789', 'CUSTOMER', 'JAKARTA'),
('customer3', '$2a$10$N9qo8uLOickgx2ZMRZoMye.G5hTg9E9P3N5.qzJdU3R5e2x9v5hKO', 'Lê Minh Cường', 'cuong.le@email.com', '0934567890', 'CUSTOMER', 'JAKARTA'),

-- Warehouse Staff (Swing)
('warehouse1', '$2a$10$N9qo8uLOickgx2ZMRZoMye.G5hTg9E9P3N5.qzJdU3R5e2x9v5hKO', 'Phạm Thu Hà', 'ha.pham@email.com', '0945678901', 'WAREHOUSE', 'SWING'),
('warehouse2', '$2a$10$N9qo8uLOickgx2ZMRZoMye.G5hTg9E9P3N5.qzJdU3R5e2x9v5hKO', 'Hoàng Đức Duy', 'duy.hoang@email.com', '0956789012', 'WAREHOUSE', 'SWING'),

-- Managers (Spring Boot)
('manager1', '$2a$10$N9qo8uLOickgx2ZMRZoMye.G5hTg9E9P3N5.qzJdU3R5e2x9v5hKO', 'Vũ Minh Tuấn', 'tuan.vu@email.com', '0967890123', 'MANAGER', 'SPRING'),
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.G5hTg9E9P3N5.qzJdU3R5e2x9v5hKO', 'Administrator', 'admin@company.com', '0978901234', 'ADMIN', 'SPRING');

-- Password for all users: password123

-- =====================================================
-- Insert Sample Products
-- =====================================================
INSERT INTO products (product_code, product_name, description, price, stock_quantity, available) VALUES
('SP001', 'Laptop Dell Inspiron 15', 'Laptop Dell Inspiron 15, Intel Core i5, 8GB RAM, 512GB SSD', 15990000.00, 25, TRUE),
('SP002', 'iPhone 15 Pro', 'iPhone 15 Pro 256GB, Titanium Design, A17 Pro Chip', 34990000.00, 15, TRUE),
('SP003', 'Samsung Galaxy Tab S9', 'Samsung Galaxy Tab S9, 12.4 inch, 256GB, AMOLED Display', 22990000.00, 30, TRUE),
('SP004', 'Tai nghe AirPods Pro 2', 'Tai nghe AirPods Pro 2 với USB-C, Active Noise Cancellation', 6990000.00, 50, TRUE),
('SP005', 'Chuột không dây Logitech MX Master 3', 'Chuột không dây cao cấp, ergonomic design, 4000 DPI', 3490000.00, 40, TRUE),
('SP006', 'Bàn phím cơ Corsair K70', 'Bàn phím cơ RGB, Cherry MX Red switches', 4990000.00, 35, TRUE),
('SP007', 'Màn hình LG UltraWide 29"', 'Màn hình 29 inch, Ultrawide, IPS, 2560x1080', 8990000.00, 20, TRUE),
('SP008', 'Ổ cứng SSD Samsung 1TB', 'Ổ cứng SSD Samsung 980 PRO 1TB, NVMe PCIe 4.0', 2990000.00, 60, TRUE),
('SP009', 'Webcam Logitech C920', 'Webcam HD 1080p, Stereo microphone', 1990000.00, 45, TRUE),
('SP010', 'Loa Bluetooth JBL Flip 6', 'Loa Bluetooth chống nước IPX7, 12 giờ pin', 2990000.00, 55, TRUE);

-- =====================================================
-- Insert Sample Orders with Proper Status Flow
-- Status: PENDING → PROCESSING → READY → SHIPPING → COMPLETED
-- =====================================================
INSERT INTO orders (order_number, customer_id, customer_name, customer_email, customer_phone, shipping_address, status, created_by, created_platform) VALUES
('ORD-20260901-001', 1, 'Nguyễn Văn An', 'an.nguyen@email.com', '0912345678', '123 Đường Lê Lợi, Quận 1, TP.HCM', 'COMPLETED', 'customer1', 'JAKARTA'),
('ORD-20260902-001', 2, 'Trần Thị Bình', 'binh.tran@email.com', '0923456789', '456 Đường Nguyễn Huệ, Quận 1, TP.HCM', 'SHIPPING', 'customer2', 'JAKARTA'),
('ORD-20260905-001', 3, 'Lê Minh Cường', 'cuong.le@email.com', '0934567890', '789 Đường Đồng Khởi, Quận 1, TP.HCM', 'READY', 'customer3', 'JAKARTA'),
('ORD-20260907-001', 1, 'Nguyễn Văn An', 'an.nguyen@email.com', '0912345678', '321 Đường Pasteur, Quận 3, TP.HCM', 'PROCESSING', 'customer1', 'JAKARTA'),
('ORD-20260908-001', 2, 'Trần Thị Bình', 'binh.tran@email.com', '0923456789', '654 Đường Võ Văn Tần, Quận 3, TP.HCM', 'PENDING', 'customer2', 'JAKARTA');

-- =====================================================
-- Insert Order Items
-- =====================================================
-- Order 1 (Completed) - 1 item
INSERT INTO order_items (order_id, product_code, product_name, quantity, unit_price) VALUES
(1, 'SP002', 'iPhone 15 Pro', 1, 34990000.00);

-- Order 2 (Shipping) - 3 items
INSERT INTO order_items (order_id, product_code, product_name, quantity, unit_price) VALUES
(2, 'SP001', 'Laptop Dell Inspiron 15', 1, 15990000.00),
(2, 'SP004', 'Tai nghe AirPods Pro 2', 1, 6990000.00),
(2, 'SP005', 'Chuột không dây Logitech MX Master 3', 1, 3490000.00);

-- Order 3 (Ready) - 1 item
INSERT INTO order_items (order_id, product_code, product_name, quantity, unit_price) VALUES
(3, 'SP001', 'Laptop Dell Inspiron 15', 1, 15990000.00);

-- Order 4 (Processing) - 2 items
INSERT INTO order_items (order_id, product_code, product_name, quantity, unit_price) VALUES
(4, 'SP004', 'Tai nghe AirPods Pro 2', 1, 6990000.00),
(4, 'SP005', 'Chuột không dây Logitech MX Master 3', 1, 3490000.00);

-- Order 5 (Pending) - 1 item
INSERT INTO order_items (order_id, product_code, product_name, quantity, unit_price) VALUES
(5, 'SP007', 'Màn hình LG UltraWide 29"', 1, 8990000.00);

-- =====================================================
-- Insert Order History (Audit Trail)
-- =====================================================
-- Order 1: Full flow
INSERT INTO order_history (order_id, order_number, action, from_status, to_status, performed_by, performed_platform, notes) VALUES
(1, 'ORD-20260901-001', 'CREATE', NULL, 'PENDING', 'customer1', 'JAKARTA', 'Khách hàng tạo đơn hàng'),
(1, 'ORD-20260901-001', 'STATUS_CHANGE', 'PENDING', 'PROCESSING', 'warehouse1', 'SWING', 'Kho tiếp nhận xử lý'),
(1, 'ORD-20260901-001', 'STATUS_CHANGE', 'PROCESSING', 'READY', 'warehouse1', 'SWING', 'Đã đóng gói xong'),
(1, 'ORD-20260901-001', 'APPROVE', 'READY', 'SHIPPING', 'manager1', 'SPRING', 'Phê duyệt giao hàng'),
(1, 'ORD-20260901-001', 'COMPLETE', 'SHIPPING', 'COMPLETED', 'customer1', 'JAKARTA', 'Khách hàng xác nhận đã nhận hàng');

-- Order 2: In shipping status
INSERT INTO order_history (order_id, order_number, action, from_status, to_status, performed_by, performed_platform, notes) VALUES
(2, 'ORD-20260902-001', 'CREATE', NULL, 'PENDING', 'customer2', 'JAKARTA', 'Khách hàng tạo đơn hàng'),
(2, 'ORD-20260902-001', 'STATUS_CHANGE', 'PENDING', 'PROCESSING', 'warehouse1', 'SWING', 'Kho tiếp nhận xử lý'),
(2, 'ORD-20260902-001', 'STATUS_CHANGE', 'PROCESSING', 'READY', 'warehouse1', 'SWING', 'Đã đóng gói xong'),
(2, 'ORD-20260902-001', 'APPROVE', 'READY', 'SHIPPING', 'manager1', 'SPRING', 'Phê duyệt giao hàng');

-- Order 3: Ready for approval
INSERT INTO order_history (order_id, order_number, action, from_status, to_status, performed_by, performed_platform, notes) VALUES
(3, 'ORD-20260905-001', 'CREATE', NULL, 'PENDING', 'customer3', 'JAKARTA', 'Khách hàng tạo đơn hàng'),
(3, 'ORD-20260905-001', 'STATUS_CHANGE', 'PENDING', 'PROCESSING', 'warehouse1', 'SWING', 'Kho tiếp nhận xử lý'),
(3, 'ORD-20260905-001', 'STATUS_CHANGE', 'PROCESSING', 'READY', 'warehouse1', 'SWING', 'Đã đóng gói xong');

-- Order 4: Processing in warehouse
INSERT INTO order_history (order_id, order_number, action, from_status, to_status, performed_by, performed_platform, notes) VALUES
(4, 'ORD-20260907-001', 'CREATE', NULL, 'PENDING', 'customer1', 'JAKARTA', 'Khách hàng tạo đơn hàng'),
(4, 'ORD-20260907-001', 'STATUS_CHANGE', 'PENDING', 'PROCESSING', 'warehouse1', 'SWING', 'Kho tiếp nhận xử lý');

-- Order 5: New pending order
INSERT INTO order_history (order_id, order_number, action, from_status, to_status, performed_by, performed_platform, notes) VALUES
(5, 'ORD-20260908-001', 'CREATE', NULL, 'PENDING', 'customer2', 'JAKARTA', 'Khách hàng tạo đơn hàng');
