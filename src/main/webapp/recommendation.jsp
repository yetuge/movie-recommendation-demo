<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movie.entity.MovieInformation" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    request.setAttribute("pageTitle", "猜你喜欢");
    request.setAttribute("pageName", "recommend");
%>
<%@ include file="header.jsp" %>

    <!-- Hero Section -->
    <section class="hero-section" style="padding: 32px;">
        <div class="welcome-icon">
            <i class="fas fa-magic animated-icon"></i>
        </div>
        <h2 class="mb-3">为你精选的影片</h2>
        <p class="lead mb-0" style="color: var(--color-text-secondary);">
            基于你的观影喜好，智能推荐最匹配的电影
        </p>
    </section>

    <!-- Movie Grid -->
    <section>
        <div class="text-center mb-4">
            <h2 class="section-title">推荐电影</h2>
        </div>

        <div class="movie-grid">
        <%
            List<MovieInformation> recommendations = (List<MovieInformation>) request.getAttribute("recommendations");
            if (recommendations != null && !recommendations.isEmpty()) {
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                for (MovieInformation movie : recommendations) {
                    String imageUrl = movie.getImageUrl() != null ? movie.getImageUrl() : "default.jpg";
        %>
            <div class="movie-card">
                <div class="movie-poster-wrapper">
                    <img loading="lazy"
                         src="${pageContext.request.contextPath}/images/<%= imageUrl %>"
                         onerror="this.src='${pageContext.request.contextPath}/images/default.jpg'"
                         alt="<%= movie.getMovieName() %>"
                         class="movie-poster">
                </div>
                <div class="movie-info">
                    <h5 class="movie-title" title="<%= movie.getMovieName() %>">
                        <%= movie.getMovieName() %>
                    </h5>
                    <div class="movie-meta">
                        <span class="movie-genre">
                            <%= movie.getGenre() %>
                        </span>
                        <span class="movie-year">
                            <%= yearFormat.format(movie.getReleaseTime()) %>
                        </span>
                    </div>
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="rating-badge">
                            ★ <%= String.format("%.1f", movie.getScore()) %>
                        </span>
                        <a href="movieDetail?movieId=<%= movie.getMovieId() %>"
                           class="btn btn-sm btn-outline-gold">
                            <i class="fas fa-eye"></i>详情
                        </a>
                    </div>
                </div>
            </div>
        <%
                }
            } else {
        %>
        <!-- Empty State -->
        <div class="empty-state" style="grid-column: 1 / -1;">
            <i class="fas fa-heart"></i>
            <h2>暂无推荐</h2>
            <p class="text-muted mb-4">快去收藏几部电影吧！</p>
            <p class="text-muted mb-4" style="font-size: 0.9rem;">
                收藏喜欢的电影，系统会根据你的喜好推荐更多精彩影片
            </p>
            <a href="movieList" class="btn btn-secondary">
                <i class="fas fa-th-large"></i>浏览全部电影
            </a>
        </div>
        <%
            }
        %>
        </div>
    </section>

<jsp:include page="footer.jsp"/>
