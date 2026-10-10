package com.travel.servlet;

import com.travel.dao.ListingDao;
import com.travel.model.HotelListing;
import com.travel.model.ListingStatus;
import com.travel.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Servlet for adding a hotel listing. */
@WebServlet("/agent/add-hotel")
public class AgentHotelListingServlet extends HttpServlet {
    private final ListingDao listingDao = new ListingDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !user.getRole().name().equals("AGENT")) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            String title = req.getParameter("title");
            String city = req.getParameter("city");
            double price = Double.parseDouble(req.getParameter("price"));
            int rooms = Integer.parseInt(req.getParameter("rooms"));

            if (title == null || title.trim().isEmpty() || city == null || city.trim().isEmpty() || price <= 0 || rooms <= 0) {
                throw new IllegalArgumentException("Invalid input data.");
            }

            HotelListing listing = new HotelListing(0, user.getId(), title, price, rooms, ListingStatus.PENDING, city);
            listingDao.save(listing);
            req.getSession().setAttribute("success", "Hotel listing added successfully and is pending approval.");
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Error adding hotel: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/agent/dashboard");
    }
}
