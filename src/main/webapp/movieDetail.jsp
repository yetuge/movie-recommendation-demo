<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movie.entity.MovieInformation" %>
<%@ page import="com.movie.entity.MovieComment" %>
<%@ page import="com.movie.entity.MovieScore" %>
<%@ page import="com.movie.entity.OrdinaryUser" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("pageTitle", "电影详情");
    request.setAttribute("pageName", "movies");
%>
<%@ include file="header.jsp" %>

<%
    MovieInformation movie = (MovieInformation) request.getAttribute("movie");
    List<MovieComment> commentList = (List<MovieComment>) request.getAttribute("commentList");
    Double avgScore = (Double) request.getAttribute("avgScore");
    Integer movieId = (Integer) request.getAttribute("movieId");
    Boolean isCollected = (Boolean) request.getAttribute("isCollected");
    MovieScore userScore = (MovieScore) request.getAttribute("userScore");
    OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    if (avgScore == null) avgScore = 0.0;
    if (isCollected == null) isCollected = false;
    String imageUrl = movie != null && movie.getImageUrl() != null ? movie.getImageUrl() : "default.jpg";
%>

    <!-- Error Message -->
    <c:if test="${not empty errorMsg}">
        <div class="alert alert-danger" role="alert" aria-live="assertive">
            <i class="fas fa-exclamation-circle me-2"></i><c:out value="${errorMsg}" />
        </div>
    </c:if>

    <!-- Success Message -->
    <c:if test="${not empty successMsg}">
        <div class="alert alert-success" role="alert" aria-live="assertive">
            <i class="fas fa-check-circle me-2"></i><c:out value="${successMsg}" />
        </div>
    </c:if>

    <c:if test="${movie != null}">
        <!-- Movie Header -->
        <div class="row mb-5">
            <!-- Poster -->
            <div class="col-lg-4 col-md-5 mb-4 mb-md-0">
                <div class="movie-poster-container">
                    <img loading="lazy"
                         src="${pageContext.request.contextPath}/images/<%= imageUrl %>"
                         alt="${movie.name}"
                         class="movie-detail-poster"
                         onerror="this.src='${pageContext.request.contextPath}/images/default.jpg'">
                </div>
            </div>

            <!-- Movie Info -->
            <div class="col-lg-8 col-md-7">
                <div class="movie-info-container">
                    <h1 class="mb-3"><%= movie.getName() %></h1>

                    <div class="info-grid">
                        <div class="info-item">
                            <span class="info-label">导演：</span>
                            <span class="info-value"><%= movie.getDirector() %></span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">演员：</span>
                            <span class="info-value"><%= movie.getActors() %></span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">类型：</span>
                            <span class="info-value"><%= movie.getGenre() %></span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">上映时间：</span>
                            <span class="info-value"><%= dateFormat.format(movie.getReleaseTime()) %></span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">时长：</span>
                            <span class="info-value"><%= movie.getDuration() %> 分钟</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">国家：</span>
                            <span class="info-value"><%= movie.getCountry() %></span>
                        </div>
                    </div>

                    <!-- Score Display -->
