# CI/CD Pipeline cho Department Service với Docker và GitHub Actions

## Mô tả

Hướng dẫn thiết lập quy trình CI/CD tự động cho ứng dụng Spring Boot (Department Service) dùng Docker và GitHub Actions.  
Quy trình gồm 2 phần chính:

- **CI**: Build và push Docker image lên Docker Hub khi có push vào nhánh `main`.
- **CD**: Deploy image mới nhất lên Ubuntu Server bằng self-hosted runner.

---

## 1. Dockerfile

Tạo file `Dockerfile` trong thư mục gốc project với nội dung:

```dockerfile
# Stage 1: Build JAR
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Stage 2: Run JAR
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","app.jar"]
```

---

## 2. GitHub Actions Workflow

Tạo thư mục `.github/workflows/` trong repo, sau đó tạo 2 file:

### 2.1 CI workflow - `ci.yml`

```yaml
name: CI - Build and Push Docker Image

on:
  push:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Login to Docker Hub
        uses: docker/login-action@v2
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}

      - name: Build and push Docker image
        run: |
          IMAGE_NAME=${{ secrets.DOCKER_USERNAME }}/department-service
          docker build -t $IMAGE_NAME:latest .
          docker push $IMAGE_NAME:latest
```

### 2.2 CD workflow - `deploy.yml`

```yaml
name: Deploy to Ubuntu Server

on:
  workflow_run:
    workflows: ["CI - Build and Push Docker Image"]
    types:
      - completed

jobs:
  deploy:
    if: ${{ github.event.workflow_run.conclusion == 'success' }}
    runs-on: self-hosted

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Login to Docker Hub
        uses: docker/login-action@v2
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}

      - name: Pull latest image from Docker Hub
        run: |
          IMAGE_NAME=${{ secrets.DOCKER_USERNAME }}/department-service
          docker pull $IMAGE_NAME:latest

      - name: Stop and remove old container
        run: |
          docker stop department-service || true
          docker rm department-service || true

      - name: Run new container
        run: |
          IMAGE_NAME=${{ secrets.DOCKER_USERNAME }}/department-service
          docker run -d \
            --name department-service \
            -p 8083:8083 \
            $IMAGE_NAME:latest
```

---

## 3. Cấu hình Self-Hosted Runner trên GitHub

1. Vào repository trên GitHub, chọn **Settings > Actions > Runners**  
2. Nhấn **New self-hosted runner**  
3. Chọn hệ điều hành **Linux**, kiến trúc **x64**  
4. Làm theo hướng dẫn tải và cài đặt runner trên Ubuntu Server của bạn  
5. Khởi chạy runner, đảm bảo nó online trên GitHub  
6. Runner này sẽ chạy workflow deploy và thực hiện kéo image, chạy container trên server.

---

## 4. Quy trình CI/CD hoạt động

- Khi bạn push code vào nhánh `main`, workflow CI sẽ build và đẩy Docker image lên Docker Hub.  
- Sau khi CI thành công, workflow deploy sẽ chạy trên self-hosted runner (Ubuntu server), kéo image mới nhất, dừng container cũ, chạy container mới với port 8083.

---

## 5. Lưu ý khi triển khai trên Ubuntu Server

- Đảm bảo đã cài Docker và cấu hình đúng trên server.  
- Self-hosted runner phải có quyền chạy Docker.  
- Có thể truyền thêm biến môi trường (DB URL, user, pass) khi chạy container nếu cần:

```bash
docker run -d --name department-service -p 8083:8083 \
  -e DB_URL=your_db_url \
  -e DB_USER=your_user \
  -e DB_PASS=your_pass \
  $IMAGE_NAME:latest
```

- Cấu hình secrets `DOCKER_USERNAME` và `DOCKER_PASSWORD` trong repo GitHub để đăng nhập Docker Hub.

---
