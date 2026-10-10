package com.travel.servlet;

import com.travel.exception.TravelException;
import com.travel.model.User;
import com.travel.service.BookingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Servlet for booking a listing. */
@WebServlet("/traveler/book")
public class BookingServlet extends HttpServlet {
    private final BookingService bookingService = new BookingService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !user.getRole().name().equals("TRAVELER")) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            int listingId = Integer.parseInt(req.getParameter("listingId"));
            LocalDate checkIn = LocalDate.parse(req.getParameter("checkIn"));
            LocalDate checkOut = LocalDate.parse(req.getParameter("checkOut"));

            bookingService.book(user.getId(), listingId, checkIn, checkOut);
            req.getSession().setAttribute("success", "Booking confirmed successfully!");
        } catch (DateTimeParseException | NumberFormatException e) {
            req.getSession().setAttribute("error", "Invalid dates or listing provided.");
        } catch (TravelException e) {
            req.getSession().setAttribute("error", e.getMessage());
        } catch (Exception e) {
            req.getSession().setAttribute("error", "An unexpected error occurred: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/traveler/dashboard");
    }
}
