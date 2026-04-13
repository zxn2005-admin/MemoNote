# 阶段1: 构建前端
FROM node:18-alpine AS frontend-builder

WORKDIR /app/frontend

# 复制前端依赖文件
COPY frontend/package*.json ./
RUN npm install

# 复制前端源码并构建
COPY frontend/ ./
RUN npm run build

# 阶段2: 构建后端
FROM maven:3.9-eclipse-temurin-17-alpine AS backend-builder

WORKDIR /app/backend

# 复制后端依赖文件
COPY backend/pom.xml ./
RUN mvn dependency:go-offline

# 复制后端源码并构建
COPY backend/src ./src
RUN mvn clean package -DskipTests

# 阶段3: 最终运行镜像
FROM eclipse-temurin:17-jre-alpine

# 安装nginx
RUN apk add --no-cache nginx

WORKDIR /app

# 复制后端jar包
COPY --from=backend-builder /app/backend/target/*.jar app.jar

# 复制前端构建产物到nginx目录
COPY --from=frontend-builder /app/frontend/dist /usr/share/nginx/html

# 复制nginx配置
COPY nginx/nginx.conf /etc/nginx/nginx.conf

# 暴露端口
EXPOSE 80 8080

# 启动脚本
COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh

CMD ["/app/start.sh"]
