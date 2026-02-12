<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - CINEMA</title>

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;500;600;700&family=Lato:wght@300;400;700&display=swap" rel="stylesheet">

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/custom.css">
</head>
<body class="auth-page">
    <div class="auth-container">
        <!-- Logo Icon -->
        <div class="text-center mb-4">
            <span class="logo-icon" style="width: 60px; height: 60px; font-size: 1.8rem;">
                <i class="fas fa-film"></i>
            </span>
        </div>

        <!-- Title -->
        <h1 class="auth-title">欢迎回来</h1>
        <p class="auth-subtitle">登录您的账号，继续观影之旅</p>

        <!-- Error Message -->
        <% String errorMsg = (String) request.getAttribute("errorMsg");
           if (errorMsg != null) { %>
            <div class="alert alert-danger" role="alert" aria-live="assertive">
                <i class="fas fa-exclamation-triangle me-2"></i>
                <%= errorMsg %>
            </div>
        <% } %>

        <!-- Login Form -->
        <form action="login" method="post">
            <!-- Account Input -->
            <div class="form-floating">
                <input type="text" class="form-control" id="account" name="account"
                       placeholder="请输入邮箱或手机号" required>
                <label for="account">账号</label>
            </div>

            <!-- Password Input -->
            <div class="form-floating">
                <input type="password" class="form-control" id="password" name="password"
                       placeholder="请输入密码" required>
                <label for="password">密码</label>
            </div>

            <!-- Submit Button -->
            <button type="submit" class="btn btn-gold rounded-pill fw-bold py-3">
                <i class="fas fa-sign-in-alt me-2"></i>登录
            </button>
        </form>

        <!-- Bottom Links -->
        <div class="auth-links">
            <p>还没有账号？<a href="signup.jsp">立即注册</a></p>
            <p><a href="index.jsp" class="text-muted"><i class="fas fa-arrow-left me-1"></i>返回首页</a></p>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
