package com.travel.servlet;

import com.travel.dao.BookingDao;
import com.travel.dao.ListingDao;
import com.travel.model.Booking;
import com.travel.model.Listing;
import com.travel.model.ListingType;
import com.travel.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/** Servlet for searching approved hotels. */
@WebServlet("/traveler/search")
public class TravelerSearchServlet extends HttpServlet {
    private final ListingDao listingDao = new ListingDao();
    private final BookingDao bookingDao = new BookingDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !user.getRole().name().equals("TRAVELER")) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String query = req.getParameter("q");
        if (query != null && !query.trim().isEmpty()) {
            List<Listing> searchResults = listingDao.searchApproved(ListingType.HOTEL, query);
            req.setAttribute("searchResults", searchResults);
            req.setAttribute("searchQuery", query);
        }

        // Must also provide bookings for the dashboard view below the search
        List<Booking> myBookings = bookingDao.findByTraveler(user.getId());
        req.setAttribute("myBookings", myBookings);

        java.util.Map<Integer, String> listingTitles = new java.util.HashMap<>();
        for (Booking b : myBookings) {
            Listing l = listingDao.findById(b.getListingId());
            if (l != null) listingTitles.put(b.getListingId(), l.getTitle());
        }
        req.setAttribute("listingTitles", listingTitles);
        
        req.getRequestDispatcher("/traveler/dashboard.jsp").forward(req, resp);
    }
}
