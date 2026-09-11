package vn.edu.eaut.lab16.warehouse;

import vn.edu.eaut.lab16.shared.entity.Order;
import vn.edu.eaut.lab16.shared.entity.Product;
import vn.edu.eaut.lab16.shared.repository.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;

public class WarehouseApp extends JFrame {
    private DatabaseConnection db;
    private JTable ordersTable;
    private JTable inventoryTable;
    private DefaultTableModel ordersModel;
    private DefaultTableModel inventoryModel;
    private JLabel statusLabel;
    private JButton refreshBtn, processStockBtn, packOrderBtn;
    private Order selectedOrder;

    public WarehouseApp() {
        try {
            db = DatabaseConnection.getInstance();
            initComponents();
            loadData();
            setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khởi tạo: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initComponents() {
        setTitle("📦 Kho Hàng - Hệ Thống Xử Lý Đơn Hàng");
        setSize(1400, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Color scheme
        Color primaryColor = new Color(0, 123, 255);
        Color darkColor = new Color(52, 58, 64);
        Color lightColor = new Color(233, 236, 239);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(darkColor);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("📦  KHO HÀNG - XỬ LÝ TỒN KHO VÀ ĐÓNG GÓI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 20, 0, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JLabel platformLabel = new JLabel("🖥️ Java Swing Platform");
        platformLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        platformLabel.setForeground(Color.WHITE);
        platformLabel.setBorder(new EmptyBorder(0, 0, 0, 20));
        headerPanel.add(platformLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new JSplitPane(JSplitPane.VERTICAL_SPLIT, createOrdersPanel(), createInventoryPanel()));
        ((JSplitPane) mainPanel.getComponent(0)).setDividerLocation(400);

        add(mainPanel, BorderLayout.CENTER);

        // Status Bar
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
                new EmptyBorder(5, 10, 5, 10)));
        statusPanel.setLayout(new BorderLayout());

        statusLabel = new JLabel("Sẵn sàng");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(statusLabel, BorderLayout.WEST);

        refreshBtn = new JButton("🔄 Làm mới");
        refreshBtn.addActionListener(e -> loadData());
        statusPanel.add(refreshBtn, BorderLayout.EAST);

        add(statusPanel, BorderLayout.SOUTH);
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "📋 Đơn Hàng Cần Xử Lý"));

        String[] columns = {"ID", "Mã ĐH", "Khách hàng", "Ngày tạo", "Trạng thái", "Tổng tiền"};
        ordersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        ordersTable = new JTable(ordersModel);
        ordersTable.setFont(new Font("Arial", Font.PLAIN, 13));
        ordersTable.setRowHeight(30);
        ordersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        ordersTable.getTableHeader().setBackground(new Color(0, 123, 255));
        ordersTable.getTableHeader().setForeground(Color.WHITE);

        // Center align
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < ordersTable.getColumnCount(); i++) {
            ordersTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

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

        processStockBtn = new JButton("📊 Xử Lý Tồn Kho");
        processStockBtn.setBackground(new Color(255, 193, 7));
        processStockBtn.setForeground(Color.BLACK);
        processStockBtn.setFont(new Font("Arial", Font.BOLD, 13));
        processStockBtn.setPreferredSize(new Dimension(180, 40));
        processStockBtn.addActionListener(e -> processStock());
        processStockBtn.setEnabled(false);

        packOrderBtn = new JButton("📦 Đóng Gói Đơn Hàng");
        packOrderBtn.setBackground(new Color(40, 167, 69));
        packOrderBtn.setForeground(Color.WHITE);
        packOrderBtn.setFont(new Font("Arial", Font.BOLD, 13));
        packOrderBtn.setPreferredSize(new Dimension(180, 40));
        packOrderBtn.addActionListener(e -> packOrder());
        packOrderBtn.setEnabled(false);

        btnPanel.add(processStockBtn);
        btnPanel.add(packOrderBtn);

        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "📦 Tồn Kho Sản Phẩm"));

        String[] columns = {"Mã SP", "Tên sản phẩm", "Giá", "Tồn kho", "Trạng thái"};
        inventoryModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        inventoryTable = new JTable(inventoryModel);
        inventoryTable.setFont(new Font("Arial", Font.PLAIN, 13));
        inventoryTable.setRowHeight(30);
        inventoryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        inventoryTable.getTableHeader().setBackground(new Color(23, 162, 184));
        inventoryTable.getTableHeader().setForeground(Color.WHITE);

        // Color rows based on stock
        inventoryTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    int stock = (int) table.getValueAt(row, 3);
                    if (stock <= 0) {
                        c.setBackground(new Color(255, 198, 198));
                    } else if (stock < 10) {
                        c.setBackground(new Color(255, 243, 205));
                    } else {
                        c.setBackground(Color.WHITE);
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
        statusLabel.setText("Đã tải dữ liệu lúc: " +
                java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void loadOrders() {
        ordersModel.setRowCount(0);
        try {
            List<Order> orders = db.getAllOrders();
            // Filter orders that need processing
            orders.stream()
                    .filter(o -> o.getStatus() == Order.OrderStatus.PENDING ||
                                 o.getStatus() == Order.OrderStatus.CONFIRMED ||
                                 o.getStatus() == Order.OrderStatus.PROCESSING ||
                                 o.getStatus() == Order.OrderStatus.PACKED)
                    .forEach(order -> {
                        Vector<Object> row = new Vector<>();
                        row.add(order.getId());
                        row.add(order.getOrderNumber());
                        row.add(order.getCustomerName());
                        row.add(order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                        row.add(order.getStatus().getDisplayName());
                        row.add(String.format("%,.0f VNĐ", order.getTotalAmount()));
                        ordersModel.addRow(row);
                    });
        } catch (SQLException e) {
            showError("Lỗi tải đơn hàng: " + e.getMessage());
        }
    }

    private void loadInventory() {
        inventoryModel.setRowCount(0);
        try {
            List<Product> products = db.getAllProducts();
            products.forEach(product -> {
                Vector<Object> row = new Vector<>();
                row.add(product.getProductCode());
                row.add(product.getProductName());
                row.add(String.format("%,.0f VNĐ", product.getPrice()));
                row.add(product.getStockQuantity());
                row.add(product.isInStock() ? "✅ Còn hàng" : "❌ Hết hàng");
                inventoryModel.addRow(row);
            });
        } catch (SQLException e) {
            showError("Lỗi tải tồn kho: " + e.getMessage());
        }
    }

    private void loadOrderDetails(Long orderId) {
        try {
            selectedOrder = db.getOrderById(orderId);
            if (selectedOrder != null) {
                updateButtonStates();
            }
        } catch (SQLException e) {
            showError("Lỗi tải chi tiết đơn hàng: " + e.getMessage());
        }
    }

    private void updateButtonStates() {
        if (selectedOrder == null) {
            processStockBtn.setEnabled(false);
            packOrderBtn.setEnabled(false);
            return;
        }

        switch (selectedOrder.getStatus()) {
            case PENDING:
            case CONFIRMED:
                processStockBtn.setEnabled(true);
                packOrderBtn.setEnabled(false);
                break;
            case PROCESSING:
                processStockBtn.setEnabled(false);
                packOrderBtn.setEnabled(true);
                break;
            default:
                processStockBtn.setEnabled(false);
                packOrderBtn.setEnabled(false);
        }
    }

    private void processStock() {
        if (selectedOrder == null) return;

        try {
            // Check stock availability
            StringBuilder stockReport = new StringBuilder();
            boolean allAvailable = true;

            for (var item : selectedOrder.getItems()) {
                Product product = db.getProductByCode(item.getProductCode());
                if (product == null || product.getStockQuantity() < item.getQuantity()) {
                    allAvailable = false;
                    stockReport.append(String.format("- %s: Cần %d, Còn %d\n",
                            item.getProductName(), item.getQuantity(),
                            product != null ? product.getStockQuantity() : 0));
                }
            }

            if (!allAvailable) {
                JOptionPane.showMessageDialog(this,
                        "⚠️ Không đủ hàng trong kho:\n" + stockReport,
                        "Cảnh báo tồn kho", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Deduct stock
            for (var item : selectedOrder.getItems()) {
                db.updateProductStock(item.getProductCode(), -item.getQuantity());
            }

            // Update order status
            db.updateOrderStatus(selectedOrder.getId(), Order.OrderStatus.PROCESSING);

            JOptionPane.showMessageDialog(this,
                    "✅ Đã xử lý tồn kho thành công!\nĐơn hàng đã được chuyển sang trạng thái 'Đang xử lý'.",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

            loadData();
            selectedOrder = db.getOrderById(selectedOrder.getId());
            updateButtonStates();

        } catch (SQLException e) {
            showError("Lỗi xử lý tồn kho: " + e.getMessage());
        }
    }

    private void packOrder() {
        if (selectedOrder == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận đóng gói đơn hàng " + selectedOrder.getOrderNumber() + "?",
                "Xác nhận đóng gói", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                db.updateOrderStatus(selectedOrder.getId(), Order.OrderStatus.PACKED);

                JOptionPane.showMessageDialog(this,
                        "📦 Đơn hàng đã được đóng gói!\nChờ quản lý phê duyệt.",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);

                loadData();
                selectedOrder = db.getOrderById(selectedOrder.getId());
                updateButtonStates();

            } catch (SQLException e) {
                showError("Lỗi đóng gói: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("❌ " + message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new WarehouseApp();
        });
    }
}
