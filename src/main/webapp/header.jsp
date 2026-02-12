<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle == null ? 'CINEMA - 电影推荐系统' : pageTitle}</title>
    <meta name="description" content="发现你的下一部最爱电影，探索海量精彩影片">

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@400;500;600;700&family=Lato:wght@300;400;700&display=swap" rel="stylesheet">

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cinema-theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/custom.css">
</head>
<body>
    <!-- Background Gradient -->
    <div class="cinema-bg"></div>

    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark" style="background-color: #000000; padding: 15px 0;">
        <div class="container">
            <a class="navbar-brand fw-bold text-warning fs-3" href="index.jsp" style="font-family: serif; letter-spacing: 2px;">CINEMA</a>

            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#movieNavbar" aria-controls="movieNavbar" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="movieNavbar">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link ${pageName == 'home' ? 'active text-light' : 'text-white-50'}" href="index.jsp">首页</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${pageName == 'movies' ? 'active text-light' : 'text-white-50'}" href="movieList">电影库</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${pageName == 'recommend' ? 'active text-light' : 'text-white-50'}" href="recommend">猜你喜欢</a>
                    </li>
                </ul>

                <div class="d-flex align-items-center">
                    <form class="d-flex me-4" action="search" method="get">
                        <div class="input-group">
                            <input class="form-control bg-dark text-light border-secondary" type="search" name="keyword" placeholder="搜索电影..." aria-label="Search">
                            <button class="btn btn-outline-secondary text-warning" type="submit">
                                <i class="fas fa-search"></i>
                            </button>
                        </div>
                    </form>

                    <ul class="navbar-nav">
                        <c:choose>
                            <c:when test="${not empty sessionScope.currentUser}">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle text-warning d-flex align-items-center" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                        <i class="fas fa-user-circle fa-lg me-2"></i>
                                        <span><c:out value="${sessionScope.currentUser.userName}" /></span>
                                    </a>
                                    <ul class="dropdown-menu dropdown-menu-dark dropdown-menu-end" aria-labelledby="navbarDropdown">
                                        <li>
                                            <a class="dropdown-item" href="collect">
                                                <i class="fas fa-heart text-danger me-2"></i>我的收藏
                                            </a>
                                        </li>
                                        <li><hr class="dropdown-divider"></li>
                                        <li>
                                            <a class="dropdown-item text-danger" href="logout">
                                                <i class="fas fa-sign-out-alt me-2"></i>退出登录
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="nav-item me-2">
                                    <a class="btn btn-outline-light btn-sm px-3" href="signin.jsp">登录</a>
                                </li>
                                <li class="nav-item">
                                    <a class="btn btn-warning btn-sm px-3 fw-bold" href="signup.jsp" style="color: #000;">注册</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
            </div>
        </div>
    </nav>

    <!-- Page Content -->
    <div class="container py-5">
