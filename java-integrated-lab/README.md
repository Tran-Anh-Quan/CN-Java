# Lab 16 - Hệ Thống Quản Lý Đơn Hàng Đa Nền Tảng

## 📋 Mô Tả Bài Toán

Xây dựng hệ thống quản lý đơn hàng cho ba nhóm người sử dụng với ba nền tảng khác nhau, tuân thủ các nguyên tắc tích hợp nghiêm ngặt.

| Nhóm Người Dùng | Nền Tảng | Vai trò | Chức Năng |
|----------------|----------|---------|-----------|
| Khách hàng | Jakarta EE (Web) | CUSTOMER | Tạo đơn hàng, theo dõi và xác nhận hoàn thành |
| Nhân viên kho | Java Swing (Desktop) | WAREHOUSE | Tiếp nhận đơn, xử lý tồn kho, đóng gói |
| Quản lý | Spring Boot (Web) | MANAGER/ADMIN | Phê duyệt giao hàng, xem thống kê |

---

## 🔧 4.1. Nguyên Tắc Tích Hợp

### 1. Thống Nhất Schema
- **Tên bảng, cột, kiểu dữ liệu**: Tất cả 3 nền tảng dùng chung 5 bảng:
  - `users` - Người dùng hệ thống
  - `products` - Danh sách sản phẩm
  - `orders` - Đơn hàng
  - `order_items` - Chi tiết đơn hàng
  - `order_history` - Lịch sử thay đổi

### 2. Thống Nhất Trạng Thái
```
PENDING → PROCESSING → READY → SHIPPING → COMPLETED
   ↓          ↓           ↓
CANCELLED  CANCELLED  CANCELLED
```
- Trạng thái và quy tắc chuyển trạng thái được định nghĩa trong `OrderStatus` enum
- Mỗi nền tảng chỉ có thể chuyển các trạng thái nhất định:
  - **Jakarta EE**: PENDING (tạo), SHIPPING → COMPLETED (xác nhận)
  - **Swing**: PENDING → PROCESSING, PROCESSING → READY
  - **Spring**: READY → SHIPPING

### 3. Transaction cho Multi-Table Updates
- Mọi cập nhật nhiều bảng đều thực hiện trong một transaction
- `DatabaseConnection` sử dụng `connection.setAutoCommit(false)` và `commit()`/`rollback()`

### 4. Audit Trail
Mọi thay đổi trạng thái đều ghi:
- `performed_by`: Người thực hiện
- `performed_platform`: Nền tắng thực hiện (JAKARTA/SWING/SPRING)
- `created_at`: Thời gian
- `notes`: Ghi chú bổ sung

### 5. Service Layer cho Business Logic
- **Nguyên tắc**: Không để UI tự thay đổi trạng thái
- Tất cả nghiệp vụ đặt trong Service classes:
  - `WarehouseService`: Xử lý tồn kho, đóng gói
  - `ManagerService`: Phê duyệt, hủy đơn

### 6. Order Locking
- **Vấn đề**: Không cho phép ghi đè khi dữ liệu đã được nền tảng khác cập nhật
- **Giải pháp**: Cơ chế khóa đơn hàng
  - `locked_by`: Username của người khóa
  - `locked_at`: Thời gian khóa
  - Warehouse staff phải khóa đơn trước khi xử lý

---

## 🔄 5.3. Kịch Bản Nghiệp Vụ Bắt Buộc

