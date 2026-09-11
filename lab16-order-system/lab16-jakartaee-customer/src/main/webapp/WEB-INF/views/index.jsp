<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cửa Hàng Điện Tử - Trang Chủ</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 40px;
            max-width: 600px;
            width: 100%;
            text-align: center;
        }
        h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 2em;
        }
        .subtitle {
            color: #666;
            margin-bottom: 40px;
            font-size: 1.1em;
        }
        .menu {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }
        .menu-btn {
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px 30px;
            border-radius: 12px;
            text-decoration: none;
            font-size: 1.2em;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        .btn-create {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        .btn-create:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
        }
        .btn-track {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
        }
        .btn-track:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 30px rgba(245, 87, 108, 0.4);
        }
        .icon {
            margin-right: 10px;
            font-size: 1.3em;
        }
        .footer {
            margin-top: 30px;
            color: #999;
            font-size: 0.9em;
        }
        .platform-badge {
            display: inline-block;
            background: #e8e8e8;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.8em;
            color: #666;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="platform-badge">🖥️ Jakarta EE Platform</div>
        <h1>🏪 Cửa Hàng Điện Tử</h1>
        <p class="subtitle">Hệ thống quản lý đơn hàng trực tuyến</p>

        <div class="menu">
            <a href="${pageContext.request.contextPath}/create-order" class="menu-btn btn-create">
                <span class="icon">📦</span>
                Tạo Đơn Hàng Mới
            </a>
            <a href="${pageContext.request.contextPath}/track-order" class="menu-btn btn-track">
                <span class="icon">🔍</span>
                Theo Dõi Đơn Hàng
            </a>
        </div>

        <div class="footer">
            <p>Luồng: Khách hàng tạo đơn → Kho xử lý → Quản lý phê duyệt → Giao hàng → Xác nhận hoàn thành</p>
        </div>
    </div>
</body>
</html>
