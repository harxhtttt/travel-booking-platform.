<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - Travel Booking</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>✈️ Welcome, ${user.name} (Admin)</h2>
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

        <div class="stats-grid">
            <div class="stat-card">
                <h3>Total Users</h3>
                <p class="stat-value" data-target="${totalUsers}">0</p>
            </div>
            <div class="stat-card">
                <h3>Total Listings</h3>
                <p class="stat-value" data-target="${totalListings}">0</p>
            </div>
            <div class="stat-card">
                <h3>Pending Listings</h3>
                <p class="stat-value" data-target="${totalPending}">0</p>
            </div>
            <div class="stat-card">
                <h3>Total Bookings</h3>
                <p class="stat-value" data-target="${totalBookings}">0</p>
            </div>
        </div>

        <div class="card">
            <h3>Pending Listings for Approval</h3>
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Agent ID</th>
                            <th>Title</th>
                            <th>Type</th>
                            <th>Price</th>
                            <th>Availability</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="listing" items="${pendingListings}">
                            <tr>
                                <td>${listing.id}</td>
                                <td>${listing.agentId}</td>
                                <td>${listing.title}</td>
                                <td><span class="badge badge-pending">${listing.typeLabel}</span></td>
                                <td>₹${listing.price}</td>
                                <td>${listing.availableCount}</td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/admin/approve" method="post" style="display:inline;">
                                        <input type="hidden" name="listingId" value="${listing.id}">
                                        <input type="hidden" name="action" value="APPROVE">
                                        <button type="submit" class="btn btn-sm btn-success">Approve</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/admin/approve" method="post" style="display:inline;">
                                        <input type="hidden" name="listingId" value="${listing.id}">
                                        <input type="hidden" name="action" value="REJECT">
                                        <button type="submit" class="btn btn-sm btn-danger">Reject</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty pendingListings}">
                            <tr><td colspan="7" class="text-muted" style="text-align: center;">No pending listings at the moment.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Count up animation script -->
    <script>
        document.querySelectorAll('.stat-value').forEach(counter => {
            const target = +counter.getAttribute('data-target');
            const duration = 1000;
            const stepTime = Math.abs(Math.floor(duration / (target || 1)));
            let current = 0;
            if(target === 0) return;
            const timer = setInterval(() => {
                current += 1;
                counter.innerText = current;
                if (current >= target) {
                    clearInterval(timer);
                    counter.innerText = target;
                }
            }, stepTime);
        });
    </script>
</body>
</html>
