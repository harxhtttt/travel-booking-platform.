package com.travel.filter;

import com.travel.model.User;
import com.travel.model.Role;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String path = req.getRequestURI().substring(req.getContextPath().length());

        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (path.startsWith("/admin") && (user == null || user.getRole() != Role.ADMIN)) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (path.startsWith("/agent") && (user == null || user.getRole() != Role.AGENT)) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (path.startsWith("/traveler") && (user == null || user.getRole() != Role.TRAVELER)) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
