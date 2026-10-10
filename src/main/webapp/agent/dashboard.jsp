<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Agent Dashboard - Travel Booking</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>✈️ Welcome, ${user.name} (Agent)</h2>
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

        <div class="card">
            <h3>Add New Hotel Listing</h3>
            <form action="${pageContext.request.contextPath}/agent/add-hotel" method="post" class="form-grid" style="margin-top: 1rem;">
                <div class="form-group">
                    <input type="text" name="title" id="title" placeholder=" " required>
                    <label for="title">Hotel Title</label>
                </div>
                <div class="form-group">
                    <input type="text" name="city" id="city" placeholder=" " required>
                    <label for="city">City</label>
                </div>
                <div class="form-group">
                    <input type="number" step="0.01" name="price" id="price" placeholder=" " required min="1">
                    <label for="price">Price per Night (₹)</label>
                </div>
                <div class="form-group">
                    <input type="number" name="rooms" id="rooms" placeholder=" " required min="1">
                    <label for="rooms">Rooms Available</label>
                </div>
                <div style="grid-column: 1 / -1;">
                    <button type="submit" class="btn btn-primary" style="max-width: 200px;">Submit Listing</button>
                </div>
            </form>
        </div>

        <div class="card mt-4">
            <h3>My Listings</h3>
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Type</th>
                            <th>Price/Night</th>
                            <th>Rooms Left</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="listing" items="${listings}">
                            <tr>
                                <td>${listing.id}</td>
                                <td>${listing.title}</td>
                                <td><strong>${listing.typeLabel}</strong></td>
                                <td>₹${listing.price}</td>
                                <td>${listing.availableCount}</td>
                                <td>
                                    <span class="badge badge-${listing.status.name().toLowerCase()}">
                                        ${listing.status}
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listings}">
                            <tr><td colspan="6" class="text-muted" style="text-align: center;">You have no listings yet.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
