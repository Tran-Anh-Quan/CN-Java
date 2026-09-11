-- =====================================================
-- Schema for Multi-Platform Order Management System
-- Lab 16 - Java Integrated Lab
-- Integration Principles:
--   - All platforms share the same schema
--   - Status transitions are validated in Services
--   - All status changes are logged in order_history
--   - Orders can be locked by a platform to prevent conflicts
--   - Optimistic locking via version column for conflict detection
-- =====================================================

-- Drop tables in correct order (due to foreign keys)
DROP TABLE IF EXISTS order_history;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

-- =====================================================
-- Users Table (for authentication and audit)
-- =====================================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    role VARCHAR(50) NOT NULL,  -- CUSTOMER, WAREHOUSE, MANAGER, ADMIN
    platform VARCHAR(50) NOT NULL,  -- JAKARTA, SWING, SPRING
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_platform ON users(platform);

-- =====================================================
-- Products Table
-- =====================================================
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(50) UNIQUE NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(12,2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_code ON products(product_code);
CREATE INDEX idx_products_available ON products(available);

-- =====================================================
-- Orders Table
-- NOTE: 
--   - locked_by, locked_at: for order locking mechanism
--   - created_by, created_platform: for audit trail
--   - version: for optimistic locking (Bài 8)
--   - cancellation_reason, cancelled_by, cancelled_at: for cancel tracking (Bài 6)
--   - Status transitions must be validated in Service layer
-- =====================================================
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    customer_id BIGINT,
    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255),
    customer_phone VARCHAR(50),
    shipping_address TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(15,2) DEFAULT 0,
    -- Locking mechanism
    locked_by VARCHAR(100),
    locked_at TIMESTAMP,
    -- Audit trail
    created_by VARCHAR(100),
    created_platform VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    -- Optimistic locking version
    version INT DEFAULT 0,
    -- Track if stock has been deducted (for cancellation logic)
    stock_deducted BOOLEAN DEFAULT FALSE,
    -- Cancellation tracking
    cancellation_reason TEXT,
    cancelled_by VARCHAR(100),
    cancelled_platform VARCHAR(50),
    cancelled_at TIMESTAMP,
    -- Foreign keys
    FOREIGN KEY (customer_id) REFERENCES users(id)
);

CREATE INDEX idx_orders_number ON orders(order_number);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_locked ON orders(locked_by);
CREATE INDEX idx_orders_created ON orders(created_at);
CREATE INDEX idx_orders_updated ON orders(updated_at);
CREATE INDEX idx_orders_total ON orders(total_amount);
CREATE INDEX idx_orders_cancelled ON orders(cancelled_at);

-- =====================================================
-- Order Items Table
-- =====================================================
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_code VARCHAR(50) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_product ON order_items(product_code);

-- =====================================================
-- Order History Table (Audit Trail)
-- Records every status change with:
--   - Who made the change
--   - From which platform
--   - Previous and new status
--   - Timestamp and notes
--   - IP address for web platforms
--   - Hostname for Swing platform
-- =====================================================
CREATE TABLE order_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    order_number VARCHAR(50) NOT NULL,
    action VARCHAR(50) NOT NULL,  -- CREATE, LOCK, UNLOCK, STATUS_CHANGE, APPROVE, SHIP, COMPLETE, CANCEL
    from_status VARCHAR(50),
    to_status VARCHAR(50) NOT NULL,
    performed_by VARCHAR(100) NOT NULL,
    performed_platform VARCHAR(50) NOT NULL,
    -- Tracking fields (Bài 10)
    ip_address VARCHAR(45),       -- IPv4 or IPv6 address for web platforms
    hostname VARCHAR(255),        -- Machine name for Swing platform
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE INDEX idx_history_order ON order_history(order_id);
CREATE INDEX idx_history_action ON order_history(action);
CREATE INDEX idx_history_performer ON order_history(performed_by);
CREATE INDEX idx_history_created ON order_history(created_at);
CREATE INDEX idx_history_platform ON order_history(performed_platform);
CREATE INDEX idx_history_ip ON order_history(ip_address);

-- =====================================================
-- Order Status Flow
-- PENDING → PROCESSING → READY → SHIPPING → COMPLETED
--            ↑           ↑
--         (lock)      (pack)
-- =====================================================
