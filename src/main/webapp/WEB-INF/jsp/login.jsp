<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login - Travel Booking</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body class="split-auth-page">
    <div class="split-card">
        <div class="split-left">
            <div class="characters-container">
                <!-- char-1 purple -->
                <div class="character char-1 char-purple">
                    <div class="face">
                        <div class="eyes"><div class="eye"><div class="pupil"></div></div><div class="eye"><div class="pupil"></div></div></div>
                        <div class="mouth"></div>
                    </div>
                </div>
                <!-- char-2 pink -->
                <div class="character char-2 char-pink">
                    <div class="face">
                        <div class="eyes"><div class="eye"><div class="pupil"></div></div><div class="eye"><div class="pupil"></div></div></div>
                        <div class="mouth"></div>
                    </div>
                </div>
                <!-- char-3 orange -->
                <div class="character char-3 char-orange">
                    <div class="face">
                        <div class="eyes"><div class="eye"><div class="pupil"></div></div><div class="eye"><div class="pupil"></div></div></div>
                        <div class="mouth"></div>
                    </div>
                </div>
                <!-- char-4 yellow -->
                <div class="character char-4 char-yellow">
                    <div class="face">
                        <div class="eyes"><div class="eye"><div class="pupil"></div></div><div class="eye"><div class="pupil"></div></div></div>
                        <div class="mouth"></div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="split-right">
            <div class="logo-container">
                <svg viewBox="0 0 24 24"><path d="M21 16v-2l-8-5V3.5c0-.83-.67-1.5-1.5-1.5S10 2.67 10 3.5V9l-8 5v2l8-2.5V19l-2 1.5V22l3.5-1 3.5 1v-1.5L13 19v-5.5l8 2.5z"/></svg>
            </div>
            <div class="auth-header">
                <h2>Welcome back!</h2>
                <p>Please enter your details</p>
            </div>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger" id="login-error"><%= request.getAttribute("error") %></div>
            <% } %>
            <% if (request.getParameter("msg") != null) { %>
                <div class="alert alert-success"><%= request.getParameter("msg") %></div>
            <% } %>

            <form action="${pageContext.request.contextPath}/login" method="POST" id="login-form">
                <div class="form-group <%= request.getAttribute("error") != null ? "has-error" : "" %>">
                    <input type="email" name="email" id="email" placeholder=" " required>
                    <label for="email">Email Address</label>
                </div>
                
                <div class="form-group <%= request.getAttribute("error") != null ? "has-error" : "" %>">
                    <input type="password" name="password" id="password" placeholder=" " required>
                    <label for="password">Password</label>
                    <button type="button" class="password-toggle" id="toggle-password">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>
                    </button>
                </div>
                
                <button type="submit" class="btn btn-primary" id="submit-btn">Log In</button>
            </form>
            
            <div class="auth-footer">
                Don't have an account? <a href="${pageContext.request.contextPath}/register">Sign up</a>
            </div>
        </div>
    </div>
    
    <script>
        const characters = document.querySelectorAll('.character');
        const faces = document.querySelectorAll('.face');
        const eyes = document.querySelectorAll('.eyes');
        const emailInput = document.getElementById('email');
        const passwordInput = document.getElementById('password');
        const togglePassword = document.getElementById('toggle-password');
        const submitBtn = document.getElementById('submit-btn');
        const hasError = document.getElementById('login-error') !== null;
        
        if (hasError) {
            characters.forEach(c => c.classList.add('sad'));
            setTimeout(() => {
                // Ensure shaking class is removed after the animation completes
                // characters.forEach(c => c.classList.remove('sad')); 
                // Wait, if there's an error, let them stay sad but stop shaking. The shake is a single play animation, so keeping class 'sad' just keeps the flipped mouth.
            }, 500); 
        }
        
        document.addEventListener('mousemove', (e) => {
            const x = e.clientX;
            const y = e.clientY;
            
            faces.forEach(face => {
                const rect = face.getBoundingClientRect();
                const faceX = rect.left + rect.width / 2;
                const faceY = rect.top + rect.height / 2;
                
                const angle = Math.atan2(y - faceY, x - faceX);
                const distance = Math.min(3, Math.hypot(x - faceX, y - faceY) / 50);
                
                const pupX = Math.cos(angle) * distance;
                const pupY = Math.sin(angle) * distance;
                
                const pupils = face.querySelectorAll('.pupil');
                pupils.forEach(pupil => {
                    pupil.style.transform = `translate(calc(-50% + ${pupX}px), calc(-50% + ${pupY}px))`;
                });
            });
        });
        
        emailInput.addEventListener('focus', () => {
            faces.forEach(face => face.style.transform = 'translate(10px, 5px)');
            characters.forEach(c => c.classList.remove('sad'));
        });
        emailInput.addEventListener('blur', () => {
            faces.forEach(face => face.style.transform = 'translate(0, 0)');
        });
        
        passwordInput.addEventListener('focus', () => {
            faces.forEach(face => face.style.transform = 'translate(-10px, -5px)');
            characters.forEach(c => c.classList.remove('sad'));
        });
        passwordInput.addEventListener('blur', () => {
            faces.forEach(face => face.style.transform = 'translate(0, 0)');
        });
        
        let isPasswordVisible = false;
        togglePassword.addEventListener('click', () => {
            isPasswordVisible = !isPasswordVisible;
            passwordInput.type = isPasswordVisible ? 'text' : 'password';
            
            if (isPasswordVisible) {
                eyes.forEach(eye => eye.classList.add('eyes-closed'));
                togglePassword.innerHTML = '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line></svg>';
            } else {
                eyes.forEach(eye => eye.classList.remove('eyes-closed'));
                togglePassword.innerHTML = '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>';
            }
        });
        
        submitBtn.addEventListener('mouseenter', () => {
            characters.forEach(c => c.classList.add('smile'));
        });
        submitBtn.addEventListener('mouseleave', () => {
            characters.forEach(c => c.classList.remove('smile'));
        });
    </script>
</body>
</html>
