# MemoNote - 个人备忘录系统

一个简洁、响应式的个人备忘录管理系统，支持备忘录的增删改查、分类管理、搜索筛选、用户认证等功能。

## 技术栈

### 前端
- Vue 3 + Vite
- Element Plus (UI组件库)
- Pinia (状态管理)
- Vue Router (路由)
- Axios (HTTP请求)

### 后端
- Java 17
- Spring Boot 3
- MyBatis-Plus (ORM)
- Spring Security + JWT (认证)
- MySQL 8.0

## 项目结构

```
MemoNote/
├── memo-note-backend/          # 后端项目
│   ├── src/main/java/com/memonote/
│   │   ├── config/            # 配置类
│   │   ├── controller/        # 控制器
│   │   ├── dto/               # 数据传输对象
│   │   ├── entity/            # 实体类
│   │   ├── mapper/            # MyBatis映射器
│   │   ├── security/          # 安全相关
│   │   ├── service/           # 服务层
│   │   └── vo/                # 视图对象
│   └── src/main/resources/
│       └── db/schema.sql      # 数据库脚本
├── memo-note-frontend/         # 前端项目
│   ├── src/
│   │   ├── api/               # API接口
│   │   ├── components/        # 组件
│   │   ├── router/            # 路由
│   │   ├── stores/            # Pinia状态
│   │   └── views/             # 页面视图
│   └── package.json
├── docker-compose.yml          # Docker编排
├── Dockerfile.backend          # 后端Dockerfile
├── Dockerfile.frontend         # 前端Dockerfile
└── nginx.conf                  # Nginx配置
```

## 功能特性

### 用户功能
- 用户注册/登录
- JWT Token认证
- 密码加密存储 (BCrypt)
- 个人中心（修改资料、密码）

### 备忘录功能
- 创建、编辑、删除备忘录
- 备忘录列表（分页、排序）
- 分类管理
- 优先级设置（普通/重要/紧急）
- 完成状态切换
- 提醒时间设置

### 搜索与筛选
- 关键词搜索（标题、内容）
- 按分类筛选
- 按状态筛选（已完成/未完成）
- 按优先级筛选

### 管理后台
- 用户管理
- 统计概览
- 用户状态管理

### 界面特性
- 响应式设计（适配移动端）
- 简洁美观的UI
- 流畅的交互体验

## 快速开始

### 方式一：Docker Compose 部署（推荐）

1. 确保已安装 Docker 和 Docker Compose

2. 构建并启动服务
```bash
docker-compose up -d
```

3. 等待服务启动（首次启动需要下载镜像和构建，可能需要几分钟）

4. 访问应用
- 前端页面: http://localhost
- 后端API: http://localhost:8080

5. 默认账号
- 管理员: admin / admin123

### 方式二：本地开发环境

#### 后端启动

1. 创建 MySQL 数据库
```sql
CREATE DATABASE memo_note CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行数据库脚本
```bash
mysql -u root -p memo_note < memo-note-backend/src/main/resources/db/schema.sql
```

3. 修改数据库配置
编辑 `memo-note-backend/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/memo_note?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

4. 启动后端
```bash
cd memo-note-backend
mvn clean install
mvn spring-boot:run
```

后端服务将在 http://localhost:8080 启动

#### 前端启动

1. 安装依赖
```bash
cd memo-note-frontend
npm install
```

2. 启动开发服务器
```bash
npm run dev
```

前端开发服务器将在 http://localhost:3000 启动

## API 接口文档

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `GET /api/auth/info` - 获取当前用户信息

### 用户接口
- `GET /api/user/profile` - 获取个人资料
- `PUT /api/user/profile` - 更新个人资料

### 备忘录接口
- `GET /api/memo/list` - 获取备忘录列表
- `GET /api/memo/page` - 分页获取备忘录
- `GET /api/memo/{id}` - 获取备忘录详情
- `POST /api/memo` - 创建备忘录
- `PUT /api/memo/{id}` - 更新备忘录
- `DELETE /api/memo/{id}` - 删除备忘录
- `PUT /api/memo/{id}/toggle` - 切换完成状态
- `GET /api/memo/statistics` - 获取统计信息

### 分类接口
- `GET /api/category/list` - 获取分类列表
- `POST /api/category` - 创建分类
- `PUT /api/category/{id}` - 更新分类
- `DELETE /api/category/{id}` - 删除分类

### 管理接口
- `GET /api/admin/users` - 获取用户列表
- `PUT /api/admin/users/{id}/status` - 更新用户状态
- `DELETE /api/admin/users/{id}` - 删除用户
- `GET /api/admin/statistics` - 获取管理统计

## 数据库表结构

### 用户表 (user)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名 |
| password | VARCHAR(255) | 加密密码 |
| email | VARCHAR(100) | 邮箱 |
| nickname | VARCHAR(50) | 昵称 |
| avatar | VARCHAR(255) | 头像 |
| role | TINYINT | 角色(0-用户,1-管理员) |
| status | TINYINT | 状态(0-禁用,1-启用) |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### 分类表 (category)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 分类名称 |
| color | VARCHAR(20) | 颜色 |
| icon | VARCHAR(50) | 图标 |
| sort_order | INT | 排序 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### 备忘录表 (memo)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| category_id | BIGINT | 分类ID |
| title | VARCHAR(200) | 标题 |
| content | TEXT | 内容 |
| priority | TINYINT | 优先级(0-普通,1-重要,2-紧急) |
| status | TINYINT | 状态(0-未完成,1-已完成) |
| remind_time | DATETIME | 提醒时间 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

## 安全说明

1. 密码使用 BCrypt 算法加密存储
2. 使用 JWT Token 进行身份认证
3. API 接口需要携带 Authorization: Bearer {token} 请求头
4. 管理员接口需要 ADMIN 角色权限

## 开发说明

### 后端开发
- 使用 Lombok 简化代码
- 使用 MyBatis-Plus 简化数据库操作
- 统一返回 Result 对象
- 全局异常处理

### 前端开发
- 使用 Composition API
- 使用 Pinia 进行状态管理
- 使用 Element Plus 组件库
- 响应式布局适配移动端

## 常见问题

1. **数据库连接失败**
   - 检查 MySQL 服务是否启动
   - 检查数据库配置是否正确
   - 检查数据库是否已创建

2. **前端无法访问后端**
   - 检查后端服务是否启动
   - 检查 vite.config.js 中的代理配置
   - 检查是否存在跨域问题

3. **Docker 部署问题**
   - 确保 Docker 和 Docker Compose 已安装
   - 检查端口是否被占用
   - 查看容器日志: `docker-compose logs`

## License

MIT License
