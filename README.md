# 电影推荐系统

基于 Java Servlet + JSP 的电影推荐 Web 应用，支持 Web 浏览器和 Android 移动端。

## 功能特性

### 用户模块
- 用户注册（手机号/邮箱）
- 用户登录
- 个人信息管理

### 电影模块
- 浏览全部电影
- 电影详情查看
- 搜索电影（按名称或导演）
- 按类型筛选

### 交互模块
- 电影评分（0-10分）
- 发表评论（支持修改）
- 收藏/取消收藏
- 查看收藏列表

### 推荐系统
- **基于内容推荐**：根据用户收藏和高分电影，推荐同类型但未看过的电影
- **热门推荐**：基于平均分推荐热门电影（冷启动方案）

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 1.8 | 开发语言 |
| Servlet API | 4.0.1 | Web 容器 |
| JSP | 2.3.3 | 前端页面 |
| JSTL | 1.2 | JSP 标签库 |
| MySQL | 8.0 | 数据库 |
| JDBC | - | 数据访问 |
| Gson | 2.10.1 | JSON 序列化 |
| Maven | - | 项目构建 |
| Tomcat | - | Web 服务器 |

## 快速开始

### 前置要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Apache Tomcat 9.0+

### 数据库配置

创建数据库 `movie_recommend_db` 并执行初始化 SQL：

```sql
CREATE DATABASE movie_recommend_db;

USE movie_recommend_db;

-- 用户表
CREATE TABLE ordinary_user (
    userId INT PRIMARY KEY AUTO_INCREMENT,
    phoneNumber VARCHAR(20),
    userMailbox VARCHAR(50) UNIQUE,
    userPassword VARCHAR(100) NOT NULL,
    userName VARCHAR(50),
    gender VARCHAR(10),
    birthday DATE,
    registerTime DATETIME
);

-- 电影信息表
CREATE TABLE movie_information (
    movieId INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    content TEXT,
    genre VARCHAR(50),
    releaseTime DATE,
    director VARCHAR(50),
    actors VARCHAR(200)
);

-- 电影评分表
CREATE TABLE movie_score (
    scoreId INT PRIMARY KEY AUTO_INCREMENT,
    movieId INT,
    userId INT,
    score DOUBLE,
    publishTime DATETIME,
    FOREIGN KEY (movieId) REFERENCES movie_information(movieId),
    FOREIGN KEY (userId) REFERENCES ordinary_user(userId),
    UNIQUE KEY (movieId, userId)
);

-- 电影评论表
CREATE TABLE movie_comment (
    commentId INT PRIMARY KEY AUTO_INCREMENT,
    movieId INT,
    userId INT,
    content TEXT,
    publishTime DATETIME,
    FOREIGN KEY (movieId) REFERENCES movie_information(movieId),
    FOREIGN KEY (userId) REFERENCES ordinary_user(userId)
);

-- 收藏表
CREATE TABLE collect (
    collectId INT PRIMARY KEY AUTO_INCREMENT,
    movieId INT,
    userId INT,
    collectTime DATETIME,
    FOREIGN KEY (movieId) REFERENCES movie_information(movieId),
    FOREIGN KEY (userId) REFERENCES ordinary_user(userId),
    UNIQUE KEY (movieId, userId)
);
```

### 编译打包

```bash
mvn clean package
```

生成的 WAR 文件位于 `target/movie.war`

### 部署

将 `target/movie.war` 复制到 Tomcat 的 `webapps` 目录：

```bash
cp target/movie.war /path/to/tomcat/webapps/
```

启动 Tomcat：

```bash
/path/to/tomcat/bin/startup.sh
```

访问：http://localhost:8080/movie/

## API 文档

### 通用说明

所有 API 请求可以通过以下方式指定返回 JSON：
- URL 参数：`?format=json`
- 请求头：`Accept: application/json`
- Android 客户端自动检测

### 电影 API

**基础路径**: `/api/movie`

| 端点 | 方法 | 参数 | 说明 |
|--------|------|------|------|
| `action=list` | GET | - | 获取电影列表 |
| `action=detail` | GET | `movieId` | 获取电影详情 |
| `action=search` | GET | `keyword` | 搜索电影 |
| `action=genre` | GET | `genre` | 按类型筛选 |

**示例**:
```bash
# 获取电影列表
GET /api/movie?action=list&format=json

# 获取电影详情
GET /api/movie?action=detail&movieId=1&format=json

# 搜索电影
GET /api/movie?action=search&keyword=黑客帝国&format=json
```

### 用户 API

**基础路径**: `/api/user`

| 端点 | 方法 | 参数 | 说明 |
|--------|------|------|------|
| `action=login` | POST | `account, password` | 用户登录 |
| `action=register` | POST | `phoneNumber, userMailbox, userPassword, userName, gender, birthday` | 用户注册 |
| `action=logout` | | | 登出 |
| `action=currentUser` | GET | - | 获取当前用户 |
| `action=recommend` | GET | - | 获取推荐电影 |

**示例**:
```bash
# 登录
POST /api/user?action=login&account=test@example.com&password=123456&format=json

# 注册
POST /api/user?action=register&phoneNumber=13800138000&userMailbox=test@example.com&userPassword=123456&userName=测试用户&gender=男&birthday=1990-01-01&format=json

# 获取推荐
GET /api/user?action=recommend&format=json
```

### 响应格式

成功响应:
```json
{
  "success": true,
  "message": "操作成功",
  "data": { ... }
}
```

失败响应:
```json
{
  "success": false,
  "message": "错误信息"
}
```

## 项目结构

```
movie/
├── src/main/java/com/movie/
│   ├── entity/         # 数据库实体类
│   ├── dao/            # 数据访问层
│   ├── service/        # 业务逻辑层
│   ├── controller/      # Servlet 控制层
│   └── util/           # 工具类
├── src/main/webapp/    # Web 资源
│   ├── *.jsp           # JSP 页面
│   ├── css/            # 样式表
│   └── js/             # JavaScript 文件
└── pom.xml             # Maven 配置
```

## 推荐算法

### 策略 A：基于内容推荐

1. 获取用户收藏和高评分（>8分）的电影
2. 统计这些电影出现频率最高的类型
3. 查询该类型下、用户未看过的电影
4. 返回最多 10 部推荐电影

### 策略 B：热门推荐（冷启动）

1. 计算每部电影的平均评分
2. 按平均分排序，返回最高分的 10 部电影

## 开发

```bash
# 编译
mvn compile

# 运行测试
mvn test

# 打包
mvn package
```

## License

MIT
