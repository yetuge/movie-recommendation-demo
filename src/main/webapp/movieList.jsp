<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movie.entity.MovieInformation" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("pageTitle", "电影列表");
    request.setAttribute("pageName", "movies");
%>
<%@ include file="header.jsp" %>

    <!-- Page Title Section -->
    <section class="hero-section" style="padding: 32px;">
        <div class="welcome-icon">
            <i class="fas fa-th-large animated-icon"></i>
        </div>
        <%
            String searchKeyword = (String) request.getAttribute("searchKeyword");
            if (searchKeyword != null && !searchKeyword.isEmpty()) {
        %>
            <h2 class="mb-3">搜索结果</h2>
            <p class="lead mb-0" style="color: var(--color-text-secondary);">
                关键词：<strong class="text-gold"><c:out value="${searchKeyword}" /></strong>
            </p>
        <%
            } else {
        %>
            <h2 class="mb-3">探索海量电影</h2>
            <p class="lead mb-0" style="color: var(--color-text-secondary);">
                发现来自世界各地的精彩影片
            </p>
        <%
            }
        %>
    </section>

    <!-- Movie Grid -->
    <section>
        <div class="text-center mb-4">
            <h2 class="section-title"><%= searchKeyword != null && !searchKeyword.isEmpty() ? "搜索结果" : "全部电影" %></h2>
        </div>

        <div class="movie-grid">
        <%
            List<MovieInformation> movies = (List<MovieInformation>) request.getAttribute("movies");
            if (movies != null && !movies.isEmpty()) {
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                for (MovieInformation movie : movies) {
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
            <i class="fas fa-film"></i>
            <h2>暂无电影</h2>
            <p class="text-muted mb-4">数据库中暂时没有电影数据</p>
            <a href="="index.jsp" class="btn btn-secondary">
                <i class="fas fa-home"></i>返回首页
            </a>
        </div>
        <%
            }
        %>
        </div>
    </section>

<jsp:include page="footer.jsp"/>
