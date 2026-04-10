# 1. 基础镜像 - 使用OpenJDK 17作为基础镜像
FROM openjdk:17-jdk-slim

# 2. 设置工作目录
WORKDIR /app

# 3. 安装必要环境 - Node.js 18, Maven, Git
RUN apt-get update && apt-get install -y \
    curl \
    wget \
    gnupg \
    git \
    maven \
    && curl -fsSL https://deb.nodesource.com/setup_18.x | bash - \
    && apt-get install -y nodejs \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# 4. 验证安装
RUN java -version
RUN node -v
RUN npm -v
RUN mvn -v

# 5. 创建项目工作目录
RUN mkdir -p /app/backend /app/frontend

# 6. 预留 repo 目录 (用于挂载代码仓库)
RUN mkdir -p /repo

# 7. 暴露端口 - 前端5173, 后端8080
EXPOSE 5173 8080

# 8. 默认启动命令 - 进入交互式 shell
CMD ["bash"]
