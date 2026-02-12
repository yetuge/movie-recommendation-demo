<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注册 - CINEMA</title>

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
    <div class="auth-container wide">
        <!-- Logo Icon -->
        <div class="text-center mb-4">
            <span class="logo-icon" style="width: 60px; height: 60px; font-size: 1.8rem;">
                <i class="fas fa-film"></i>
            </span>
        </div>

        <!-- Title -->
        <h1 class="auth-title">创建账号</h1>
        <p class="auth-subtitle">加入我们，开启电影之旅</p>

        <!-- Error Message -->
        <% String errorMsg = (String) request.getAttribute("errorMsg");
           if (errorMsg != null) { %>
            <div class="alert alert-danger" role="alert" aria-live="assertive">
                <i class="fas fa-exclamation-triangle me-2"></i>
                <%= errorMsg %>
            </div>
        <% } %>

        <!-- Register Form -->
        <form action="register" method="post">
            <!-- Username -->
            <div class="form-floating">
                <input type="text" class="form-control" id="userName" name="userName"
                       placeholder="请输入用户名" required>
                <label for="userName">用户名 *</label>
            </div>

            <!-- Phone & Email Row -->
            <div class="row g-3">
                <div class="col-md-6">
                    <div class="form-floating">
                        <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber"
                               placeholder="请输入手机号">
                        <label for="phoneNumber">手机号</label>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-floating">
                        <input type="email" class="form-control" id="userMailbox" name="userMailbox"
                               placeholder="请输入邮箱" required>
                        <label for="userMailbox">邮箱 *</label>
                    </div>
                </div>
            </div>

            <!-- Password -->
            <div class="form-floating mt-3">
                <input type="password" class="form-control" id="userPassword" name="userPassword"
                       placeholder="请输入密码（至少6位）" required minlength="6">
                <label for="userPassword">密码 *</label>
            </div>

            <!-- Gender & Birthday Row -->
            <div class="row g-3 mt-3">
                <div class="col-md-6">
                    <label for="gender" class="form-label text-muted mb-2">性别</label>
                    <select class="form-select" id="gender" name="gender">
                        <option value="">请选择</option>
                        <option value="男">男</option>
                        <option value="女">女</option>
                        <option value="保密">保密</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label for="birthday" class="form-label text-muted mb-2">生日</label>
                    <input type="date" class="form-control" id="birthday" name="birthday">
                </div>
            </div>

            <!-- Submit Button -->
            <button type="submit" class="btn btn-gold rounded-pill fw-bold py-3 mt-4">
                <i class="fas fa-user-plus me-2"></i>注册
            </button>
        </form>

        <!-- Bottom Links -->
        <div class="auth-links">
            <p>已有账号？<a href="signin.jsp">立即登录</a></p>
            <p><a href="index.jsp" class="text-muted"><i class="fas fa-arrow-left me-1"></i>返回首页</a></p>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
