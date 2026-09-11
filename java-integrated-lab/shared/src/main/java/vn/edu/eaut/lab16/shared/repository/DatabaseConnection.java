package vn.edu.eaut.lab16.shared.repository;

import vn.edu.eaut.lab16.shared.entity.*;
import vn.edu.eaut.lab16.shared.enums.*;
import vn.edu.eaut.lab16.shared.exception.ConflictException;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared database connection class for all platforms.
 * 
 * Supports:
 * - Bài 6: Cancel with stock restoration
 * - Bài 7: Multi-condition search with PreparedStatement
 * - Bài 8: Optimistic locking with version column
 * - Bài 9: Dashboard statistics
 * - Bài 10: IP/hostname tracking in history
 */
public class DatabaseConnection {
    // H2 Database with AUTO_SERVER for multi-platform access
    private static final String JDBC_URL = "jdbc:h2:file:./data/ordersystem;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            connection.setAutoCommit(false); // Enable transaction support
            initializeSchema();
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            connection.setAutoCommit(false);
        }
        return connection;
    }

    public void commit() throws SQLException {
        if (connection != null) {
            connection.commit();
        }
    }

    public void rollback() {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize schema on startup if tables don't exist.
     */
    private void initializeSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(100) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "full_name VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(255), phone VARCHAR(50), " +
                    "role VARCHAR(50) NOT NULL, platform VARCHAR(50) NOT NULL, " +
                    "active BOOLEAN DEFAULT TRUE, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

            stmt.execute("CREATE TABLE IF NOT EXISTS products (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "product_code VARCHAR(50) UNIQUE NOT NULL, " +
                    "product_name VARCHAR(255) NOT NULL, description TEXT, " +
                    "price DECIMAL(12,2) NOT NULL, " +
                    "stock_quantity INT DEFAULT 0, " +
                    "available BOOLEAN DEFAULT TRUE, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

            stmt.execute("CREATE TABLE IF NOT EXISTS orders (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "order_number VARCHAR(50) UNIQUE NOT NULL, " +
                    "customer_id BIGINT, customer_name VARCHAR(255) NOT NULL, " +
                    "customer_email VARCHAR(255), customer_phone VARCHAR(50), " +
                    "shipping_address TEXT, status VARCHAR(50) NOT NULL DEFAULT 'PENDING', " +
                    "total_amount DECIMAL(15,2) DEFAULT 0, " +
                    "locked_by VARCHAR(100), locked_at TIMESTAMP, " +
                    "created_by VARCHAR(100), created_platform VARCHAR(50), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                    "version INT DEFAULT 0, stock_deducted BOOLEAN DEFAULT FALSE, " +
                    "cancellation_reason TEXT, cancelled_by VARCHAR(100), " +
                    "cancelled_platform VARCHAR(50), cancelled_at TIMESTAMP)");

            stmt.execute("CREATE TABLE IF NOT EXISTS order_items (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "order_id BIGINT NOT NULL, " +
                    "product_code VARCHAR(50) NOT NULL, " +
                    "product_name VARCHAR(255) NOT NULL, " +
                    "quantity INT NOT NULL, unit_price DECIMAL(12,2) NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS order_history (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "order_id BIGINT NOT NULL, " +
                    "order_number VARCHAR(50) NOT NULL, " +
                    "action VARCHAR(50) NOT NULL, " +
                    "from_status VARCHAR(50), to_status VARCHAR(50) NOT NULL, " +
                    "performed_by VARCHAR(100) NOT NULL, " +
                    "performed_platform VARCHAR(50) NOT NULL, " +
                    "ip_address VARCHAR(45), hostname VARCHAR(255), " +
                    "notes TEXT, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            connection.commit();

            // Auto-insert sample data if database is empty (first run)
            insertSampleDataIfEmpty(stmt);
        }
    }

    /**
     * Insert sample data on first run.
     * Checks if users table is empty before inserting.
     */
    private void insertSampleDataIfEmpty(Statement stmt) throws SQLException {
        // Check if data already exists
        try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
            if (rs.next() && rs.getInt(1) > 0) {
                return; // Data already exists, skip
            }
        }

        // Insert sample users (plain-text passwords for demo;
        // CustomerPortal LoginServlet does plain-text compare.
        // Spring Security uses BCrypt for its own InMemoryUserDetailsManager.)
        stmt.execute("""
            INSERT INTO users (username, password, full_name, email, phone, role, platform) VALUES
            ('customer1', 'password123', 'Nguyễn Văn An', 'an.nguyen@email.com', '0912345678', 'CUSTOMER', 'JAKARTA'),
            ('customer2', 'password123', 'Trần Thị Bình', 'binh.tran@email.com', '0923456789', 'CUSTOMER', 'JAKARTA'),
            ('customer3', 'password123', 'Lê Minh Cường', 'cuong.le@email.com', '0934567890', 'CUSTOMER', 'JAKARTA'),
            ('warehouse1', 'password123', 'Phạm Thu Hà', 'ha.pham@email.com', '0945678901', 'WAREHOUSE', 'SWING'),
            ('warehouse2', 'password123', 'Hoàng Đức Duy', 'duy.hoang@email.com', '0956789012', 'WAREHOUSE', 'SWING'),
            ('manager1', 'password123', 'Vũ Minh Tuấn', 'tuan.vu@email.com', '0967890123', 'MANAGER', 'SPRING'),
            ('admin', 'password123', 'Administrator', 'admin@company.com', '0978901234', 'ADMIN', 'SPRING')
            """);

        // Insert sample products
        stmt.execute("""
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
            ('SP010', 'Loa Bluetooth JBL Flip 6', 'Loa Bluetooth chống nước IPX7, 12 giờ pin', 2990000.00, 55, TRUE)
            """);

        // Insert sample orders
        stmt.execute("""
            INSERT INTO orders (order_number, customer_id, customer_name, customer_email, customer_phone, shipping_address, status, created_by, created_platform) VALUES
            ('ORD-20260901-001', 1, 'Nguyễn Văn An', 'an.nguyen@email.com', '0912345678', '123 Đường Lê Lợi, Quận 1, TP.HCM', 'COMPLETED', 'customer1', 'JAKARTA'),
            ('ORD-20260902-001', 2, 'Trần Thị Bình', 'binh.tran@email.com', '0923456789', '456 Đường Nguyễn Huệ, Quận 1, TP.HCM', 'SHIPPING', 'customer2', 'JAKARTA'),
            ('ORD-20260905-001', 3, 'Lê Minh Cường', 'cuong.le@email.com', '0934567890', '789 Đường Đồng Khởi, Quận 1, TP.HCM', 'READY', 'customer3', 'JAKARTA'),
            ('ORD-20260907-001', 1, 'Nguyễn Văn An', 'an.nguyen@email.com', '0912345678', '321 Đường Pasteur, Quận 3, TP.HCM', 'PROCESSING', 'customer1', 'JAKARTA'),
            ('ORD-20260908-001', 2, 'Trần Thị Bình', 'binh.tran@email.com', '0923456789', '654 Đường Võ Văn Tần, Quận 3, TP.HCM', 'PENDING', 'customer2', 'JAKARTA')
            """);

        // Insert order items
        stmt.execute("""
            INSERT INTO order_items (order_id, product_code, product_name, quantity, unit_price) VALUES
            (1, 'SP002', 'iPhone 15 Pro', 1, 34990000.00),
            (2, 'SP001', 'Laptop Dell Inspiron 15', 1, 15990000.00),
            (2, 'SP004', 'Tai nghe AirPods Pro 2', 1, 6990000.00),
            (2, 'SP005', 'Chuột không dây Logitech MX Master 3', 1, 3490000.00),
            (3, 'SP001', 'Laptop Dell Inspiron 15', 1, 15990000.00),
            (4, 'SP004', 'Tai nghe AirPods Pro 2', 1, 6990000.00),
            (4, 'SP005', 'Chuột không dây Logitech MX Master 3', 1, 3490000.00),
            (5, 'SP007', 'Màn hình LG UltraWide 29"', 1, 8990000.00)
            """);

        // Insert order history
        stmt.execute("""
            INSERT INTO order_history (order_id, order_number, action, from_status, to_status, performed_by, performed_platform, notes) VALUES
            (1, 'ORD-20260901-001', 'CREATE', NULL, 'PENDING', 'customer1', 'JAKARTA', 'Khách hàng tạo đơn hàng'),
            (1, 'ORD-20260901-001', 'STATUS_CHANGE', 'PENDING', 'PROCESSING', 'warehouse1', 'SWING', 'Kho tiếp nhận xử lý'),
            (1, 'ORD-20260901-001', 'STATUS_CHANGE', 'PROCESSING', 'READY', 'warehouse1', 'SWING', 'Đã đóng gói xong'),
            (1, 'ORD-20260901-001', 'STATUS_CHANGE', 'READY', 'SHIPPING', 'manager1', 'SPRING', 'Phê duyệt giao hàng'),
            (1, 'ORD-20260901-001', 'COMPLETE', 'SHIPPING', 'COMPLETED', 'customer1', 'JAKARTA', 'Khách hàng xác nhận đã nhận hàng'),
            (2, 'ORD-20260902-001', 'CREATE', NULL, 'PENDING', 'customer2', 'JAKARTA', 'Khách hàng tạo đơn hàng'),
            (2, 'ORD-20260902-001', 'STATUS_CHANGE', 'PENDING', 'PROCESSING', 'warehouse1', 'SWING', 'Kho tiếp nhận xử lý'),
            (2, 'ORD-20260902-001', 'STATUS_CHANGE', 'PROCESSING', 'READY', 'warehouse1', 'SWING', 'Đã đóng gói xong'),
            (2, 'ORD-20260902-001', 'STATUS_CHANGE', 'READY', 'SHIPPING', 'manager1', 'SPRING', 'Phê duyệt giao hàng'),
            (3, 'ORD-20260905-001', 'CREATE', NULL, 'PENDING', 'customer3', 'JAKARTA', 'Khách hàng tạo đơn hàng'),
            (3, 'ORD-20260905-001', 'STATUS_CHANGE', 'PENDING', 'PROCESSING', 'warehouse1', 'SWING', 'Kho tiếp nhận xử lý'),
            (3, 'ORD-20260905-001', 'STATUS_CHANGE', 'PROCESSING', 'READY', 'warehouse1', 'SWING', 'Đã đóng gói xong'),
            (4, 'ORD-20260907-001', 'CREATE', NULL, 'PENDING', 'customer1', 'JAKARTA', 'Khách hàng tạo đơn hàng'),
            (4, 'ORD-20260907-001', 'STATUS_CHANGE', 'PENDING', 'PROCESSING', 'warehouse1', 'SWING', 'Kho tiếp nhận xử lý'),
            (5, 'ORD-20260908-001', 'CREATE', NULL, 'PENDING', 'customer2', 'JAKARTA', 'Khách hàng tạo đơn hàng')
            """);

        connection.commit();
        System.out.println("[DatabaseConnection] Sample data inserted successfully.");
    }

    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND active = TRUE";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        user.setPlatform(Platform.valueOf(rs.getString("platform")));
        user.setActive(rs.getBoolean("active"));
        return user;
    }

    // ==================== Product Operations ====================

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE available = TRUE ORDER BY product_code";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        }
        return products;
    }

    public Product getProductByCode(String productCode) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_code = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, productCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        }
        return null;
    }

    public void updateProductStock(String productCode, int quantityChange) throws SQLException {
        String sql = "UPDATE products SET stock_quantity = stock_quantity + ?, updated_at = ? WHERE product_code = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, quantityChange);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(3, productCode);
            pstmt.executeUpdate();
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setProductCode(rs.getString("product_code"));
        product.setProductName(rs.getString("product_name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        product.setAvailable(rs.getBoolean("available"));
        return product;
    }

    // ==================== Order Operations ====================

    public Order getOrderById(Long id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(getOrderItems(order.getId()));
                    return order;
                }
            }
        }
        return null;
    }

    public Order getOrderByNumber(String orderNumber) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_number = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, orderNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(getOrderItems(order.getId()));
                    return order;
                }
            }
        }
        return null;
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY created_at DESC";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setItems(getOrderItems(order.getId()));
                orders.add(order);
            }
        }
        return orders;
    }

    public List<Order> getOrdersByStatus(OrderStatus status) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status = ? ORDER BY created_at DESC";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(getOrderItems(order.getId()));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    public List<Order> getOrdersByStatuses(List<OrderStatus> statuses) throws SQLException {
        List<Order> orders = new ArrayList<>();
        if (statuses.isEmpty()) return orders;

        StringBuilder sql = new StringBuilder("SELECT * FROM orders WHERE status IN (");
        for (int i = 0; i < statuses.size(); i++) {
            sql.append("?");
            if (i < statuses.size() - 1) sql.append(",");
        }
        sql.append(") ORDER BY created_at DESC");

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < statuses.size(); i++) {
                pstmt.setString(i + 1, statuses.get(i).name());
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(getOrderItems(order.getId()));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    /**
     * Lock order for processing.
     */
    public boolean lockOrder(Long orderId, String username) throws SQLException {
        String sql = "UPDATE orders SET locked_by = ?, locked_at = ?, version = version + 1 " +
                     "WHERE id = ? AND (locked_by IS NULL OR locked_by = ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(3, orderId);
            pstmt.setString(4, username);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Unlock order after processing.
     */
    public void unlockOrder(Long orderId, String username) throws SQLException {
        String sql = "UPDATE orders SET locked_by = NULL, locked_at = NULL, version = version + 1 " +
                     "WHERE id = ? AND locked_by = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    /**
     * Update order status with optimistic locking and history logging.
     * Uses version column to detect conflicts (Bài 8).
     */
    public void updateOrderStatus(Long orderId, OrderStatus newStatus, String performedBy, 
                                   Platform platform, String notes, int expectedVersion,
                                   String ipAddress, String hostname) throws SQLException {
        Connection conn = getConnection();
        try {
            Order order = getOrderById(orderId);
            if (order == null) {
                throw new SQLException("Order not found: " + orderId);
            }

            // Optimistic locking check (Bài 8)
            if (order.getVersion() != expectedVersion) {
                throw new ConflictException(order.getOrderNumber(), 
                        order.getStatus().name(), 
                        String.valueOf(order.getVersion()),
                        String.valueOf(expectedVersion));
            }

            // Validate status transition
            if (!order.getStatus().canTransitionTo(newStatus)) {
                throw new SQLException(String.format("Invalid transition from %s to %s", 
                        order.getStatus(), newStatus));
            }

            OrderStatus oldStatus = order.getStatus();

            // Update order status with version check
            String updateSql = "UPDATE orders SET status = ?, updated_at = ?, version = version + 1 WHERE id = ? AND version = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setString(1, newStatus.name());
                pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setLong(3, orderId);
                pstmt.setInt(4, expectedVersion);
                int updated = pstmt.executeUpdate();
                if (updated == 0) {
                    throw new ConflictException(order.getOrderNumber(), 
                            order.getStatus().name(),
                            String.valueOf(order.getVersion()),
                            String.valueOf(expectedVersion));
                }
            }

            // Insert history record with IP/hostname
            String historySql = """
                INSERT INTO order_history (order_id, order_number, action, from_status, to_status, 
                                          performed_by, performed_platform, ip_address, hostname, notes)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(historySql)) {
                pstmt.setLong(1, orderId);
                pstmt.setString(2, order.getOrderNumber());
                pstmt.setString(3, HistoryAction.STATUS_CHANGE.name());
                pstmt.setString(4, oldStatus.name());
                pstmt.setString(5, newStatus.name());
                pstmt.setString(6, performedBy);
                pstmt.setString(7, platform.name());
                pstmt.setString(8, ipAddress);
                pstmt.setString(9, hostname);
                pstmt.setString(10, notes);
                pstmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } catch (ConflictException e) {
            conn.rollback();
            throw e;
        }
    }

    /**
     * Backward compatible version - uses current version.
     */
    public void updateOrderStatus(Long orderId, OrderStatus newStatus, String performedBy, 
                                   Platform platform, String notes) throws SQLException {
        Order order = getOrderById(orderId);
        if (order == null) throw new SQLException("Order not found: " + orderId);
        updateOrderStatus(orderId, newStatus, performedBy, platform, notes, 
                         order.getVersion(), null, getHostname());
    }

    /**
     * Cancel order with stock restoration (Bài 6).
     * If stock was deducted, restore it in the same transaction.
     */
    public void cancelOrder(Long orderId, String reason, String performedBy,
                            Platform platform, String ipAddress, String hostname) throws SQLException {
        Connection conn = getConnection();
        try {
            Order order = getOrderById(orderId);
            if (order == null) {
                throw new SQLException("Order not found: " + orderId);
            }

            OrderStatus oldStatus = order.getStatus();

            // Restore stock if it was deducted
            if (order.isStockDeducted()) {
                for (OrderItem item : order.getItems()) {
                    updateProductStock(item.getProductCode(), item.getQuantity());
                }
            }

            // Update order status with cancellation details
            String updateSql = """
                UPDATE orders SET status = ?, updated_at = ?, version = version + 1,
                                 cancellation_reason = ?, cancelled_by = ?, 
                                 cancelled_platform = ?, cancelled_at = ?
                WHERE id = ?
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setString(1, OrderStatus.CANCELLED.name());
                pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setString(3, reason);
                pstmt.setString(4, performedBy);
                pstmt.setString(5, platform.name());
                pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setLong(7, orderId);
                pstmt.executeUpdate();
            }

            // Insert history record
            String historySql = """
                INSERT INTO order_history (order_id, order_number, action, from_status, to_status, 
                                          performed_by, performed_platform, ip_address, hostname, notes)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(historySql)) {
                pstmt.setLong(1, orderId);
                pstmt.setString(2, order.getOrderNumber());
                pstmt.setString(3, HistoryAction.CANCEL.name());
                pstmt.setString(4, oldStatus.name());
                pstmt.setString(5, OrderStatus.CANCELLED.name());
                pstmt.setString(6, performedBy);
                pstmt.setString(7, platform.name());
                pstmt.setString(8, ipAddress);
                pstmt.setString(9, hostname);
                pstmt.setString(10, reason != null ? reason : "Hủy đơn hàng");
                pstmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    /**
     * Create new order with items and history.
     */
    public Long createOrder(Order order, String createdBy, Platform platform,
                             String ipAddress, String hostname) throws SQLException {
        Connection conn = getConnection();
        try {
            String orderSql = """
                INSERT INTO orders (order_number, customer_id, customer_name, customer_email, customer_phone,
                                   shipping_address, status, total_amount, created_by, created_platform, 
                                   created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            Long orderId = null;
            try (PreparedStatement pstmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, order.getOrderNumber());
                pstmt.setObject(2, order.getCustomerId());
                pstmt.setString(3, order.getCustomerName());
                pstmt.setString(4, order.getCustomerEmail());
                pstmt.setString(5, order.getCustomerPhone());
                pstmt.setString(6, order.getShippingAddress());
                pstmt.setString(7, OrderStatus.PENDING.name());
                pstmt.setBigDecimal(8, order.getTotalAmount());
                pstmt.setString(9, createdBy);
                pstmt.setString(10, platform.name());
                pstmt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getLong(1);
                        order.setId(orderId);
                    }
                }
            }

            // Insert order items
            if (order.getItems() != null && !order.getItems().isEmpty()) {
                String itemSql = """
                    INSERT INTO order_items (order_id, product_code, product_name, quantity, unit_price)
                    VALUES (?, ?, ?, ?, ?)
                """;
                try (PreparedStatement pstmt = conn.prepareStatement(itemSql)) {
                    for (OrderItem item : order.getItems()) {
                        pstmt.setLong(1, orderId);
                        pstmt.setString(2, item.getProductCode());
                        pstmt.setString(3, item.getProductName());
                        pstmt.setInt(4, item.getQuantity());
                        pstmt.setBigDecimal(5, item.getUnitPrice());
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            }

            // Insert history record with IP/hostname
            String historySql = """
                INSERT INTO order_history (order_id, order_number, action, from_status, to_status, 
                                          performed_by, performed_platform, ip_address, hostname, notes)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(historySql)) {
                pstmt.setLong(1, orderId);
                pstmt.setString(2, order.getOrderNumber());
                pstmt.setString(3, HistoryAction.CREATE.name());
                pstmt.setString(4, null);
                pstmt.setString(5, OrderStatus.PENDING.name());
                pstmt.setString(6, createdBy);
                pstmt.setString(7, platform.name());
                pstmt.setString(8, ipAddress);
                pstmt.setString(9, hostname);
                pstmt.setString(10, "Khách hàng tạo đơn hàng");
                pstmt.executeUpdate();
            }

            conn.commit();
            return orderId;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    /**
     * Backward compatible createOrder without IP/hostname.
     */
    public Long createOrder(Order order, String createdBy, Platform platform) throws SQLException {
        return createOrder(order, createdBy, platform, null, getHostname());
    }

    /**
     * Mark stock as deducted for an order (called when stock is processed).
     */
    public void markStockDeducted(Long orderId) throws SQLException {
        String sql = "UPDATE orders SET stock_deducted = TRUE, version = version + 1 WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Get order items.
     */
    private List<OrderItem> getOrderItems(Long orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setOrderId(rs.getLong("order_id"));
                    item.setProductCode(rs.getString("product_code"));
                    item.setProductName(rs.getString("product_name"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setOrderNumber(rs.getString("order_number"));
        
        Long customerId = rs.getObject("customer_id", Long.class);
        order.setCustomerId(customerId);
        
        order.setCustomerName(rs.getString("customer_name"));
        order.setCustomerEmail(rs.getString("customer_email"));
        order.setCustomerPhone(rs.getString("customer_phone"));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        
        BigDecimal totalAmount = rs.getBigDecimal("total_amount");
        if (totalAmount != null) order.setTotalAmount(totalAmount);
        
        order.setLockedBy(rs.getString("locked_by"));
        Timestamp lockedAt = rs.getTimestamp("locked_at");
        if (lockedAt != null) {
            order.setLockedAt(lockedAt.toLocalDateTime());
        }
        
        order.setCreatedBy(rs.getString("created_by"));
        String platformStr = rs.getString("created_platform");
        if (platformStr != null) {
            order.setCreatedPlatform(Platform.valueOf(platformStr));
        }
        
        order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        order.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        order.setVersion(rs.getInt("version"));
        order.setStockDeducted(rs.getBoolean("stock_deducted"));
        order.setCancellationReason(rs.getString("cancellation_reason"));
        order.setCancelledBy(rs.getString("cancelled_by"));
        
        String cancelledPlatform = rs.getString("cancelled_platform");
        if (cancelledPlatform != null) {
            order.setCancelledPlatform(Platform.valueOf(cancelledPlatform));
        }
        
        Timestamp cancelledAt = rs.getTimestamp("cancelled_at");
        if (cancelledAt != null) {
            order.setCancelledAt(cancelledAt.toLocalDateTime());
        }
        
        return order;
    }

    // ==================== Bài 7: Multi-condition Search ====================

    /**
     * Search orders with multiple conditions using PreparedStatement.
     */
    public List<Order> searchOrders(String orderNumber, String customerName, OrderStatus status,
                                     LocalDate fromDate, LocalDate toDate,
                                     BigDecimal minAmount, BigDecimal maxAmount) throws SQLException {
        List<Order> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM orders WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (orderNumber != null && !orderNumber.isEmpty()) {
            sql.append(" AND order_number LIKE ?");
            params.add("%" + orderNumber + "%");
        }
        if (customerName != null && !customerName.isEmpty()) {
            sql.append(" AND customer_name LIKE ?");
            params.add("%" + customerName + "%");
        }
        if (status != null) {
            sql.append(" AND status = ?");
            params.add(status.name());
        }
        if (fromDate != null) {
            sql.append(" AND DATE(created_at) >= ?");
            params.add(Date.valueOf(fromDate));
        }
        if (toDate != null) {
            sql.append(" AND DATE(created_at) <= ?");
            params.add(Date.valueOf(toDate));
        }
        if (minAmount != null) {
            sql.append(" AND total_amount >= ?");
            params.add(minAmount);
        }
        if (maxAmount != null) {
            sql.append(" AND total_amount <= ?");
            params.add(maxAmount);
        }

        sql.append(" ORDER BY created_at DESC");

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(getOrderItems(order.getId()));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    /**
     * Search orders with pagination (Bài 7).
     */
    public List<Order> searchOrdersWithPaging(String orderNumber, String customerName, OrderStatus status,
                                                LocalDate fromDate, LocalDate toDate,
                                                BigDecimal minAmount, BigDecimal maxAmount,
                                                int page, int pageSize) throws SQLException {
        List<Order> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM orders WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (orderNumber != null && !orderNumber.isEmpty()) {
            sql.append(" AND order_number LIKE ?");
            params.add("%" + orderNumber + "%");
        }
        if (customerName != null && !customerName.isEmpty()) {
            sql.append(" AND customer_name LIKE ?");
            params.add("%" + customerName + "%");
        }
        if (status != null) {
            sql.append(" AND status = ?");
            params.add(status.name());
        }
        if (fromDate != null) {
            sql.append(" AND DATE(created_at) >= ?");
            params.add(Date.valueOf(fromDate));
        }
        if (toDate != null) {
            sql.append(" AND DATE(created_at) <= ?");
            params.add(Date.valueOf(toDate));
        }
        if (minAmount != null) {
            sql.append(" AND total_amount >= ?");
            params.add(minAmount);
        }
        if (maxAmount != null) {
            sql.append(" AND total_amount <= ?");
            params.add(maxAmount);
        }

        sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(getOrderItems(order.getId()));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    /**
     * Count orders with search conditions.
     */
    public int countOrders(String orderNumber, String customerName, OrderStatus status,
                            LocalDate fromDate, LocalDate toDate,
                            BigDecimal minAmount, BigDecimal maxAmount) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM orders WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (orderNumber != null && !orderNumber.isEmpty()) {
            sql.append(" AND order_number LIKE ?");
            params.add("%" + orderNumber + "%");
        }
        if (customerName != null && !customerName.isEmpty()) {
            sql.append(" AND customer_name LIKE ?");
            params.add("%" + customerName + "%");
        }
        if (status != null) {
            sql.append(" AND status = ?");
            params.add(status.name());
        }
        if (fromDate != null) {
            sql.append(" AND DATE(created_at) >= ?");
            params.add(Date.valueOf(fromDate));
        }
        if (toDate != null) {
            sql.append(" AND DATE(created_at) <= ?");
            params.add(Date.valueOf(toDate));
        }
        if (minAmount != null) {
            sql.append(" AND total_amount >= ?");
            params.add(minAmount);
        }
        if (maxAmount != null) {
            sql.append(" AND total_amount <= ?");
            params.add(maxAmount);
        }

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    // ==================== Bài 9: Dashboard Statistics ====================

    /**
     * Get count of orders today.
     */
    public int getOrdersTodayCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE DATE(created_at) = CURRENT_DATE";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    /**
     * Get total revenue from COMPLETED orders.
     */
    public BigDecimal getCompletedRevenue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE status = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, OrderStatus.COMPLETED.name());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1);
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * Get revenue for a date range.
     */
    public BigDecimal getRevenueByDateRange(LocalDate fromDate, LocalDate toDate) throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE status = ? AND DATE(created_at) BETWEEN ? AND ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, OrderStatus.COMPLETED.name());
            pstmt.setDate(2, Date.valueOf(fromDate));
            pstmt.setDate(3, Date.valueOf(toDate));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1);
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * Get top N products by quantity sold.
     */
    public List<Map<String, Object>> getTopProducts(int limit) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = """
            SELECT oi.product_code, oi.product_name, 
                   SUM(oi.quantity) as total_quantity, 
                   SUM(oi.quantity * oi.unit_price) as total_revenue,
                   COUNT(DISTINCT oi.order_id) as order_count
            FROM order_items oi 
            JOIN orders o ON oi.order_id = o.id
            WHERE o.status IN ('COMPLETED', 'SHIPPING')
            GROUP BY oi.product_code, oi.product_name
            ORDER BY total_quantity DESC
            LIMIT ?
        """;
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("productCode", rs.getString("product_code"));
                    row.put("productName", rs.getString("product_name"));
                    row.put("totalQuantity", rs.getLong("total_quantity"));
                    row.put("totalRevenue", rs.getBigDecimal("total_revenue"));
                    row.put("orderCount", rs.getLong("order_count"));
                    result.add(row);
                }
            }
        }
        return result;
    }

    /**
     * Get top N customers by order count and total spent.
     */
    public List<Map<String, Object>> getTopCustomers(int limit) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = """
            SELECT customer_id, customer_name, customer_email,
                   COUNT(*) as order_count,
                   SUM(total_amount) as total_spent,
                   SUM(CASE WHEN status = 'COMPLETED' THEN total_amount ELSE 0 END) as completed_spent
            FROM orders
            GROUP BY customer_id, customer_name, customer_email
            ORDER BY total_spent DESC
            LIMIT ?
        """;
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("customerId", rs.getObject("customer_id", Long.class));
                    row.put("customerName", rs.getString("customer_name"));
                    row.put("customerEmail", rs.getString("customer_email"));
                    row.put("orderCount", rs.getLong("order_count"));
                    row.put("totalSpent", rs.getBigDecimal("total_spent"));
                    row.put("completedSpent", rs.getBigDecimal("completed_spent"));
                    result.add(row);
                }
            }
        }
        return result;
    }

    /**
     * Get orders count by status.
     */
    public Map<String, Long> getOrderCountByStatus() throws SQLException {
        Map<String, Long> result = new HashMap<>();
        String sql = "SELECT status, COUNT(*) as cnt FROM orders GROUP BY status";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.put(rs.getString("status"), rs.getLong("cnt"));
            }
        }
        return result;
    }

    // ==================== Bài 10: Order History with Filters ====================

    /**
     * Search order history with multiple filters.
     */
    public List<OrderHistory> searchHistory(String orderNumber, String action, Platform platform,
                                             String performedBy, LocalDate fromDate, LocalDate toDate,
                                             int page, int pageSize) throws SQLException {
        List<OrderHistory> historyList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM order_history WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (orderNumber != null && !orderNumber.isEmpty()) {
            sql.append(" AND order_number LIKE ?");
            params.add("%" + orderNumber + "%");
        }
        if (action != null && !action.isEmpty()) {
            sql.append(" AND action = ?");
            params.add(action);
        }
        if (platform != null) {
            sql.append(" AND performed_platform = ?");
            params.add(platform.name());
        }
        if (performedBy != null && !performedBy.isEmpty()) {
            sql.append(" AND performed_by LIKE ?");
            params.add("%" + performedBy + "%");
        }
        if (fromDate != null) {
            sql.append(" AND DATE(created_at) >= ?");
            params.add(Date.valueOf(fromDate));
        }
        if (toDate != null) {
            sql.append(" AND DATE(created_at) <= ?");
            params.add(Date.valueOf(toDate));
        }

        sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    historyList.add(mapResultSetToHistory(rs));
                }
            }
        }
        return historyList;
    }

    /**
     * Count history with filters.
     */
    public int countHistory(String orderNumber, String action, Platform platform,
                             String performedBy, LocalDate fromDate, LocalDate toDate) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM order_history WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (orderNumber != null && !orderNumber.isEmpty()) {
            sql.append(" AND order_number LIKE ?");
            params.add("%" + orderNumber + "%");
        }
        if (action != null && !action.isEmpty()) {
            sql.append(" AND action = ?");
            params.add(action);
        }
        if (platform != null) {
            sql.append(" AND performed_platform = ?");
            params.add(platform.name());
        }
        if (performedBy != null && !performedBy.isEmpty()) {
            sql.append(" AND performed_by LIKE ?");
            params.add("%" + performedBy + "%");
        }
        if (fromDate != null) {
            sql.append(" AND DATE(created_at) >= ?");
            params.add(Date.valueOf(fromDate));
        }
        if (toDate != null) {
            sql.append(" AND DATE(created_at) <= ?");
            params.add(Date.valueOf(toDate));
        }

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<OrderHistory> getOrderHistory(Long orderId) throws SQLException {
        List<OrderHistory> historyList = new ArrayList<>();
        String sql = "SELECT * FROM order_history WHERE order_id = ? ORDER BY created_at ASC";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    historyList.add(mapResultSetToHistory(rs));
                }
            }
        }
        return historyList;
    }

    public void addHistory(OrderHistory history) throws SQLException {
        String sql = """
            INSERT INTO order_history (order_id, order_number, action, from_status, to_status, 
                                      performed_by, performed_platform, ip_address, hostname, notes)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setLong(1, history.getOrderId());
            pstmt.setString(2, history.getOrderNumber());
            pstmt.setString(3, history.getAction().name());
            pstmt.setString(4, history.getFromStatus() != null ? history.getFromStatus().name() : null);
            pstmt.setString(5, history.getToStatus().name());
            pstmt.setString(6, history.getPerformedBy());
            pstmt.setString(7, history.getPerformedPlatform().name());
            pstmt.setString(8, history.getIpAddress());
            pstmt.setString(9, history.getHostname());
            pstmt.setString(10, history.getNotes());
            pstmt.executeUpdate();
        }
    }

    private OrderHistory mapResultSetToHistory(ResultSet rs) throws SQLException {
        OrderHistory history = new OrderHistory();
        history.setId(rs.getLong("id"));
        history.setOrderId(rs.getLong("order_id"));
        history.setOrderNumber(rs.getString("order_number"));
        history.setAction(HistoryAction.valueOf(rs.getString("action")));
        
        String fromStatus = rs.getString("from_status");
        if (fromStatus != null) {
            history.setFromStatus(OrderStatus.valueOf(fromStatus));
        }
        
        history.setToStatus(OrderStatus.valueOf(rs.getString("to_status")));
        history.setPerformedBy(rs.getString("performed_by"));
        history.setPerformedPlatform(Platform.valueOf(rs.getString("performed_platform")));
        history.setIpAddress(rs.getString("ip_address"));
        history.setHostname(rs.getString("hostname"));
        history.setNotes(rs.getString("notes"));
        history.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        return history;
    }

    /**
     * Get local hostname for Swing platform tracking.
     */
    public static String getHostname() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown";
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
