# MemoNote - 个人备忘录系统

一个简洁美观的个人备忘录系统，支持用户注册登录、备忘录增删改查、分类管理、简单搜索和管理后台功能。

## 技术栈

### 前端
- Vue 3
- Vite
- Element Plus
- Axios
- Vue Router
- Pinia

### 后端
- Java 17
- Spring Boot 3
- Spring Security
- MyBatis-Plus
- MySQL 8.0
- JWT

## 功能特性

- ✅ 用户注册登录（密码加密存储）
- ✅ 备忘录列表展示（按时间排序、置顶）
- ✅ 备忘录的添加、编辑、删除
- ✅ 分类管理功能
- ✅ 关键字搜索功能
- ✅ 标记完成/置顶功能
- ✅ 响应式设计，适配电脑和手机
- ✅ 个人中心
- ✅ 管理员后台（用户管理）

## 快速开始

### 方式一：Docker 一键启动（推荐）

确保已安装 Docker 和 Docker Compose，然后在项目根目录执行：

```bash
docker-compose up -d
```

启动完成后访问：
- 前端：http://localhost:3000
- 后端API：http://localhost:8080/api

### 方式二：本地开发运行

#### 1. 启动 MySQL 数据库

确保本地已安装 MySQL 8.0，创建数据库并执行初始化脚本：
```bash
mysql -u root -p < sql/memonote.sql
```

修改 `backend/src/main/resources/application.yml` 中的数据库连接配置。

#### 2. 启动后端

```bash
cd backend

# Maven 启动
mvn clean install
mvn spring-boot:run
```

后端启动在 http://localhost:8080

#### 3. 启动前端

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端启动在 http://localhost:3000

## 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 演示用户 | demo | demo123 |

## 项目结构

```
MemoNote/
├── backend/                 # 后端项目
│   ├── src/main/java/com/memonote/
│   │   ├── config/         # 配置类
│   │   ├── controller/     # 控制层
│   │   ├── dto/            # 数据传输对象
│   │   ├── entity/         # 实体类
│   │   ├── mapper/         # 数据访问层
│   │   ├── service/        # 业务逻辑层
│   │   ├── common/         # 通用类
│   │   └── MemoNoteApplication.java
│   ├── pom.xml
│   └── Dockerfile
├── frontend/               # 前端项目
│   ├── src/
│   │   ├── views/          # 页面
│   │   ├── layouts/        # 布局
│   │   ├── stores/         # 状态管理
│   │   ├── router/         # 路由
│   │   └── utils/          # 工具类
│   ├── package.json
│   └── Dockerfile
├── sql/                    # 数据库脚本
├── docker-compose.yml
└── README.md
```

## 主要功能说明

### 1. 用户认证
- 密码使用 BCrypt 加密存储
- 使用 JWT Token 进行身份验证
- 支持用户注册和登录

### 2. 备忘录功能
- 按创建时间倒序排列
- 支持置顶功能
- 支持标记完成
- 可按分类筛选
- 支持关键词搜索

### 3. 分类管理
- 用户可自定义分类
- 支持分类颜色设置
- 分类与备忘录关联显示

### 4. 管理后台
- 管理员可查看所有用户
- 支持搜索用户
- 支持设置/取消管理员权限
- 支持删除用户

## 数据库表结构

### user 用户表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名 |
| password | VARCHAR(255) | 加密密码 |
| email | VARCHAR(100) | 邮箱 |
| avatar | VARCHAR(255) | 头像 |
| role | VARCHAR(20) | 角色 |

### category 分类表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| name | VARCHAR(50) | 分类名称 |
| color | VARCHAR(20) | 分类颜色 |

### memo 备忘录表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| category_id | BIGINT | 分类ID |
| title | VARCHAR(200) | 标题 |
| content | TEXT | 内容 |
| is_top | TINYINT | 是否置顶 |
| is_complete | TINYINT | 是否完成 |

## 浏览器支持

- Chrome (推荐)
- Firefox
- Safari
- Edge
- 移动端浏览器

## 许可证

MIT License
