package vn.edu.eaut.customer.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Index Servlet - Entry point for Customer Portal.
 *
 * Integration Principle: No business logic here, just routing.
 * Handles logout and session setup for logged-in users.
 */
@WebServlet(name = "IndexServlet", urlPatterns = {"/index", "/"})
public class IndexServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        // Initialize database connection on startup (triggers schema init)
        vn.edu.eaut.lab16.shared.repository.DatabaseConnection.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
    }
}
