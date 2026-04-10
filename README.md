# MemoNote - 个人备忘录系统

一个简洁、美观、功能完整的个人备忘录管理系统。

## 功能特性

- 用户注册与登录（JWT认证）
- 备忘录增删改查
- 分类管理
- 优先级设置（普通/重要/紧急）
- 状态管理（未完成/已完成）
- 关键字搜索
- 筛选功能（分类、状态、优先级）
- 个人中心
- 管理后台
- 响应式设计，适配PC和移动端

## 技术栈

### 前端
- Vue 3.4
- Vite 5
- Element Plus 2.4
- Pinia (状态管理)
- Vue Router 4
- Axios
- TailwindCSS

### 后端
- Java 17
- Spring Boot 3.2
- Spring Security
- MyBatis-Plus 3.5
- JWT (jjwt 0.12)
- MySQL 8.0

## 项目结构

```
MemoNote/
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/memonote/
│   │   │   │   ├── config/     # 配置类
│   │   │   │   ├── controller/ # 控制器
│   │   │   │   ├── dto/        # 数据传输对象
│   │   │   │   ├── entity/     # 实体类
│   │   │   │   ├── mapper/     # MyBatis Mapper
│   │   │   │   ├── service/    # 服务层
│   │   │   │   └── security/   # 安全相关
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── pom.xml
│   └── Dockerfile
│
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── api/               # API接口
│   │   ├── components/        # 组件
│   │   ├── layouts/           # 布局
│   │   ├── router/            # 路由
│   │   ├── stores/            # 状态管理
│   │   ├── styles/            # 样式
│   │   └── views/             # 页面
│   ├── package.json
│   ├── vite.config.js
│   └── Dockerfile
│
├── sql/
│   └── init.sql               # 数据库初始化脚本
│
├── docker-compose.yml         # Docker编排文件
└── Dockerfile                 # 开发环境Dockerfile
```

## 快速开始

### 方式一：Docker Compose（推荐）

1. 确保已安装 Docker 和 Docker Compose

2. 克隆项目并进入目录
```bash
cd MemoNote
```

3. 启动所有服务
```bash
docker-compose up -d
```

4. 访问应用
- 前端：http://localhost:5173
- 后端API：http://localhost:8080/api

### 方式二：本地开发

#### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+

#### 1. 初始化数据库

```bash
mysql -u root -p < sql/init.sql
```

或者在MySQL客户端中执行 `sql/init.sql` 文件内容。

#### 2. 启动后端

```bash
cd backend

# 安装依赖
mvn clean install

# 启动服务
mvn spring-boot:run
```

后端服务将在 http://localhost:8080 启动

#### 3. 启动前端

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端服务将在 http://localhost:5173 启动

## 配置说明

### 后端配置 (backend/src/main/resources/application.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/memonote
    username: root
    password: root123456  # 请修改为你的数据库密码

jwt:
  secret: memonote-jwt-secret-key-2024-very-long-secret-key-for-security  # 生产环境请修改
  expiration: 86400000  # Token有效期（毫秒），默认24小时
```

### 前端配置 (frontend/vite.config.js)

```javascript
server: {
  port: 5173,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',  // 后端地址
      changeOrigin: true
    }
  }
}
```

## 默认账号

系统初始化后会创建一个管理员账号：

- 用户名：`admin`
- 密码：`admin123`
- 角色：管理员

## API接口文档

### 认证接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/auth/login | 用户登录 |
| POST | /api/auth/register | 用户注册 |
| GET | /api/auth/info | 获取当前用户信息 |

### 备忘录接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/memo/list | 获取备忘录列表 |
| GET | /api/memo/search | 搜索备忘录 |
| GET | /api/memo/{id} | 获取备忘录详情 |
| POST | /api/memo | 创建备忘录 |
| PUT | /api/memo/{id} | 更新备忘录 |
| DELETE | /api/memo/{id} | 删除备忘录 |
| PUT | /api/memo/{id}/status | 更新状态 |
| GET | /api/memo/stats | 获取统计信息 |

### 分类接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/category/list | 获取分类列表 |
| POST | /api/category | 创建分类 |
| PUT | /api/category/{id} | 更新分类 |
| DELETE | /api/category/{id} | 删除分类 |

### 用户接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/user/profile | 获取个人资料 |
| PUT | /api/user/profile | 更新个人资料 |
| PUT | /api/user/password | 修改密码 |

### 管理接口（需要管理员权限）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/admin/users | 获取用户列表 |
| PUT | /api/admin/users/{id}/status | 更新用户状态 |
| GET | /api/admin/stats | 获取系统统计 |

## 数据库表结构

### 用户表 (sys_user)
- id: 用户ID
- username: 用户名
- password: 密码（BCrypt加密）
- nickname: 昵称
- email: 邮箱
- phone: 手机号
- avatar: 头像
- role: 角色（0-普通用户，1-管理员）
- status: 状态（0-禁用，1-启用）

### 备忘录表 (memo_note)
- id: 备忘录ID
- user_id: 用户ID
- category_id: 分类ID
- title: 标题
- content: 内容
- priority: 优先级（0-普通，1-重要，2-紧急）
- status: 状态（0-未完成，1-已完成）
- reminder_time: 提醒时间
- tags: 标签

### 分类表 (memo_category)
- id: 分类ID
- user_id: 用户ID
- name: 分类名称
- color: 分类颜色
- sort: 排序

## 构建生产版本

### 后端
```bash
cd backend
mvn clean package -DskipTests
java -jar target/memonote-backend-1.0.0.jar
```

### 前端
```bash
cd frontend
npm run build
# 生成的文件在 dist 目录
```

## 安全说明

1. 密码使用 BCrypt 加密存储
2. 使用 JWT 进行身份认证
3. 支持跨域请求（CORS）
4. 接口权限控制（Spring Security）

## 开发环境 Dockerfile

项目根目录的 Dockerfile 提供了一个包含 Java 17 和 Node.js 18 的开发环境：

```bash
docker build -t memonote-dev .
docker run -it -v ${PWD}:/app -p 5173:5173 -p 8080:8080 memonote-dev
```

## 许可证

MIT License
