package vn.edu.eaut.warehouse.view;

import vn.edu.eaut.lab16.shared.entity.Order;
import vn.edu.eaut.lab16.shared.entity.OrderHistory;
import vn.edu.eaut.lab16.shared.entity.Product;
import vn.edu.eaut.lab16.shared.enums.OrderStatus;
import vn.edu.eaut.lab16.shared.exception.ConflictException;
import vn.edu.eaut.lab16.shared.exception.OrderLockedException;
import vn.edu.eaut.lab16.shared.repository.DatabaseConnection;
import vn.edu.eaut.warehouse.service.WarehouseService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;

/**
 * Main Frame for Warehouse Desktop Application.
 *
 * Integration Principles:
 * - UI does not change status directly
 * - All business logic in WarehouseService
 * - Shows lock status for orders
 * - Displays order history
 *
 * Uses FlatLaf for modern UI rendering.
 */
public class MainFrame extends JFrame {
    private WarehouseService warehouseService;
    private JTable ordersTable;
    private JTable inventoryTable;
    private DefaultTableModel ordersModel;
    private DefaultTableModel inventoryModel;
    private JLabel statusLabel;
    private JButton acceptOrderBtn, processStockBtn, packOrderBtn, releaseBtn, cancelOrderBtn, refreshBtn;
    private Order selectedOrder;
    private String currentUser = "warehouse1"; // Simulated login

    public MainFrame() {
        // Initialize service (also triggers DB schema init)
        DatabaseConnection.getInstance();
        warehouseService = new WarehouseService(currentUser);
        initComponents();
        loadData();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("📦 Kho Hàng - Hệ Thống Xử Lý Đơn Hàng");
        setSize(1500, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setPreferredSize(new Dimension(0, 75));
        headerPanel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(new EmptyBorder(0, 20, 0, 0));

        JLabel titleLabel = new JLabel("📦  KHO HÀNG - XỬ LÝ TỒN KHO VÀ ĐÓNG GÓI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("👤 Người dùng: " + currentUser
                + "  |  🖥️ Host: " + warehouseService.getHostname()
                + "  |  🖥️ Java Swing + FlatLaf");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(new Color(180, 180, 180));

        titlePanel.add(titleLabel);
        titlePanel.add(userLabel);
        headerPanel.add(titlePanel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Main Content - Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(450);
        splitPane.setTopComponent(createOrdersPanel());
        splitPane.setBottomComponent(createInventoryPanel());
        splitPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(splitPane, BorderLayout.CENTER);

        // Status Bar
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)));
        statusPanel.setLayout(new BorderLayout());

        statusLabel = new JLabel("Sẵn sàng xử lý đơn hàng");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusPanel.add(statusLabel, BorderLayout.WEST);

        refreshBtn = new JButton("🔄 Làm Mới");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshBtn.addActionListener(e -> loadData());
        statusPanel.add(refreshBtn, BorderLayout.EAST);

