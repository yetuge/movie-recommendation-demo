<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.movie.entity.OrdinaryUser" %>
<%@ page import="com.movie.entity.MovieInformation" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>电影后台 - 管理系统</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- FontAwesome Icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #f5f5f5;
        }
        .navbar-brand {
            font-weight: 600;
        }
        .poster-thumb {
            width: 50px;
            height: 75px;
            object-fit: cover;
            border-radius: 4px;
        }
        .table th {
            background-color: #343a40;
            color: white;
        }
    </style>
</head>
<body>

<!-- 顶部导航栏 -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="admin/movies">
            <i class="fas fa-cogs me-2"></i>电影后台管理系统
        </a>
        <div class="d-flex align-items-center">
            <span class="text-white me-3">
                <i class="fas fa-user-shield me-1"></i>
                <c:if test="${not empty sessionScope.currentUser}">
                    <c:out value="${sessionScope.currentUser.userName}"/>
                </c:if>
            </span>
            <a href="<%=request.getContextPath()%>/index.jsp" class="btn btn-outline-light btn-sm">
                <i class="fas fa-home me-1"></i>返回前台首页
            </a>
        </div>
    </div>
</nav>

<!-- 主体内容 -->
<div class="container mt-4">
    <div class="card shadow-sm">
        <!-- 卡片头部 -->
        <div class="card-header bg-white d-flex justify-content-between align-items-center py-3">
            <h5 class="mb-0">
                <i class="fas fa-film me-2"></i>电影列表
            </h5>
            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addMovieModal">
                <i class="fas fa-plus me-1"></i>添加新电影
            </button>
        </div>
        <!-- 卡片内容 -->
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead>
                        <tr>
                            <th style="width: 60px;">ID</th>
                            <th style="width: 80px;">海报</th>
                            <th>电影名称</th>
                            <th>类型</th>
                            <th>导演</th>
                            <th style="width: 100px;">状态</th>
                            <th style="width: 140px;">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty movies and not empty movies[0]}">
                                <c:forEach items="${movies}" var="movie">
                                    <tr>
                                        <td><c:out value="${movie.movieId}"/></td>
                                        <td>
                                            <img src="${pageContext.request.contextPath}/images/${not empty movie.imageUrl ? movie.imageUrl : 'default.jpg'}"
                                                 alt="${movie.name}"
                                                 class="poster-thumb"
                                                 loading="lazy"
                                                 onerror="this.src='${pageContext.request.contextPath}/images/default.jpg'">
                                        </td>
                                        <td>
                                            <strong><c:out value="${movie.name}"/></strong>
                                        </td>
                                        <td>
                                            <span class="badge bg-info">
                                                <c:out value="${movie.genre}"/>
                                            </span>
                                        </td>
                                        <td><c:out value="${movie.director}"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${movie.isShowing == 1}">
                                                    <a href="${pageContext.request.contextPath}/admin/movies?action=toggleStatus&movieId=${movie.movieId}&currentStatus=1"
                                                       class="badge bg-success text-decoration-none p-2"
                                                       onclick="return confirm('确定要下架这部电影吗？前台将不再显示');">
                                                        <i class="fas fa-arrow-down me-1"></i>上架中
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${pageContext.request.contextPath}/admin/movies?action=toggleStatus&movieId=${movie.movieId}&currentStatus=0"
                                                       class="badge bg-secondary text-decoration-none p-2"
                                                       onclick="return confirm('确定要重新上架这部电影吗？');">
                                                        <i class="fas fa-arrow-up me-1"></i>已下架
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <div class="btn-group btn-group-sm">
                                                <button type="button" class="btn btn-warning"
                                                        onclick="editMovie(${movie.movieId})">
                                                    <i class="fas fa-edit"></i> 编辑
                                                </button>
                                                <button type="button" class="btn btn-danger"
                                                        onclick="deleteMovie(${movie.movieId}, '${movie.name}')">
                                                    <i class="fas fa-trash"></i> 删除
                                                </button>
                                            </div>
                                            <!-- 隐藏字段存储数据 -->
                                            <input type="hidden" id="movie-name-${movie.movieId}" value="${movie.name}">
                                            <input type="hidden" id="movie-genre-${movie.movieId}" value="${movie.genre}">
                                            <input type="hidden" id="movie-director-${movie.movieId}" value="${movie.director}">
                                            <input type="hidden" id="movie-actors-${movie.movieId}" value="${movie.actors}">
                                            <input type="hidden" id="movie-releaseTime-${movie.movieId}"
                                                   value="<fmt:formatDate value="${movie.releaseTime}" pattern="yyyy-MM-dd"/>">
                                            <input type="hidden" id="movie-score-${movie.movieId}" value="${movie.score}">
                                            <input type="hidden" id="movie-duration-${movie.movieId}" value="${movie.duration}">
                                            <input type="hidden" id="movie-country-${movie.movieId}" value="${movie.country}">
                                            <input type="hidden" id="movie-content-${movie.movieId}" value="${movie.content}">
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="7" class="text-center py-5">
                                        <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                        <p class="text-muted mb-0">暂无
