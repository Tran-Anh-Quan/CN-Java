package vn.edu.eaut.lab16.shared.repository;

import vn.edu.eaut.lab16.shared.entity.Order;
import vn.edu.eaut.lab16.shared.entity.OrderItem;
import vn.edu.eaut.lab16.shared.entity.Product;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:h2:mem:ordersystem;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            // Load H2 JDBC driver
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            initializeDatabase();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database", e);
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
        }
        return connection;
    }

    private void initializeDatabase() throws SQLException {
        String createProductsTable = """
            CREATE TABLE IF NOT EXISTS products (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                product_code VARCHAR(50) UNIQUE NOT NULL,
                product_name VARCHAR(255) NOT NULL,
                description TEXT,
                price DECIMAL(10,2) NOT NULL,
                stock_quantity INT DEFAULT 0,
                available BOOLEAN DEFAULT TRUE
            )
        """;

        String createOrdersTable = """
            CREATE TABLE IF NOT EXISTS orders (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                order_number VARCHAR(50) UNIQUE NOT NULL,
                customer_name VARCHAR(255) NOT NULL,
                customer_email VARCHAR(255),
                customer_phone VARCHAR(50),
                shipping_address TEXT,
                status VARCHAR(50) NOT NULL,
                total_amount DECIMAL(10,2),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        String createOrderItemsTable = """
            CREATE TABLE IF NOT EXISTS order_items (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                order_id BIGINT NOT NULL,
                product_name VARCHAR(255) NOT NULL,
                product_code VARCHAR(50) NOT NULL,
                quantity INT NOT NULL,
                price DECIMAL(10,2) NOT NULL,
                FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
            )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createProductsTable);
            stmt.execute(createOrdersTable);
            stmt.execute(createOrderItemsTable);
        }

        // Insert sample products if not exist
        insertSampleProducts();
    }

    private void insertSampleProducts() throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM products";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            if (rs.next() && rs.getInt(1) > 0) {
                return; // Products already exist
            }
        }

        String insertSql = """
            INSERT INTO products (product_code, product_name, description, price, stock_quantity, available)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
            // Product 1
            pstmt.setString(1, "SP001");
            pstmt.setString(2, "Laptop Dell Inspiron 15");
            pstmt.setString(3, "Laptop Dell Inspiron 15, Intel Core i5, 8GB RAM, 512GB SSD");
            pstmt.setBigDecimal(4, new java.math.BigDecimal("15990000"));
            pstmt.setInt(5, 25);
            pstmt.setBoolean(6, true);
            pstmt.executeUpdate();

            // Product 2
            pstmt.setString(1, "SP002");
            pstmt.setString(2, "iPhone 15 Pro");
            pstmt.setString(3, "iPhone 15 Pro 256GB, Titanium Design");
            pstmt.setBigDecimal(4, new java.math.BigDecimal("34990000"));
            pstmt.setInt(5, 15);
            pstmt.setBoolean(6, true);
            pstmt.executeUpdate();

            // Product 3
            pstmt.setString(1, "SP003");
            pstmt.setString(2, "Samsung Galaxy Tab S9");
            pstmt.setString(3, "Samsung Galaxy Tab S9, 12.4 inch, 256GB");
            pstmt.setBigDecimal(4, new java.math.BigDecimal("22990000"));
            pstmt.setInt(5, 30);
            pstmt.setBoolean(6, true);
            pstmt.executeUpdate();

            // Product 4
            pstmt.setString(1, "SP004");
            pstmt.setString(2, "Tai nghe AirPods Pro 2");
            pstmt.setString(3, "Tai nghe AirPods Pro 2 với USB-C");
            pstmt.setBigDecimal(4, new java.math.BigDecimal("6990000"));
            pstmt.setInt(5, 50);
            pstmt.setBoolean(6, true);
            pstmt.executeUpdate();

            // Product 5
            pstmt.setString(1, "SP005");
            pstmt.setString(2, "Chuột không dây Logitech MX Master 3");
            pstmt.setString(3, "Chuột không dây cao cấp Logitech MX Master 3");
            pstmt.setBigDecimal(4, new java.math.BigDecimal("3490000"));
            pstmt.setInt(5, 40);
            pstmt.setBoolean(6, true);
            pstmt.executeUpdate();
        }
    }

    // ==================== Product Repository Methods ====================

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY product_code";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        }
        return products;
    }

    public Product getProductByCode(String productCode) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_code = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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
        String sql = "UPDATE products SET stock_quantity = stock_quantity + ? WHERE product_code = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, quantityChange);
            pstmt.setString(2, productCode);
            pstmt.executeUpdate();
        }
    }

    // ==================== Order Repository Methods ====================

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY created_at DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setItems(getOrderItems(order.getId()));
                orders.add(order);
            }
        }
        return orders;
    }

    public List<Order> getOrdersByStatus(Order.OrderStatus status) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status = ? ORDER BY created_at DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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

    public Order getOrderById(Long id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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

    public Long saveOrder(Order order) throws SQLException {
        if (order.getId() == null) {
            // Insert new order
            String insertSql = """
                INSERT INTO orders (order_number, customer_name, customer_email, customer_phone, 
                                   shipping_address, status, total_amount, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            try (PreparedStatement pstmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, order.getOrderNumber());
                pstmt.setString(2, order.getCustomerName());
                pstmt.setString(3, order.getCustomerEmail());
                pstmt.setString(4, order.getCustomerPhone());
                pstmt.setString(5, order.getShippingAddress());
                pstmt.setString(6, order.getStatus().name());
                pstmt.setBigDecimal(7, order.getTotalAmount());
                pstmt.setTimestamp(8, Timestamp.valueOf(order.getCreatedAt()));
                pstmt.setTimestamp(9, Timestamp.valueOf(order.getUpdatedAt()));
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        order.setId(rs.getLong(1));
                    }
                }
            }

            // Save order items
            if (order.getItems() != null && !order.getItems().isEmpty()) {
                for (OrderItem item : order.getItems()) {
                    saveOrderItem(order.getId(), item);
                }
            }
        } else {
            // Update existing order
            String updateSql = """
                UPDATE orders SET customer_name = ?, customer_email = ?, customer_phone = ?,
                                 shipping_address = ?, status = ?, total_amount = ?, updated_at = ?
                WHERE id = ?
            """;
            try (PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
                pstmt.setString(1, order.getCustomerName());
                pstmt.setString(2, order.getCustomerEmail());
                pstmt.setString(3, order.getCustomerPhone());
                pstmt.setString(4, order.getShippingAddress());
                pstmt.setString(5, order.getStatus().name());
                pstmt.setBigDecimal(6, order.getTotalAmount());
                pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setLong(8, order.getId());
                pstmt.executeUpdate();
            }
        }
        return order.getId();
    }

    public void updateOrderStatus(Long orderId, Order.OrderStatus newStatus) throws SQLException {
        String sql = "UPDATE orders SET status = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newStatus.name());
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(3, orderId);
            pstmt.executeUpdate();
        }
    }

    private void saveOrderItem(Long orderId, OrderItem item) throws SQLException {
        String sql = """
            INSERT INTO order_items (order_id, product_name, product_code, quantity, price)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            pstmt.setString(2, item.getProductName());
            pstmt.setString(3, item.getProductCode());
            pstmt.setInt(4, item.getQuantity());
            pstmt.setBigDecimal(5, item.getPrice());
            pstmt.executeUpdate();
        }
    }

    private List<OrderItem> getOrderItems(Long orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductCode(rs.getString("product_code"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPrice(rs.getBigDecimal("price"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    // ==================== Mapping Methods ====================

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

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setOrderNumber(rs.getString("order_number"));
        order.setCustomerName(rs.getString("customer_name"));
        order.setCustomerEmail(rs.getString("customer_email"));
        order.setCustomerPhone(rs.getString("customer_phone"));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setStatus(Order.OrderStatus.valueOf(rs.getString("status")));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        order.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return order;
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
