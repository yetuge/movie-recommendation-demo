<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movie.entity.MovieInformation" %>
<%@ page import="com.movie.entity.OrdinaryUser" %>
<%@ page import="com.movie.dao.MovieDao" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    request.setAttribute("pageTitle", "首页");
    request.setAttribute("pageName", "home");

    // 获取热门电影数据
    MovieDao movieDao = new MovieDao();
    List<MovieInformation> hotMovies = movieDao.findTopRatedMovies(4);
    request.setAttribute("hotMovies", hotMovies);
%>
<%@ include file="header.jsp" %>

    <!-- Hero Section -->
    <section class="hero-section">
        <div class="welcome-icon">
            <i class="fas fa-film animated-icon"></i>
        </div>
        <h1 class="mb-3">欢迎来到 CINEMA</h1>
        <p class="lead mb-4" style="color: var(--color-text-secondary);">
            发现你的下一部最爱电影，探索海量精彩影片
        </p>

        <!-- Features -->
        <div class="row justify-content-center mt-4 mb-4">
            <div class="col-md-4 col-6 mb-3">
                <div class="mb-2" style="font-size: 1.5rem;">
                    <i class="fas fa-search text-gold"></i>
                </div>
                <h5 class="mb-2">智能搜索</h5>
                <p class="text-muted small">快速找到你想看的电影</p>
            </div>
            <div class="col-md-4 col-6 mb-3">
                <div class="mb-2" style="font-size: 1.5rem;">
                    <i class="fas fa-magic text-gold"></i>
                </div>
                <h5 class="mb-2">个性推荐</h5>
                <p class="text-muted small">基于喜好推荐影片</p>
            </div>
            <div class="col-md-4 col-6 mb-3">
                <div class="mb-2" style="font-size: 1.5rem;">
                    <i class="fas fa-star text-gold"></i>
                </div>
                <h5 class="mb-2">评分评论</h5>
                <p class="text-muted small">分享你的观影感受</p>
            </div>
        </div>

        <!-- CTA Buttons -->
        <%
            OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");
            if (user == null) {
        %>
            <div class="mt-4">
                <a class="btn btn-primary btn-lg me-3" href="signup.jsp">
                    <i class="fas fa-user-plus"></i>立即注册
                </a>
                <a class="btn btn-secondary btn-lg" href="movieList">
                    <i class="fas fa-th-large"></i>浏览电影
                </a>
            </div>
        <% } %>
    </section>

    <!-- Featured Movies Section -->
    <section>
        <div class="text-center mb-4">
            <h2 class="section-title">热门电影</h2>
        </div>

        <div class="movie-grid">
        <%
            if (hotMovies != null && !hotMovies.isEmpty()) {
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                int count = 0;
                for (MovieInformation movie : hotMovies) {
                    if (count >= 4) break;
                    count++;
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
            <!-- Static demo movies when no data -->
            <div class="movie-card">
                <div class="movie-poster-wrapper">
                    <img src="https://via.placeholder.com/300x450/1a1a25/d4af37?text=Interstellar"
                         alt="星际穿越" class="movie-poster" loading="lazy">
                </div>
                <div class="movie-info">
                    <h5 class="movie-title">星际穿越</h5>
                    <div class="movie-meta">
                        <span class="movie-genre">科幻</span>
                        <span class="movie-year">2014</span>
                    </div>
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="rating-badge">★ 9.3</span>
                        <a href="movieList" class="btn btn-sm btn-outline-gold">
                            <i class="fas fa-eye"></i>浏览
                        </a>
                    </div>
                </div>
            </div>

            <div class="movie-card">
                <div class="movie-poster-wrapper">
                    <img src="https://via.placeholder.com/300x450/1a1a25/d4af37?text=The+Shawshank"
                         alt="肖申克的救赎" class="movie-poster" loading="lazy">
                </div>
                <div class="movie-info">
                    <h5 class="movie-title">肖申克的救赎</h5>
                    <div class="movie-meta">
                        <span class="movie-genre">剧情</span>
                        <span class="movie-year">1994</span>
                    </div>
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="rating-badge">★ 9.7</span>
                        <a href="movieList" class="btn btn-sm btn-outline-gold">
                            <i class="fas fa-eye"></i>浏览
                        </a>
                    </div>
                </div>
            </div>

            <div class="movie-card">
                <div class="movie-poster-wrapper">
                    <img src="https://via.placeholder.com/300x450/1a1a25/d4af37?text=Inception"
                         alt="盗梦空间" class="movie-poster" loading="lazy">
                </div>
                <div class="movie-info">
                    <h5 class="movie-title">盗梦空间</h5>
                    <div class="movie-meta">
                        <span class="movie-genre">科幻</span>
                        <span class="movie-year">2010</span>
                    </div>
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="rating-badge">★ 9.0</span>
                        <a href="movieList" class="btn btn-sm btn-outline-gold">
                            <i class="fas fa-eye"></i>浏览
                        </a>
                    </div>
                </div>
            </div>

            <div class="movie-card">
                <div class="movie-poster-wrapper">
                    <img src="https://via.placeholder.com/300x450/1a1a25/d4af37?text=Forrest+Gump"
                         alt="阿甘正传" class="movie-poster" loading="lazy">
                </div>
                <div class="movie-info">
                    <h5 class="movie-title">阿甘正传</h5>
                    <div class="movie-meta">
                        <span class="movie-genre">剧情</span>
                        <span class="movie-year">1994</span>
                    </div>
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="rating-badge">★ 9.5</span>
                        <a href="movieList" class="btn btn-sm btn-outline-gold">
                            <i class="fas fa-eye"></i>浏览
                        </a>
                    </div>
                </div>
            </div>
        <%
            }
        %>
        </div>

        <!-- View All Button -->
        <div class="text-center mt-4">
            <a href="movieList" class="btn btn-secondary btn-lg">
                <i class="fas fa-th-large"></i>查看全部电影
            </a>
        </div>
    </section>

<jsp:include page="footer.jsp"/>