电影数据</p>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- 添加电影 Modal -->
<div class="modal fade" id="addMovieModal" tabindex="-1" aria-labelledby="addMovieModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="addMovieModalLabel">
                    <i class="fas fa-plus-circle me-2"></i>添加新电影
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <form action="admin/movies?action=add" method="post">
                <div class="modal-body">
                    <div class="row g-3">
                        <div class="col-12">
                            <label for="add-name" class="form-label">电影名称 <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="add-name" name="name" required>
                        </div>
                        <div class="col-md-6">
                            <label for="add-genre" class="form-label">类型</label>
                            <select class="form-select" id="add-genre" name="genre">
                                <option value="">请选择类型</option>
                                <option value="剧情">剧情</option>
                                <option value="科幻">科幻</option>
                                <option value="爱情">爱情</option>
                                <option value="动画">动画</option>
                                <option value="动作">动作</option>
                                <option value="喜剧">喜剧</option>
                                <option value="恐怖">恐怖</option>
                                <option value="悬疑">悬疑</option>
                                <option value="战争">战争</option>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label for="add-releaseTime" class="form-label">上映日期</label>
                            <input type="date" class="form-control" id="add-releaseTime" name="releaseTime">
                        </div>
                        <div class="col-md-6">
                            <label for="add-director" class="form-label">导演</label>
                            <input type="text" class="form-control" id="add-director" name="director">
                        </div>
                        <div class="col-md-6">
                            <label for="add-actors" class="form-label">主演</label>
                            <input type="text" class="form-control" id="add-actors" name="actors"
                                   placeholder="多个演员用逗号分隔">
                        </div>
                        <div class="col-md-4">
                            <label for="add-score" class="form-label">评分</label>
                            <input type="number" class="form-control" id="add-score" name="score"
                                   min="0" max="10" step="0.1" value="0">
                        </div>
                        <div class="col-md-4">
                            <label for="add-duration" class="form-label">时长（分钟）</label>
                            <input type="number" class="form-control" id="add-duration" name="duration"
                                   min="0" value="0">
                        </div>
                        <div class="col-md-4">
                            <label for="add-country" class="form-label">国家</label>
                            <input type="text" class="form-control" id="add-country" name="country">
                        </div>
                        <div class="col-12">
                            <label for="add-content" class="form-label">剧情简介</label>
                            <textarea class="form-control" id="add-content" name="content" rows="3"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save me-1"></i>保存
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- 编辑电影 Modal -->
<div class="modal fade" id="editMovieModal" tabindex="-1" aria-labelledby="editMovieModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-warning text-dark">
                <h5 class="modal-title" id="editMovieModalLabel">
                    <i class="fas fa-edit me-2"></i>编辑电影
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="admin/movies?action=update" method="post">
                <div class="modal-body">
                    <input type="hidden" id="edit-movieId" name="movieId">
                    <div class="row g-3">
                        <div class="col-12">
                            <label for="edit-name" class="form-label">电影名称 <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="edit-name" name="name" required>
                        </div>
                        <div class="col-md-6">
                            <label for="edit-genre" class="form-label">类型</label>
                            <select class="form-select" id="edit-genre" name="genre">
                                <option value="">请选择类型</option>
                                <option value="剧情">剧情</option>
                                <option value="科幻">科幻</option>
                                <option value="爱情">爱情</option>
                                <option value="动画">动画</option>
                                <option value="动作">动作</option>
                                <option value="喜剧">喜剧</option>
                                <option value="恐怖">恐怖</option>
                                <option value="悬疑">悬疑</option>
                                <option value="战争">战争</option>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label for="edit-releaseTime" class="form-label">上映日期</label>
                            <input type="date" class="form-control" id="edit-releaseTime" name="releaseTime">
                        </div>
                        <div class="col-md-6">
                            <label for="edit-director" class="form-label">导演</label>
                            <input type="text" class="form-control" id="edit-director" name="director">
                        </div>
                        <div class="col-md-6">
                            <label for="edit-actors" class="form-label">主演</label>
                            <input type="text" class="form-control" id="edit-actors" name="actors"
                                   placeholder="多个演员用逗号分隔">
                        </div>
                        <div class="col-md-4">
                            <label for="edit-score" class="form-label">评分</label>
                            <input type="number" class="form-control" id="edit-score" name="score"
                                   min="0" max="10" step="0.1" value="0">
                        </div>
                        <div class="col-md-4">
                            <label for="edit-duration" class="form-label">时长（分钟）</label>
                            <input type="number" class="form-control" id="edit-duration" name="duration"
                                   min="0" value="0">
                        </div>
                        <div class="col-md-4">
                            <label for="edit-country" class="form-label">国家</label>
                            <input type="text" class="form-control" id="edit-country" name="country">
                        </div>
                        <div class="col-12">
                            <label for="edit-content" class="form-label">剧情简介</label>
                            <textarea class="form-control" id="edit-content" name="content" rows="3"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                    <button type="submit" class="btn btn-warning">
                        <i class="fas fa-save me-1"></i>保存修改
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // 编辑电影
    function editMovie(movieId) {
        var name = document.getElementById('movie-name-' + movieId).value;
        var genre = document.getElementById('movie-genre-' + movieId).value;
        var director = document.getElementById('movie-director-' + movieId).value;
        var actors = document.getElementById('movie-actors-' + movieId).value;
        var releaseTime = document.getElementById('movie-releaseTime-' + movieId).value;
        var score = document.getElementById('movie-score-' + movieId).value;
        var duration = document.getElementById('movie-duration-' + movieId).value;
        var country = document.getElementById('movie-country-' + movieId).value;
        var content = document.getElementById('movie-content-' + movieId).value;

        document.getElementById('edit-movieId').value = movieId;
        document.getElementById('edit-name').value = name;
        document.getElementById('edit-genre').value = genre;
        document.getElementById('edit-director').value = director;
        document.getElementById('edit-actors').value = actors;
        document.getElementById('edit-releaseTime').value = releaseTime;
        document.getElementById('edit-score').value = score;
        document.getElementById('edit-duration').value = duration;
        document.getElementById('edit-country').value = country;
        document.getElementById('edit-content').value = content;

        var editModal = new bootstrap.Modal(document.getElementById('editMovieModal'));
        editModal.show();
    }

    // 删除电影
    function deleteMovie(movieId, movieName) {
        if (confirm('确定要删除电影「' + movieName + '」吗？\n\n此操作将同时删除该电影的所有评分、评论和收藏记录，且无法撤销！')) {
            window.location.href = 'admin/movies?action=delete&movieId=' + movieId;
        }
    }
</script>

</body>
</html>
