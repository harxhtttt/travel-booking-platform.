package com.travel.servlet;

import com.travel.model.User;
import com.travel.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = authService.login(req.getParameter("email"), req.getParameter("password"));
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            
            switch (user.getRole()) {
                case ADMIN: resp.sendRedirect(req.getContextPath() + "/admin/dashboard"); break;
                case AGENT: resp.sendRedirect(req.getContextPath() + "/agent/dashboard"); break;
                case TRAVELER: resp.sendRedirect(req.getContextPath() + "/traveler/dashboard"); break;
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }
    }
}
