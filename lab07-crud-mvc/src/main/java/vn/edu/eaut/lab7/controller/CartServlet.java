package vn.edu.eaut.lab7.controller;

import vn.edu.eaut.lab7.model.CartItem;
import vn.edu.eaut.lab7.model.SanPham;
import vn.edu.eaut.lab7.repository.SanPhamRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/cart")
public class CartServlet extends HttpServlet {
    private SanPhamRepository repo = new SanPhamRepository();

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "view";

        HttpSession session = req.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        switch (action) {
            case "add":
                String id = req.getParameter("id");
                SanPham sp = repo.findById(id);
                if (sp != null) {
                    boolean found = false;
                    for (CartItem item : cart) {
                        if (item.getProduct().getId().equals(id)) {
                            item.setQuantity(item.getQuantity() + 1);
                            found = true;
                            break;
                        }
                    }
                    if (!found) cart.add(new CartItem(sp, 1));
                }
                resp.sendRedirect(req.getContextPath() + "/admin/cart");
                break;
            case "remove":
                String removeId = req.getParameter("id");
                cart.removeIf(item -> item.getProduct().getId().equals(removeId));
                resp.sendRedirect(req.getContextPath() + "/admin/cart");
                break;
            case "view":
            default:
                double totalMoney = cart.stream().mapToDouble(CartItem::getTotalPrice).sum();
                req.setAttribute("totalMoney", totalMoney);
                req.getRequestDispatcher("/views/cart/index.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        
        if (cart != null) {
            String id = req.getParameter("id");
            int qty = Integer.parseInt(req.getParameter("quantity"));
            for (CartItem item : cart) {
                if (item.getProduct().getId().equals(id)) {
                    if (qty > 0) {
                        item.setQuantity(qty);
                    } else {
                        cart.remove(item);
                    }
                    break;
                }
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/cart");
    }
}
