package com.travel.servlet;

import com.travel.dao.BookingDao;
import com.travel.dao.ListingDao;
import com.travel.dao.UserDao;
import com.travel.model.Listing;
import com.travel.model.ListingStatus;
import com.travel.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/** Servlet for the Admin Dashboard. */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private final ListingDao listingDao = new ListingDao();
    private final UserDao userDao = new UserDao();
    private final BookingDao bookingDao = new BookingDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !user.getRole().name().equals("ADMIN")) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Fetch counts for dashboard
        int totalUsers = userDao.findAll().size();
        List<Listing> allListings = listingDao.findAll();
        int totalListings = allListings.size();
        
        List<Listing> pendingListings = listingDao.findByStatus(ListingStatus.PENDING);
        int totalPending = pendingListings.size();
        
        int totalBookings = bookingDao.findAll().size();

        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("totalListings", totalListings);
        req.setAttribute("totalPending", totalPending);
        req.setAttribute("totalBookings", totalBookings);
        req.setAttribute("pendingListings", pendingListings);
        
        req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
    }
}
