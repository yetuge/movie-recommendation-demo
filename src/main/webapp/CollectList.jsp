<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movie.entity.MovieInformation" %>
<%@ page import="com.movie.entity.OrdinaryUser" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    request.setAttribute("pageTitle", "我的收藏");
    request.setAttribute("pageName", "collect");
%>
<%@ include file="header.jsp" %>

    <!-- 收藏标题区域 -->
    <div class="jumbotron">
        <div class="welcome-icon">
            <i class="fas fa-heart animated-icon" style="color: #ffd700;"></i>
        </div>
        <h2 class="fw-bold mb-3">我的电影收藏</h2>
        <p class="lead mb-0">记录你喜爱的电影，随时回顾精彩时刻</p>
    </div>

    <!-- 电影网格 -->
    <div class="row mt-4">
        <div class="col-12 text-center mb-4">
            <h2 class="section-title">收藏列表</h2>
        </div>

        <%
            List<MovieInformation> collectList = (List<MovieInformation>) request.getAttribute("collectList");
            if (collectList != null && !collectList.isEmpty()) {
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                for (MovieInformation movie : collectList) {
                    String imageUrl = movie.getImageUrl() != null ? movie.getImageUrl() : "default.jpg";
        %>
            <div class="col-md-3 col-sm-6 mb-4">
                <div class="movie-card">
                    <img loading="lazy" src="${pageContext.request.contextPath}/images/<%= imageUrl %>"
                         alt="<%= movie.getMovieName() %>"
                         class="movie-poster"
                         onerror="this.src='${pageContext.request.contextPath}/images/default.jpg'">
                    <div class="card-body-custom">
                        <h5 class="movie-title" title="<%= movie.getMovieName() %>">
                            <%= movie.getMovieName() %>
                        </h5>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="text-muted small">
                                <%= movie.getGenre() %> · <%= yearFormat.format(movie.getReleaseTime()) %>
                            </span>
                            <span class="rating-badge">★ <%= String.format("%.1f", movie.getScore()) %></span>
                        </div>
                        <div class="d-flex gap-2">
                            <a href="movieDetail?movieId=<%= movie.getMovieId() %>"
                               class="btn btn-gold flex-grow-1 btn-sm">
                                <i class="fas fa-eye me-1"></i>详情
                            </a>
                            <a href="collect?action=remove&movieId=<%= movie.getMovieId() %>"
                               class="btn btn-outline-danger flex-grow-1 btn-sm confirm-delete"
                               data-confirm-message="确定取消收藏吗？">
                                <i class="fas fa-trash-alt me-1"></i>取消
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        <%
                }
            } else {
        %>
            <div class="col-12 text-center">
                <div class="alert alert-warning">
                    <i class="fas fa-folder-open me-2"></i>
                    <strong>暂无收藏</strong>
                </div>
                <p class="text-muted mb-4">您还没有收藏任何电影</p>
                <a href="movieList" class="btn btn-outline-gold btn-lg">
                    <i class="fas fa-film me-2"></i>去浏览电影
                </a>
            </div>
        <%
            }
        %>
    </div>

    <script>
        // Handle delete confirmation
        document.addEventListener('DOMContentLoaded', function() {
            document.querySelectorAll('.confirm-delete').forEach(function(link) {
                link.addEventListener('click', function(e) {
                    if (!confirm(this.dataset.confirmMessage || '确定要执行此操作吗？')) {
                        e.preventDefault();
                        return false;
                    }
                });
            });
        });
    </script>

<%@ include file="footer.jsp" %>
