<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movie.entity.MovieInformation" %>
<%@ page import="com.movie.entity.MovieComment" %>
<%@ page import="com.movie.entity.OrdinaryUser" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    MovieInformation movie = (MovieInformation) request.getAttribute("movie");
    String title = "电影详情";
    if (movie != null) {
        title = movie.getMovieName() + " - 电影详情";
    }
    request.setAttribute("pageTitle", title);
%>
<%@ include file="header.jsp" %>

    <% String errorMsg = (String) request.getAttribute("errorMsg");
       if (errorMsg != null) { %>
        <div class="alert alert-danger mb-4">
            <i class="fas fa-exclamation-circle me-2"></i>
            <%= errorMsg %>
        </div>
    <% }

       if (movie != null) { %>

        <!-- 电影信息卡片 -->
        <div class="card mb-4" style="background: rgba(30, 30, 50, 0.8); border: 1px solid rgba(255, 215, 0, 0.2); border-radius: 15px;">
            <div class="row g-0">
                <div class="col-md-4">
                    <img loading="lazy" src="${pageContext.request.contextPath}/images/movie_<%= movie.getMovieId() %>.jpg"
                         alt="<%= movie.getMovieName() %> 电影海报"
                         class="img-fluid rounded-start"
                         style="min-height: 400px; object-fit: cover;"
                         onerror="this.src='https://via.placeholder.com/300x400/2a2a4a/ffd700?text=Movie'">
                </div>
                <div class="col-md-8">
                    <div class="card-body">
                        <h2 class="card-title fw-bold mb-3" style="color: #ffd700;">
                            <c:out value="${movie.movieName}" />
                        </h2>

                        <!-- 评分 -->
                        <div class="d-flex align-items-center mb-3">
                            <%
                                double score = movie.getScore();
                                int stars = (int) Math.floor(score / 2);
                                for (int i = 0; i < 5; i++) {
                                    if (i < stars) {
                            %>
                                <i class="fas fa-star me-1" style="color: #ffd700; font-size: 1.3rem;"></i>
                            <%  } else { %>
                                <i class="far fa-star me-1" style="color: #666; font-size: 1.3rem;"></i>
                            <%  }
                                }
                            %>
                            <span class="ms-2 fw-bold" style="color: #ffd700; font-size: 1.5rem;">
                                <%= String.format("%.1f", score) %>
                            </span>
                            <span class="text-muted">/ 10</span>
                        </div>

                        <!-- 元信息 -->
                        <div class="row g-3 mb-3">
                            <div class="col-6">
                                <span class="text-muted small">类型：</span>
                                <span class="text-warning small ms-1"><%= movie.getGenre() %></span>
                            </div>
                            <div class="col-6">
                                <span class="text-muted small">时长：</span>
                                <span class="ms-1"><%= movie.getDuration() %> 分钟</span>
                            </div>
                            <div class="col-6">
                                <span class="text-muted small">导演：</span>
                                <span class="ms-1"><%= movie.getDirector() %></span>
                            </div>
                            <div class="col-6">
                                <span class="text-muted small">国家：</span>
                                <span class="ms-1"><%= movie.getCountry() %></span>
                            </div>
                            <div class="col-12">
                                <span class="text-muted small">主演：</span>
                                <span class="ms-1"><%= movie.getMainActors() %></span>
                            </div>
                            <div class="col-6">
                                <span class="text-muted small">上映日期：</span>
                                <span class="ms-1"><%= new SimpleDateFormat("yyyy-MM-dd").format(movie.getReleaseTime()) %></span>
                            </div>
                        </div>

                        <!-- 剧情简介 -->
                        <div class="mt-4 pt-3 border-top" style="border-color: rgba(255, 215, 0, 0.2) !important;">
                            <h5 class="mb-2" style="color: #ffd700;">剧情简介</h5>
                            <p class="text-secondary" style="line-height: 1.8;">
                                <%= movie.getPlot() %>
                            </p>
                        </div>

                        <!-- 操作按钮 -->
                        <% OrdinaryUser currentUser = (OrdinaryUser) session.getAttribute("currentUser");
                           if (currentUser != null) { %>
                            <div class="mt-4">
                                <% Boolean isCollected = (Boolean) request.getAttribute("isCollected");
                                   if (isCollected != null && isCollected) { %>
                                    <a href="collect?action=remove&movieId=<%= movie.getMovieId() %>"
                                       class="btn btn-outline-danger me-2">
                                        <i class="fas fa-heart-broken me-1"></i>取消收藏
                                    </a>
                                <% } else { %>
                                    <a href="collect?action=add&movieId=<%= movie.getMovieId() %>"
                                       class="btn btn-gold me-2">
                                        <i class="fas fa-heart me-1"></i>收藏电影
                                    </a>
                                <% } %>
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>

        <% if (currentUser != null) { %>
            <!-- 评分区域 -->
            <div class="card mb-4" style="background: rgba(30, 30, 50, 0.8); border: 1px solid rgba(255, 215, 0, 0.2); border-radius: 15px;">
                <div class="card-body">
                    <h5 class="card-title mb-3" style="color: #ffd700;">
                        <i class="fas fa-star-half-alt me-2"></i>为这部电影评分
                    </h5>
                    <form action="addScore" method="post">
                        <input type="hidden" name="movieId" value="<%= movie.getMovieId() %>">
                        <div class="row align-items-center">
                            <div class="col-md-4">
                                <label class="form-label text-muted">评分（0-10分）</label>
                            </div>
                            <div class="col-md-6">
                                <input type="number" id="score" name="score"
                                       class="form-control"
                                       min="0" max="10" step="0.5" required>
                            </div>
                            <div class="col-md-2">
                                <button type="submit" class="btn btn-gold w-100">提交</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <!-- 评论区域 -->
            <div class="card mb-4" style="background: rgba(30, 30, 50, 0.8); border: 1px solid rgba(255, 215, 0, 0.2); border-radius: 15px;">
                <div class="card-body">
                    <h5 class="card-title mb-3" style="color: #ffd700;">
                        <i class="fas fa-comment-alt me-2"></i>发表评论
                    </h5>
                    <form action="addComment" method="post">
                        <input type="hidden" name="movieId" value="<%= movie.getMovieId() %>">
                        <div class="mb-3">
                            <textarea id="content" name="content"
                                      class="form-control"
                                      rows="4"
                                      placeholder="写下你的观影感受..."
                                      required></textarea>
                        </div>
                        <button type="submit" class="btn btn-gold">
                            <i class="fas fa-paper-plane me-2"></i>发布评论
                        </button>
                    </form>
                </div>
            </div>
        <% } %>

        <!-- 评论列表 -->
        <div class="card mb-4" style="background: rgba(30, 30, 50, 0.8); border: 1px solid rgba(255, 215, 0, 0.2); border-radius: 15px;">
            <div class="card-body">
                <h5 class="card-title mb-4" style="color: #ffd700;">
                    <i class="fas fa-comments me-2"></i>影评列表
                </h5>
                <% List<MovieComment> comments = (List<MovieComment>) request.getAttribute("comments");
                   if (comments != null && !comments.isEmpty()) {
                       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                       for (MovieComment comment : comments) { %>
                        <div class="border-bottom pb-3 mb-3" style="border-color: rgba(255, 215, 0, 0.2) !important;">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <div class="d-flex align-items-center">
                                    <div class="rounded-circle me-2"
                                         style="width: 35px; height: 35px; background: linear-gradient(135deg, #ffd700, #ffed4e); display: flex; align-items: center; justify-content: center;">
                                        <span style="font-weight: bold; color: #1a1a2e;">
                                            <%= comment.getUserId() != null && String.valueOf(comment.getUserId()).length() > 0 ? String.valueOf(comment.getUserId()).substring(0, 1).toUpperCase() : "?" %>
                                        </span>
                                    </div>
                                    <strong style="color: #ffd700;">用户 <c:out value="${comment.userId}" /></strong>
                                </div>
                                <small class="text-muted"><%= sdf.format(comment.getPublishTime()) %></small>
                            </div>
                            <p class="text-secondary mb-0" style="line-height: 1.6;">
                                <%= comment.getContent() %>
                            </p>
                        </div>
                <%     }
                   } else { %>
                    <div class="text-center py-4">
                        <i class="fas fa-comment-slash mb-3" style="font-size: 2rem; color: #666;"></i>
                        <p class="text-muted mb-0">暂无评论，快来发表第一条评论吧！</p>
                    </div>
                <% } %>
            </div>
        </div>

    <% } else { %>
        <div class="text-center py-5">
            <i class="fas fa-film mb-4" style="font-size: 4rem; color: #666;"></i>
            <h3 class="mb-3">电影不存在</h3>
            <p class="text-muted mb-4">抱歉，您查找的电影不存在</p>
            <a href="movieList" class="btn btn-outline-gold">
                <i class="fas fa-arrow-left me-2"></i>返回电影列表
            </a>
        </div>
    <% } %>

<%@ include file="footer.jsp" %>
