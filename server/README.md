# SET Server (Backend API)

Spring Boot 3.x REST API server cho ứng dụng SET (Smart Expense Tracker), quản lý bằng **Gradle** và kiến trúc **Clean Architecture**.

## 1. Công nghệ sử dụng
- **Framework**: Spring Boot 3.4.0 (Java 21 LTS)
- **Quản lý dự án**: Gradle 8.7
- **Database**: PostgreSQL (JPA / Hibernate)
- **Security**: Spring Security & JWT (JSON Web Token)
- **AI Service**: Google Gemini API

## 2. Cấu trúc Clean Architecture

```
com.smartexpense.api/
├── domain/          ← Entity, Repository Interface, Business Exceptions
├── application/     ← Use Cases, DTOs, Input/Output Ports
├── infrastructure/  ← JPA Repository Impl, Security/JWT Filters, Gemini AI Adapter, Database Config
├── presentation/    ← REST Controllers, Global Exception Handler
└── ServerApplication.java ← Main Spring Boot entrypoint
```

## 3. Chạy server với Gradle

### Biên dịch dự án:
```bash
gradle build
```

### Chạy ứng dụng:
```bash
gradle bootRun
```
Server sẽ khởi chạy tại cổng mặc định `http://localhost:8080`.
