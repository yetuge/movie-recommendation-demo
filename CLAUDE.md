# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

# Movie 项目

详细的使用说明和 API 文档请参阅 [README.md](README.md)。

## 项目概述

采用 MVC 模式的 Java Web 应用，包含：

- Web 前端 (JSP)
- 管理后台 (admin.jsp)
- 共享的 MySQL 8.0 数据库

## 技术栈

| 层次     | 技术方案             |
| -------- | -------------------- |
| 后端     | Java (Servlet + JSP) |
| 数据库   | MySQL 8.0            |
| 服务器   | Apache Tomcat        |
| 数据访问 | JDBC                 |
| 前端     | JSP (Web端)          |
| JSON     | Gson 2.10.1          |

## 架构模式

MVC (Model-View-Controller) 设计模式

## 项目结构

```
movie/
├── src/main/java/com/movie/
│   ├── entity/     # 数据库实体类 (JavaBean)
│   ├── dao/         # 数据访问层 (JDBC)
│   ├── service/     # 业务逻辑层
│   ├── controller/  # Servlet 控制层 (Web + API)
│   └── util/        # 工具类 (DBUtil, JsonUtil)
├── src/main/webapp/
│   ├── WEB-INF/
│   │   └── web.xml  # Web 部署描述符
│   ├── css/         # 样式表
│   ├── images/       #_电影海报图片
│   ├── js/          # JavaScript 文件
│   └── *.jsp        # JSP 页面
├── pom.xml          # Maven 配置文件
└── target/          # 编译输出目录
```

## 常用命令

```bash
# 编译打包
mvn clean package

# 部署到 Tomcat
# 1. 将 target/movie.war 复制到 Tomcat webapps 目录
# 2. 重启 Tomcat
```

## 架构说明

### 分层架构

- **Entity**: JavaBean 实体类，对应数据库表
  - OrdinaryUser, MovieInformation, MovieScore, MovieComment, Collect

- **DAO**: 数据访问层，直接操作 JDBC
  - UserDao, MovieDao, CommentDao, ScoreDao, CollectDao
  - 所有数据库操作都在此层完成

- **Service**: 业务逻辑层
  - UserService: 用户注册/登录业务
  - RecommendationService: 电影推荐算法（基于内容 + 热门推荐）

- **Controller**: Servlet 控制层
  - Web Servlet: 处理 JSP 页面请求，使用转发
  - API Servlet: 处理 JSON 请求，返回 JSON

### 数据库连接

DBUtil 类提供 JDBC 连接：

- 数据库名: `movie_recommend_db`
- 使用 `DBUtil.getConnection()` 获取连接
- 使用 `DBUtil.close(conn, pstmt, rs)` 释放资源

### Session 管理

登录用户存储在 Session 中，key 为 `"currentUser"`

```java
HttpSession session = request.getSession();
OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");
```

### 电影海报图片

- 海报图片存储在 `src/main/webapp/images/` 目录
- 数据库 `movie_information` 表的 `image_url` 字段存储图片文件名
- JSP 页面使用以下格式显示海报：
  ```jsp
  src="${pageContext.request.contextPath}/images/<%= movie.getImageUrl() %>"
  onerror="this.src='${pageContext.request.contextPath}/images/default.jpg'"
  ```
- 默认海报：`images/default.jpg`

## API 端点

### 电影 API (`/api/movie`)

检测方式: `format=json` 参数 或 `Accept: application/json` 头

| action | 方法 | 说明                   |
| ------ | ---- | ---------------------- |
| list   | GET  | 获取电影列表           |
| detail | GET  | 获取电影详情 (movieId) |
| search | GET  | 搜索电影 (keyword)     |
| genre  | GET  | 按类型筛选 (genre)     |

### 用户 API (`/api/user`)

| action      | 方法 | 说明                                                                      |
| ----------- | ---- | ------------------------------------------------------------------------- |
| login       | POST | 登录 (account, password)                                                  |
| register    | POST | 注册 (phoneNumber, userMailbox, userPassword, userName, gender, birthday) |
| logout      | POST | 登出                                                                      |
| currentUser | GET  | 获取当前用户                                                              |
| recommend   | GET  | 获取推荐电影                                                              |

### Web Servlet 映射

| 路径         | 说明     |
| ------------ | -------- |
| /login       | 登录     |
| /register    | 注册     |
| /logout      | 登出     |
| /movieList   | 电影列表 |
| /movieDetail | 电影详情 |
| /addComment  | 添加评论 |
| /addScore    | 添加评分 |
| /collect     | 收藏操作 |
| /recommend   | 推荐页面 |
| /admin       | 管理后台 |

### 管理后台 API (`/admin/movies`)

| action       | 方法 | 说明                 |
| ------------ | ---- | -------------------- |
| (default)    | GET  | 获取电影管理列表     |
| add          | POST | 添加新电影           |
| update       | POST | 更新电影信息         |
| delete       | POST | 删除电影（级联删除） |
| toggleStatus | POST | 切换电影上下架状态   |

## 推荐算法

### 策略 A：基于内容推荐

1. 获取用户收藏和高分（>8分）电影
2. 统计最热门的类型
3. 推荐同类型但未看过的电影（最多10部）

### 策略 B：热门推荐（冷启动）

1. 计算每部电影的平均分
2. 返回平均分最高的10部电影

## 前端安全与性能优化

### 已实现的安全修复

- **XSS 防护**: 使用 JSTL `<c:out>` 标签输出用户数据
- **Accessibility**: 错误消息添加 `role="alert"` 和 `aria-live="assertive"`
- **图片懒加载**: 所有电影海报添加 `loading="lazy"` 属性

### 已实现的性能优化

- **CDN 完整性**: Bootstrap CDN 添加 integrity 和 crossorigin 属性
- **代码质量**: JavaScript 提取魔术数字为常量
- **响应式**: 使用 Bootstrap 间距工具类替代内联样式

## 开发说明

- 使用 `JsonUtil.toJson(obj)`/`fromJson(json, clazz)` 进行 JSON 序列化
- 所有 API 响应格式: `{"success": true/false, "message": "...", "data": ...}`
- 数据库配置应使用环境变量或属性文件

## 已知问题与解决方案

### 样式加载问题

参考 `FRONTEND_STYLES_GUIDE.md` 和 `STYLE_TROUBLESHOOTING.md`

# currentDate

Today's date is 2026-03-05.
