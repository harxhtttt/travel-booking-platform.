package com.travel.servlet;

import com.travel.dao.BookingDao;
import com.travel.dao.ListingDao;
import com.travel.model.Booking;
import com.travel.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/** Servlet for Traveler Dashboard. Shows their bookings. */
@WebServlet("/traveler/dashboard")
public class TravelerDashboardServlet extends HttpServlet {
    private final BookingDao bookingDao = new BookingDao();
    private final ListingDao listingDao = new ListingDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !user.getRole().name().equals("TRAVELER")) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Fetch traveler's bookings
        List<Booking> myBookings = bookingDao.findByTraveler(user.getId());
        req.setAttribute("myBookings", myBookings);
        
        java.util.Map<Integer, String> listingTitles = new java.util.HashMap<>();
        for (Booking b : myBookings) {
            com.travel.model.Listing l = listingDao.findById(b.getListingId());
            if (l != null) listingTitles.put(b.getListingId(), l.getTitle());
        }
        req.setAttribute("listingTitles", listingTitles);
        
        req.getRequestDispatcher("/traveler/dashboard.jsp").forward(req, resp);
    }
}
