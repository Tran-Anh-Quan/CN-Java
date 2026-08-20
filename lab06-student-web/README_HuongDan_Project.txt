======================================================================
HƯỚNG DẪN TỔNG QUAN DỰ ÁN LAB06 - QUẢN LÝ SINH VIÊN (SERVLET & JSP)
======================================================================

1. TỔNG QUAN DỰ ÁN (PROJECT OVERVIEW)
----------------------------------------------------------------------
Dự án "lab06-student-web" là một ứng dụng Web viết bằng Java dựa trên 
kiến trúc MVC (Model - View - Controller) sử dụng Servlet và JSP.
Chức năng chính:
- Đăng nhập / Xác thực người dùng và phân quyền (Admin / User).
- Quản lý danh sách sinh viên: Thêm, sửa, xóa, tìm kiếm sinh viên.
- Có Dashboard / Welcome page.
- Sử dụng Filter để log request và bảo vệ các trang yêu cầu quyền Admin.
- Sử dụng Listener để khởi tạo dữ liệu mẫu ban đầu khi ứng dụng deploy.


2. CẤU TRÚC THƯ MỤC & CODE (PROJECT STRUCTURE)
----------------------------------------------------------------------
Dự án được tổ chức theo chuẩn Maven với các package chính trong thư mục:
`src/main/java/vn/edu/eaut/lab6/`

a. Package `model`:
   - Chứa các class đối tượng thực thể (Java Beans) như Student, User.
   - Định nghĩa các thuộc tính, Constructor, Getter/Setter.

b. Package `store` (hay DAO/Repository):
   - Chứa các class lưu trữ dữ liệu tạm thời (ví dụ: StudentDataStore, UserDataStore).
   - Đảm nhiệm việc giao tiếp với nguồn dữ liệu (ở đây có thể dùng List/Map trong bộ nhớ tạm).
   
c. Package `controller` (Servlet):
   - Đóng vai trò là "Controller" trong mô hình MVC.
   - Nhận Request từ trình duyệt, lấy dữ liệu từ `store`, xử lý logic (nếu có).
   - Đưa dữ liệu (Attribute) vào Request và điều hướng (forward/redirect) tới JSP.
   - Các file tiêu biểu: StudentServlet (xử lý CRUD), LoginServlet, LogoutServlet...

d. Package `filter`:
   - Chứa các Filter (như LogFilter, AuthFilter).
   - Can thiệp vào Request trước khi đến Servlet, hoặc Response trước khi trả về View.
   - Thường dùng để ghi log truy cập, hoặc kiểm tra quyền đăng nhập (Auth).

e. Package `listener`:
   - Chứa các ServletContextListener.
   - Kích hoạt khi Server (Tomcat) bắt đầu chạy hoặc dừng.
   - Dùng để nạp dữ liệu mẫu ban đầu (Initial Data) vào Session hoặc Application Context.

f. Thư mục `src/main/webapp/`:
   - Đóng vai trò là "View" trong mô hình MVC.
   - Chứa các file giao diện HTML, CSS, JS và các file `.jsp`.
   - Cung cấp giao diện trực quan cho người dùng tương tác, hiển thị dữ liệu từ Controller trả về.


3. CÁCH HIỂU LUỒNG CHẠY CỦA CODE (WORKFLOW)
----------------------------------------------------------------------
Khi bạn mở trình duyệt và tương tác, quá trình diễn ra như sau:
(1) Trình duyệt gửi Request (ví dụ: click nút "Thêm Sinh Viên", hoặc "Đăng nhập").
(2) Filter: Request chạy qua Filter đầu tiên (kiểm tra xem đã login chưa, có quyền không, log lại info). Nếu không đạt, báo lỗi 403 hoặc bắt đăng nhập. Nếu OK, cho qua.
(3) Servlet (Controller): Servlet tiếp nhận Request (qua doGet hoặc doPost).
(4) Model & Store: Servlet lấy dữ liệu từ request parameter, gọi đến `store` để thao tác (thêm sửa xóa) trên Model.
(5) Điều hướng (Forward/Redirect): 
    - Nếu lấy dữ liệu ra để hiển thị: Servlet setAttribute và "Forward" sang trang .jsp.
    - Nếu vừa thêm/sửa/xóa thành công: Servlet thường "Redirect" về trang danh sách để tránh submit lại form.
(6) JSP (View): Sinh mã HTML dựa trên dữ liệu nhận được và trả về trình duyệt (Response).


4. HƯỚNG DẪN CÁCH CHẠY DỰ ÁN (HOW TO RUN)
----------------------------------------------------------------------
Yêu cầu hệ thống:
- Java JDK 11 hoặc mới hơn.
- Maven đã được cài đặt và cấu hình biến môi trường.

Cách chạy sử dụng Maven Tomcat Plugin (đã được cấu hình trong pom.xml):
- Bước 1: Mở Terminal / Command Prompt hoặc Terminal trong IDE (VSCode, IntelliJ, Eclipse) tại thư mục gốc của dự án (nơi chứa file pom.xml).
- Bước 2: Chạy câu lệnh Maven sau để dọn dẹp và chạy server Tomcat:`
      cd D:\Java\lab06-student-web
      mvn clean tomcat7:run
- Bước 3: Đợi một lát cho Maven tải thư viện và khởi động Server Tomcat. Bạn sẽ thấy dòng "Starting Tomcat server on port: 8080".
- Bước 4: Mở trình duyệt web và truy cập vào đường dẫn:
        http://localhost:8080/

Cách dừng ứng dụng:
- Ở cửa sổ Terminal đang chạy, bấm tổ hợp phím `Ctrl + C` để dừng server.


