<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Traveler Dashboard - Travel Booking</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>✈️ Welcome, ${user.name}</h2>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline">Logout</a>
    </div>

    <div class="container">
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success">${sessionScope.success}</div>
            <c:remove var="success" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger">${sessionScope.error}</div>
            <c:remove var="error" scope="session" />
        </c:if>

        <!-- Search Section -->
        <div class="card" style="text-align: center; padding: 3rem 1rem;">
            <h1 style="color: var(--navy); margin-bottom: 1rem; font-size: 2.5rem;">Where to next?</h1>
            <p style="color: var(--text-muted); margin-bottom: 2rem;">Discover perfect stays and seamless journeys.</p>
            
            <form action="${pageContext.request.contextPath}/traveler/search" method="get" class="search-form" style="max-width: 600px; margin: 0 auto;">
                <div class="form-group" style="margin-bottom: 0;">
                    <input type="text" name="q" id="q" placeholder=" " value="${searchQuery}" required style="border-radius: 50px; padding: 1.2rem 1.5rem;">
                    <label for="q" style="left: 1.5rem;">Search city or hotel name...</label>
                </div>
                <button type="submit" class="btn btn-primary" style="border-radius: 50px; padding: 0 2.5rem; font-size: 1.1rem;">🔍 Search</button>
            </form>
        </div>

        <c:if test="${not empty searchResults}">
            <h3 class="mt-4" style="margin-top: 3rem;">Search Results for "${searchQuery}"</h3>
            <div class="results-grid">
                <c:forEach var="hotel" items="${searchResults}">
                    <div class="result-card">
                        <div class="result-header">
                            <h4>${hotel.title}</h4>
                        </div>
                        <div class="result-body">
                            <p><strong>📍 City:</strong> ${hotel.city}</p>
                            <p><strong>💰 Price:</strong> ₹${hotel.price} / night</p>
                            <p><strong>🚪 Available Rooms:</strong> ${hotel.availableCount}</p>
                            
                            <form action="${pageContext.request.contextPath}/traveler/book" method="post" class="mt-4">
                                <input type="hidden" name="listingId" value="${hotel.id}">
                                <div class="form-group">
                                    <input type="date" name="checkIn" required style="padding: 0.8rem; font-size: 0.9rem;">
                                </div>
                                <div class="form-group">
                                    <input type="date" name="checkOut" required style="padding: 0.8rem; font-size: 0.9rem;">
                                </div>
                                <button type="submit" class="btn btn-success w-100">Book Now</button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
        
        <c:if test="${empty searchResults && searchQuery != null}">
            <div class="card mt-4" style="text-align: center;">
                <h3 style="color: var(--text-muted);">No hotels found matching "${searchQuery}".</h3>
                <p>Try searching for a different city or hotel name.</p>
            </div>
        </c:if>

        <!-- My Bookings Section -->
        <div class="card" style="margin-top: 3rem;">
            <h3>My Bookings</h3>
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Booking ID</th>
                            <th>Hotel</th>
                            <th>Check-in</th>
                            <th>Check-out</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="booking" items="${myBookings}">
                            <tr>
                                <td>#${booking.id}</td>
                                <td><strong>${listingTitles[booking.listingId]}</strong></td>
                                <td>${booking.startDate}</td>
                                <td>${booking.endDate}</td>
                                <td>
                                    <span class="badge badge-${booking.status.name().toLowerCase()}">
                                        ${booking.status}
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty myBookings}">
                            <tr><td colspan="5" class="text-muted" style="text-align: center;">You have no bookings yet. Time to explore!</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