        add(statusPanel, BorderLayout.SOUTH);
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "📋 Đơn Hàng Cần Xử Lý"));

        String[] columns = {"ID", "Mã ĐH", "Khách hàng", "Ngày tạo", "Trạng thái", "Khóa", "Tổng tiền"};
        ordersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        ordersTable = new JTable(ordersModel);
        ordersTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ordersTable.setRowHeight(28);
        ordersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        ordersTable.getTableHeader().setBackground(new Color(102, 126, 234));
        ordersTable.getTableHeader().setForeground(Color.WHITE);

        // Center all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < ordersTable.getColumnCount(); i++) {
            ordersTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Color code by lock status
        ordersTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    String lockedBy = String.valueOf(table.getValueAt(row, 5));
                    if (lockedBy != null && !lockedBy.isEmpty() && !"null".equals(lockedBy)) {
                        c.setBackground(lockedBy.equals(currentUser)
                                ? new Color(220, 255, 220)  // Light green - locked by me
                                : new Color(255, 220, 220)); // Light red - locked by someone
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    }
                }
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        ordersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = ordersTable.getSelectedRow();
                if (row >= 0) {
                    Long orderId = (Long) ordersModel.getValueAt(row, 0);
                    loadOrderDetails(orderId);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        acceptOrderBtn = new JButton("📥 Tiếp Nhận");
        acceptOrderBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        acceptOrderBtn.setPreferredSize(new Dimension(150, 38));
        acceptOrderBtn.addActionListener(e -> acceptOrder());
        acceptOrderBtn.setEnabled(false);

        processStockBtn = new JButton("📊 Kiểm Tra Tồn Kho");
        processStockBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        processStockBtn.setPreferredSize(new Dimension(190, 38));
        processStockBtn.addActionListener(e -> processStock());
        processStockBtn.setEnabled(false);

        packOrderBtn = new JButton("📦 Đóng Gói");
        packOrderBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        packOrderBtn.setPreferredSize(new Dimension(150, 38));
        packOrderBtn.addActionListener(e -> packOrder());
        packOrderBtn.setEnabled(false);

        releaseBtn = new JButton("🔓 Mở Khóa");
        releaseBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        releaseBtn.setPreferredSize(new Dimension(130, 38));
        releaseBtn.addActionListener(e -> releaseOrder());
        releaseBtn.setEnabled(false);

        cancelOrderBtn = new JButton("❌ Hủy Đơn");
        cancelOrderBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelOrderBtn.setPreferredSize(new Dimension(130, 38));
        cancelOrderBtn.addActionListener(e -> cancelOrder());
        cancelOrderBtn.setEnabled(false);

        btnPanel.add(acceptOrderBtn);
        btnPanel.add(processStockBtn);
        btnPanel.add(packOrderBtn);
        btnPanel.add(releaseBtn);
        btnPanel.add(cancelOrderBtn);

        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "📦 Tồn Kho Sản Phẩm"));

        String[] columns = {"Mã SP", "Tên sản phẩm", "Giá", "Tồn kho", "Trạng thái"};
        inventoryModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        inventoryTable = new JTable(inventoryModel);
        inventoryTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        inventoryTable.setRowHeight(28);
        inventoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        inventoryTable.getTableHeader().setBackground(new Color(23, 162, 184));
        inventoryTable.getTableHeader().setForeground(Color.WHITE);

        // Color by stock level
        inventoryTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    int stock = Integer.parseInt(String.valueOf(table.getValueAt(row, 3)));
                    if (stock <= 0) {
                        c.setBackground(new Color(255, 200, 200));
                    } else if (stock < 10) {
                        c.setBackground(new Color(255, 245, 200));
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    }
                }
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadData() {
        loadOrders();
        loadInventory();
        statusLabel.setText("Đã tải dữ liệu lúc: "
                + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void loadOrders() {
        ordersModel.setRowCount(0);
        try {
            List<Order> orders = warehouseService.getOrdersForWarehouse();
            for (Order order : orders) {
                Vector<Object> row = new Vector<>();
                row.add(order.getId());
                row.add(order.getOrderNumber());
                row.add(order.getCustomerName());
                row.add(order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                row.add(order.getStatus().getDisplayName());
                row.add(order.getLockedBy() != null ? order.getLockedBy() : "");
                row.add(String.format("%,.0f ₫", order.getTotalAmount()));
                ordersModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải đơn hàng: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadInventory() {
        inventoryModel.setRowCount(0);
        try {
            List<Product> products = warehouseService.getAllProducts();
            for (Product product : products) {
                Vector<Object> row = new Vector<>();
                row.add(product.getProductCode());
                row.add(product.getProductName());
                row.add(String.format("%,.0f ₫", product.getPrice()));
                row.add(product.getStockQuantity());
                row.add(product.isInStock() ? "✅ Còn hàng" : "❌ Hết hàng");
                inventoryModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải tồn kho: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadOrderDetails(Long orderId) {
        try {
            selectedOrder = warehouseService.getOrderById(orderId);
            if (selectedOrder != null) {
                updateButtonStates();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải chi tiết: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateButtonStates() {
        if (selectedOrder == null) {
            acceptOrderBtn.setEnabled(false);
            processStockBtn.setEnabled(false);
            packOrderBtn.setEnabled(false);
            releaseBtn.setEnabled(false);
            cancelOrderBtn.setEnabled(false);
            return;
        }

        OrderStatus status = selectedOrder.getStatus();
        boolean isLockedByMe = selectedOrder.isLockedBy(currentUser);
        boolean isLockedByOther = selectedOrder.isLocked() && !isLockedByMe;

        acceptOrderBtn.setEnabled(status == OrderStatus.PENDING && !isLockedByOther);
        processStockBtn.setEnabled(status == OrderStatus.PROCESSING && isLockedByMe);
        packOrderBtn.setEnabled(status == OrderStatus.PROCESSING && isLockedByMe);
        releaseBtn.setEnabled(isLockedByMe);
        cancelOrderBtn.setEnabled(status == OrderStatus.PROCESSING && isLockedByMe);
    }

    private void acceptOrder() {
        if (selectedOrder == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận tiếp nhận đơn hàng " + selectedOrder.getOrderNumber() + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            String result = warehouseService.acceptOrder(selectedOrder.getId());
            if ("SUCCESS".equals(result)) {
                JOptionPane.showMessageDialog(this,
                        "✅ Đã tiếp nhận đơn hàng!\nĐơn hàng được khóa và chuyển sang 'Đang xử lý'.",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                selectedOrder = warehouseService.getOrderById(selectedOrder.getId());
                updateButtonStates();
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ " + result, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (OrderLockedException ole) {
            JOptionPane.showMessageDialog(this, "❌ " + ole.getMessage(), "Đơn bị khóa", JOptionPane.ERROR_MESSAGE);
        } catch (ConflictException ce) {
            JOptionPane.showMessageDialog(this,
                    "⚠️ XUNG ĐỘT DỮ LIỆU!\n" + ce.getMessage()
                    + "\nĐơn hàng có thể đã bị thay đổi bởi nền tảng khác. Vui lòng tải lại.",
                    "Xung đột", JOptionPane.WARNING_MESSAGE);
            loadData();
            selectedOrder = null;
            updateButtonStates();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processStock() {
        if (selectedOrder == null) return;
        try {
            String result = warehouseService.processStock(selectedOrder.getId());
            if ("SUCCESS".equals(result)) {
                JOptionPane.showMessageDialog(this,
                        "✅ Đã kiểm tra và trừ tồn kho thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                selectedOrder = warehouseService.getOrderById(selectedOrder.getId());
                updateButtonStates();
            } else if (result.startsWith("WARNING:")) {
                JOptionPane.showMessageDialog(this, "⚠️ " + result.substring(8),
                        "Cảnh báo tồn kho", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "❌ " + result, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void packOrder() {
        if (selectedOrder == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận đóng gói đơn hàng " + selectedOrder.getOrderNumber() + "?\n"
                + "Đơn hàng sẽ được chuyển cho quản lý phê duyệt.",
                "Xác nhận đóng gói", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            String result = warehouseService.packOrder(selectedOrder.getId());
            if ("SUCCESS".equals(result)) {
                JOptionPane.showMessageDialog(this,
                        "📦 Đơn hàng đã được đóng gói!\nChờ quản lý phê duyệt giao hàng.",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                selectedOrder = null;
                updateButtonStates();
            } else {
                JOptionPane.showMessageDialog(this, "❌ " + result, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void releaseOrder() {
        if (selectedOrder == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "Mở khóa đơn hàng " + selectedOrder.getOrderNumber() + "?\n"
                + "Đơn hàng sẽ có thể được tiếp nhận bởi người khác.",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            String result = warehouseService.releaseOrder(selectedOrder.getId());
            if ("SUCCESS".equals(result)) {
                JOptionPane.showMessageDialog(this, "🔓 Đã mở khóa đơn hàng!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                selectedOrder = warehouseService.getOrderById(selectedOrder.getId());
                updateButtonStates();
            } else {
                JOptionPane.showMessageDialog(this, "❌ " + result, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelOrder() {
        if (selectedOrder == null) return;
        String reason = JOptionPane.showInputDialog(this,
                "Nhập lý do hủy đơn hàng " + selectedOrder.getOrderNumber() + ":",
                "Hủy đơn hàng", JOptionPane.QUESTION_MESSAGE);
        if (reason == null) return;
        if (reason.trim().isEmpty()) reason = "Nhân viên kho hủy đơn";

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận HỦY đơn hàng " + selectedOrder.getOrderNumber() + "?\n"
                + "Nếu đã trừ tồn kho, hệ thống sẽ hoàn lại số lượng.",
                "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            String result = warehouseService.cancelOrder(selectedOrder.getId(), reason);
            if ("SUCCESS".equals(result)) {
                JOptionPane.showMessageDialog(this,
                        "❌ Đã hủy đơn hàng!\n"
                        + "Đơn hàng được chuyển sang CANCELLED.\n"
                        + (selectedOrder.isStockDeducted() ? "Tồn kho đã được hoàn lại." : ""),
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                selectedOrder = null;
                updateButtonStates();
            } else {
                JOptionPane.showMessageDialog(this, "❌ " + result, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ConflictException ce) {
            JOptionPane.showMessageDialog(this,
                    "⚠️ XUNG ĐỘT DỮ LIỆU!\n" + ce.getMessage()
                    + "\nVui lòng tải lại dữ liệu.",
                    "Xung đột", JOptionPane.WARNING_MESSAGE);
            loadData();
            selectedOrder = null;
            updateButtonStates();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
