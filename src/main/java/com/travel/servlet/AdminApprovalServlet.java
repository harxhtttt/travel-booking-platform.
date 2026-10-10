package com.travel.servlet;

import com.travel.dao.ListingDao;
import com.travel.model.ListingStatus;
import com.travel.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Servlet for Admin approving/rejecting listings. */
@WebServlet("/admin/approve")
public class AdminApprovalServlet extends HttpServlet {
    private final ListingDao listingDao = new ListingDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !user.getRole().name().equals("ADMIN")) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            int listingId = Integer.parseInt(req.getParameter("listingId"));
            String action = req.getParameter("action"); // 'APPROVE' or 'REJECT'

            if ("APPROVE".equals(action)) {
                listingDao.updateStatus(listingId, ListingStatus.APPROVED);
                req.getSession().setAttribute("success", "Listing " + listingId + " approved successfully.");
            } else if ("REJECT".equals(action)) {
                listingDao.updateStatus(listingId, ListingStatus.REJECTED);
                req.getSession().setAttribute("success", "Listing " + listingId + " rejected.");
            } else {
                throw new IllegalArgumentException("Invalid action.");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Error processing approval: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
    }
}