'                    <div class="score-display">
                        <div class="score-box">
                            <div class="score-value">★ <%= String.format("%.1f", avgScore) %></div>
                            <div class="score-label">平均评分</div>
                        </div>
                    </div>

                    <!-- Action Buttons -->
                    <div class="action-buttons">
                        <%
                            if (user != null) {
                        %>
                            <!-- Collect Button -->
                            <form action="api/toggleCollect" method="post" class="d-inline">
                                <input type="hidden" name="movieId" value="<%= movieId %>">
                                <button type="submit"
                                        class="btn <%= isCollected ? "btn-danger" : "btn-primary" %>">
                                    <i class="fas fa-<%= isCollected ? "heart-broken" : "heart" %>"></i>
                                    <%= isCollected ? "取消收藏" : "收藏电影" %>
                                </button>
                            </form>
                        <%
                            } else {
                        %>
                            <a href="signin.jsp" class="btn btn-primary">
                                <i class="fas fa-heart"></i>收藏电影
                            </a>
                        <%
                            }
                        %>

                        <!-- Scroll to Comments -->
                        <a href="#comment-section" class="btn btn-secondary">
                            <i class="fas fa-comments"></i>查看评论
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Plot Section -->
        <div class="mb-5">
            <div class="section-card">
                <h3 class="section-title mb-3">
                    <i class="fas fa-file-alt me-2"></i>电影简介
                </h3>
                <div class="plot-content">
                    <p><%= movie.getContent() %></p>
                </div>
            </div>
        </div>

        <!-- Score Section -->
        <div class="mb-5">
            <div class="section-card">
                <h3 class="section-title mb-3">
                    <i class="fas fa-star me-2"></i>评分电影
                </h3>
                <%
                    if (user != null) {
                %>
                    <%
                        if (userScore != null) {
                    %>
                        <!-- Already Scored -->
                        <div class="alert alert-info">
                            <i class="fas fa-info-circle me-2"></i>您已评分：<strong><%= String.format("%.1f", userScore.getScore()) %></strong> 分
                        </div>
                    <%
                        }
                    %>
                    <form action="addScore" method="post">
                        <input type="hidden" name="movieId" value="<%= movieId %>">
                        <div class="row align-items-end">
                            <div class="col-md-4 mb-3 mb-md-0">
                                <label for="score" class="form-label">评分（1-10分）</label>
                                <select class="form-select" id="score" name="score" required>
                                    <option value="" disabled selected>请选择评分</option>
                                    <%
                                        for (int i = 1; i <= 10; i++) {
                                            String selected = (userScore != null && userScore.getScore() == i) ? " selected" : "";
                                    %>
                                        <option value="<%= i %>"<%= selected %>><%= i %> 分</option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div class="col-md-auto">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-paper-plane"></i><%= userScore != null ? "修改评分" : "提交评分" %>
                                </button>
                            </div>
                        </div>
                    </form>
                <%
                    } else {
                %>
                    <div class="alert alert-light">
                        <i class="fas fa-lock me-2"></i>请 <a href="signin.jsp" class="link">登录</a> 后评分
                    </div>
                <%
                    }
                %>
            </div>
        </div>

        <!-- Comment Section -->
        <div id="comment-section" class="mb-5">
            <div class="section-card">
                <h3 class="section-title mb-3">
                    <i class="fas fa-comments me-2"></i>用户评论
                </h3>

                <%
                    if (user != null) {
                %>
                    <!-- Comment Form -->
                    <div class="comment-form mb-4">
                        <form action="addComment" method="post">
                            <input type="hidden" name="movieId" value="<%= movieId %>">
                            <div class="mb-3">
                                <label for="content" class="form-label">发表你的评论</label>
                                <textarea class="form-control" id="content" name="content" rows="4"
                                          placeholder="分享你对这部电影的看法..." required></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-paper-plane"></i>发表评论
                            </button>
                        </form>
                    </div>
                <%
                    } else {
                %>
                    <div class="alert alert-light mb-4">
                        <i class="fas fa-lock me-2"></i>请 <a href="signin.jsp" class="link">登录</a> 后发表评论
                    </div>
                <%
                    }
                %>

                <!-- Comment List -->
                <div class="comment-list">
                    <%
                        if (commentList != null && !commentList.isEmpty()) {
                            for (MovieComment comment : commentList) {
                    %>
                        <div class="comment-item">
                            <div class="comment-header">
                                <div class="comment-user">
                                    <i class="fas fa-user-circle me-2"></i>
                                    <%= comment.getUserName() != null ? comment.getUserName() : "未知用户" %>
                                </div>
                                <div class="comment-time">
                                    <i class="fas fa-clock me-1"></i><%= timeFormat.format(comment.getPublishTime()) %>
                                </div>
                            </div>
                            <div class="comment-body">
                                <%= comment.getContent() %>
                            </div>
                        </div>
                    <%
                            }
                        } else {
                    %>
                        <div class="text-center text-muted py-4">
                            <i class="fas fa-comment-slash fa-3x mb-3" style="color: var(--color-accent-gold);"></i>
                            <p>暂无评论，快来抢沙发吧！</p>
                        </div>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${movie == null}">
        <!-- Movie Not Found -->
        <div class="alert alert-warning" role="alert" aria-live="assertive">
            <i class="fas fa-exclamation-triangle me-2"></i>电影不存在或已被删除
        </div>
        <div class="text-center mt-4">
            <a href="movieList" class="btn btn-primary">
                <i class="fas fa-arrow-left me-2"></i>返回电影列表
            </a>
        </div>
    </c:if>

<%@ include file="footer.jsp" %>