```
┌──────────────────────────────────────────────────────────────────────────────┐
│ 1. Khách hàng đăng nhập Customer Portal và chọn sản phẩm                      │
│    ↓                                                                         │
│ 2. Jakarta EE tạo đơn ở PENDING, ghi lịch sử                                │
│    ↓                                                                         │
│ 3. Java Swing tải PENDING từ MySQL, hiển thị cho nhân viên kho               │
│    ↓                                                                         │
│ 4. Nhân viên kho tiếp nhận → hệ thống khóa đơn, kiểm tra và trừ tồn kho     │
│    ↓                                                                         │
│ 5. Sau khi đóng gói, nhân viên kho chuyển sang READY, mở khóa               │
│    ↓                                                                         │
│ 6. Spring Boot hiển thị READY cho quản lý                                    │
│    ↓                                                                         │
│ 7. Quản lý phê duyệt → đơn chuyển sang SHIPPING                              │
│    ↓                                                                         │
│ 8. Khách hàng thấy SHIPPING trên Jakarta EE                                  │
│    ↓                                                                         │
│ 9. Khách hàng xác nhận → COMPLETED                                           │
│    ↓                                                                         │
│ 10. Mỗi lần chuyển → ghi một dòng lịch sử                                    │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## 📁 Cấu Trúc Dự Án

```
java-integrated-lab/
├── database/
│   ├── schema.sql              # Schema với 5 bảng + relationships
│   └── sample-data.sql         # Users, Products, Orders, OrderHistory
│
├── shared/                     # Shared module (dependency cho tất cả platforms)
│   ├── pom.xml
│   └── src/main/java/vn/edu/eaut/lab16/shared/
│       ├── enum/               # Platform, OrderStatus, UserRole, HistoryAction
│       ├── entity/             # User, Product, Order, OrderItem, OrderHistory
│       ├── repository/         # DatabaseConnection (shared)
│       └── exception/          # Custom exceptions
│
├── customer-portal-jakarta/    # Jakarta EE Web App
│   ├── pom.xml
│   └── src/main/
│       ├── java/vn/edu/eaut/customer/
│       │   └── controller/     # IndexServlet, CreateOrderServlet, TrackOrderServlet
│       └── webapp/WEB-INF/views/  # JSP pages
│
├── warehouse-desktop-swing/     # Java Swing Desktop App
│   ├── pom.xml
│   └── src/main/java/vn/edu/eaut/warehouse/
│       ├── service/            # WarehouseService (business logic)
│       └── view/               # MainFrame (UI only)
│
├── management-portal-spring/   # Spring Boot Web App
│   ├── pom.xml
│   └── src/main/
│       ├── java/vn/edu/eaut/management/
│       │   ├── config/         # SecurityConfig
│       │   ├── controller/     # DashboardController, OrderController, AuthController
│       │   └── service/        # ManagerService (business logic)
│       └── resources/
│           ├── application.properties
│           └── templates/      # Thymeleaf templates
│
├── pom.xml                     # Parent POM
└── README.md
```

---

## 🗄️ Cơ Sở Dữ Liệu

### 5 Bảng Chính

| Bảng | Mô tả |
|------|-------|
| `users` | Người dùng (username, password, role, platform) |
| `products` | Sản phẩm (code, name, price, stock) |
| `orders` | Đơn hàng (status, locked_by, created_by, created_platform) |
| `order_items` | Chi tiết đơn hàng (FK to orders) |
| `order_history` | Lịch sử thay đổi (who, when, platform, notes) |

### Order Status Flow
```
PENDING ──→ PROCESSING ──→ READY ──→ SHIPPING ──→ COMPLETED
   │            │            │
   └────────────┴────────────┴──────→ CANCELLED
```

---

## 🚀 Cách Chạy Ứng Dụng

### Yêu Cầu
- Java 17+
- Maven 3.6+

### 1. Chạy Customer Portal (Jakarta EE)
```bash
cd customer-portal-jakarta
mvn clean package
mvn jetty:run
```
Truy cập: http://localhost:8080/

### 2. Chạy Warehouse Desktop (Swing)
```bash
cd warehouse-desktop-swing
mvn clean compile
mvn exec:java
```

### 3. Chạy Management Portal (Spring Boot)
```bash
cd management-portal-spring
mvn clean package
mvn spring-boot:run
```
Truy cập: http://localhost:8081/

**Tài khoản demo:**
- Manager: `manager1` / `password123`
- Admin: `admin` / `password123`

---

## 🔐 Bảo Mật

### Spring Security Configuration
- Chỉ MANAGER và ADMIN được truy cập dashboard và orders
- CUSTOMER và WAREHOUSE bị chặn (redirect to login)

### Database Security
- Passwords được hash với BCrypt
- Không lưu plain text passwords

---

## 📝 Ghi Chú Quan Trọng

1. **Shared Module**: Tất cả 3 platforms đều phụ thuộc vào `shared` module
2. **AUTO_SERVER**: H2 database với `AUTO_SERVER=TRUE` cho phép multi-JVM access
3. **Transaction**: Mọi multi-table operation đều trong transaction
4. **Audit Trail**: Mọi status change đều được ghi vào `order_history`
5. **Order Locking**: Kho phải lock đơn trước khi xử lý

---

## 📚 Tài Liệu Tham Khảo

- [Jakarta EE Documentation](https://jakarta.ee/specifications/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [H2 Database Features](https://www.h2database.com/html/features.html)

---

**Lab 16 - Enterprise Application Integration**
