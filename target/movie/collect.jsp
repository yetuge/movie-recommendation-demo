<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movie.entity.MovieInformation" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    request.setAttribute("pageTitle", "我的收藏");
    request.setAttribute("pageName", "collect");
%>
<%@ include file="header.jsp" %>

    <!-- Hero Section -->
    <section class="hero-section" style="padding: 32px;">
        <div class="welcome-icon">
            <i class="fas fa-heart animated-icon"></i>
        </div>
        <h2 class="mb-3">我的收藏</h2>
        <p class="lead mb-0" style="color: var(--color-text-secondary);">
            这里是你收藏的所有精彩电影
        </p>
    </section>

    <!-- Movie Grid -->
    <section>
        <div class="text-center mb-4">
            <h2 class="section-title">收藏列表</h2>
        </div>

        <div class="movie-grid">
        <%
            List<MovieInformation> movies = (List<MovieInformation>) request.getAttribute("myCollections");
            if (movies != null && !movies.isEmpty()) {
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                for (MovieInformation movie : movies) {
                    String encodedMovieName = java.net.URLEncoder.encode(movie.getMovieName(), "UTF-8");
        %>
            <div class="movie-card">
                <div class="movie-poster-wrapper">
                    <img loading="lazy"
                         src="${pageContext.request.contextPath}/images/movie_<%= movie.getMovieId() %>.jpg"
                         onerror="this.src='https://via.placeholder.com/300x450/1a1a25/d4af37?text=<%= encodedMovieName %>'"
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
            <i class="fas fa-heart-broken"></i>
            <h2>你还没有收藏任何电影</h2>
            <p class="text-muted mb-4">去电影列表发现更多精彩影片吧</p>
            <a href="movieList" class="btn btn-primary">
                <i class="fas fa-th-large"></i>浏览电影
            </a>
        </div>
        <%
            }
        %>
        </div>
    </section>

<jsp:include page="footer.jsp"/>
