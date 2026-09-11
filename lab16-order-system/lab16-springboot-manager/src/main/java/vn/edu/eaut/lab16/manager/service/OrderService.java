package vn.edu.eaut.lab16.manager.service;

import org.springframework.stereotype.Service;
import vn.edu.eaut.lab16.shared.entity.Order;
import vn.edu.eaut.lab16.shared.entity.Product;
import vn.edu.eaut.lab16.shared.repository.DatabaseConnection;

import java.sql.SQLException;
import java.util.List;

@Service
public class OrderService {

    private final DatabaseConnection db;

    public OrderService(DatabaseConnection db) {
        this.db = db;
    }

    public List<Order> getAllOrders() throws SQLException {
        return db.getAllOrders();
    }

    public List<Order> getOrdersByStatus(Order.OrderStatus status) throws SQLException {
        return db.getOrdersByStatus(status);
    }

    public Order getOrderById(Long id) throws SQLException {
        return db.getOrderById(id);
    }

    public void updateOrderStatus(Long orderId, Order.OrderStatus newStatus) throws SQLException {
        db.updateOrderStatus(orderId, newStatus);
    }

    public List<Product> getAllProducts() throws SQLException {
        return db.getAllProducts();
    }

    public long getTotalOrders() throws SQLException {
        return db.getAllOrders().size();
    }

    public long getPendingOrders() throws SQLException {
        return db.getOrdersByStatus(Order.OrderStatus.PENDING).size();
    }

    public long getProcessingOrders() throws SQLException {
        return db.getOrdersByStatus(Order.OrderStatus.PROCESSING).size() +
               db.getOrdersByStatus(Order.OrderStatus.PACKED).size();
    }

    public long getCompletedOrders() throws SQLException {
        return db.getOrdersByStatus(Order.OrderStatus.COMPLETED).size();
    }
}
