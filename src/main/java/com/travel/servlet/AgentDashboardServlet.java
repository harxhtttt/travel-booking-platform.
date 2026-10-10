package com.travel.servlet;

import com.travel.dao.ListingDao;
import com.travel.model.Listing;
import com.travel.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/** Servlet for the Agent Dashboard. */
@WebServlet("/agent/dashboard")
public class AgentDashboardServlet extends HttpServlet {
    private final ListingDao listingDao = new ListingDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !user.getRole().name().equals("AGENT")) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Fetch agent's listings
        List<Listing> listings = listingDao.findByAgent(user.getId());
        req.setAttribute("listings", listings);
        
        req.getRequestDispatcher("/agent/dashboard.jsp").forward(req, resp);
    }
}
