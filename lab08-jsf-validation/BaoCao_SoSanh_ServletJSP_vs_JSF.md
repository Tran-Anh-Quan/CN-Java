# 📊 Báo Cáo So Sánh Chi Tiết: Servlet/JSP (Lab 7) vs. JSF (Lab 8)

> **Môn học:** Công nghệ Java (IT3242)  
> **Chủ đề:** So sánh quy trình xử lý form, Validation, Message, Điều hướng và Mức độ tách mã giữa kiến trúc Servlet/JSP và JavaServer Faces (JSF).

---

## 📑 Bảng So Sánh Tổng Quan

| Tiêu Chí So Sánh | 🛠️ Servlet / JSP (Lab 7) | 🚀 JavaServer Faces - JSF (Lab 8) |
| :--- | :--- | :--- |
| **Quy trình xử lý Form** | Thủ công qua `HttpServletRequest`: Đọc tham số bằng `request.getParameter()`, parse kiểu dữ liệu (`Integer.parseInt()`, `Double.parseDouble()`) thủ công. | **Tự động qua JSF Lifecycle & Data Binding**: Tự động bind dữ liệu 2 chiều (`#{bean.property}`) giữa UI Component và ManagedBean. |
| **Validation Dữ Liệu** | Viết câu lệnh `if-else` thủ công trong Servlet hoặc gọi Validator helper, gán danh sách lỗi vào `request.setAttribute("errors", ...)`. | **Declarative Bean Validation (JSR-380)**: Khai báo trực tiếp bằng annotation trên Model (`@NotBlank`, `@Min`, `@Max`, `@Email`, `@Pattern`). |
| **Hiển thị Thông báo (Message)** | Duyệt danh sách lỗi từ `requestScope` bằng thẻ JSTL `<c:forEach>` trên file JSP. | **JSF Messages (`<h:message>` / `<h:messages>`)**: Tự động hiển thị lỗi theo từng input field (`for="id"`) hoặc thông báo toàn cục qua `FacesMessage`. |
| **Điều hướng Trang (Navigation)** | Dùng `request.getRequestDispatcher("page.jsp").forward(req, resp)` hoặc `response.sendRedirect("url")`. | **JSF Navigation Handler**: Trả về tên view / outcome string từ method của Bean (vd: `return "sinhvien-list?faces-redirect=true";`). |
| **Mức độ Tách Mã (Separation of Concerns)** | Vẫn còn trộn lẫn mã Java (trong Servlet) và HTML/JSTL (trong JSP). Xử lý request/response trực tiếp ở tầng HTTP level. | **Kiến trúc Component-Driven & MVC hoàn chỉnh**: Giao diện hoàn toàn là Facelets (`.xhtml`), logic ứng dụng tập trung hoàn toàn ở ManagedBean (`@ManagedBean`). |
| **Quản lý Layout / Template** | Sử dụng `<jsp:include page="..." />` đơn giản. | **Facelets Templating (`<ui:composition>`, `<ui:include>`, `<ui:insert>`)**: Layout dùng chung chuyên nghiệp, hỗ trợ ghi đè vùng nội dung động. |

---

## 🎯 Phân Tích Chi Tiết 5 Khía Cạnh Quan Trọng

### 1. Quy Trình Xử Lý Form (Form Processing)
* **Servlet/JSP (Lab 7):** Lập trình viên phải thao tác trực tiếp với các chuỗi HTTP Parameter, ép kiểu dữ liệu từ `String` sang các kiểu dữ liệu đối tượng Java và tự gán dữ liệu vào đối tượng Model.
* **JSF (Lab 8):** JSF quản lý vòng đời component qua 6 pha (Restore View, Apply Request Values, Process Validations, Update Model Values, Invoke Application, Render Response). Dữ liệu người dùng nhập trên giao diện được gán trực tiếp vào các property của ManagedBean một cách tự động.

### 2. Validation Dữ Liệu (Data Validation)
* **Servlet/JSP (Lab 7):** Mã kiểm tra hợp lệ bị rải rác trong Servlet. Nếu form có 10 trường, cần viết 10 đoạn mã `if` khác nhau để kiểm tra rỗng, đúng định dạng email, số âm,...
* **JSF (Lab 8):** Sử dụng chuẩn **JSR-380 (Bean Validation)**. Các quy tắc ràng buộc được khai báo duy nhất 1 lần trên class Model. JSF sẽ tự động kích hoạt validation trước khi gọi hàm lưu dữ liệu.

### 3. Hiển Thị Thông Báo Kết Quả (Messaging & Error Handling)
* **Servlet/JSP (Lab 7):** Cần lưu các biến lỗi vào `HttpServletRequest`, chuyển tiếp (forward) sang trang JSP và dùng JSTL `<c:if>` hoặc `<c:forEach>` để hiển thị.
* **JSF (Lab 8):** Sử dụng các thẻ chuẩn `<h:message for="fieldId"/>` ngay dưới từng ô nhập liệu. Quản lý thông báo chung qua `FacesContext.getCurrentInstance().addMessage()`.

### 4. Điều Hướng (Navigation Management)
* **Servlet/JSP (Lab 7):** Đổi trang bằng các chuỗi URL cứng trong `forward()` hoặc `sendRedirect()`.
* **JSF (Lab 8):** Hàm xử lý trong ManagedBean chỉ cần trả về tên view (Outcome-based navigation) hoặc dùng URL redirect `?faces-redirect=true` giúp quản lý luồng ứng dụng rõ ràng, linh hoạt.

### 5. Kiến Trúc & Mức Độ Tách Mã (Software Architecture)
* **Servlet/JSP (Lab 7):** Phụ thuộc chặt chẽ vào các đối tượng Servlet API (`HttpServletRequest`, `HttpServletResponse`, `HttpSession`).
* **JSF (Lab 8):** Tách biệt tuyệt đối giữa View (XHTML Facelets) và Controller/Model (ManagedBean). Bean là các POJO thuần túy, dễ dàng thực hiện Unit Test và bảo trì lâu dài.
