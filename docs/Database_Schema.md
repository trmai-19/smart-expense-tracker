# Database Schema

> Tài liệu mô tả cấu trúc database của hệ thống SET.
> Database: **PostgreSQL**
> Cập nhật file này mỗi khi thêm/sửa bảng hoặc quan hệ.

---

## Quy tắc đặt file Database

| Loại file | Server | Android |
|---|---|---|
| Domain Model (POJO) | `domain/model/` | `domain/model/` |
| DB Entity (JPA/Room) | `infrastructure/persistence/entity/` | `data/local/entity/` |
| DB Repository Interface | `domain/repository/` | `domain/repository/` |
| DB Repository Impl | `infrastructure/persistence/repository/` | `data/repository/` |
| Mapper (Entity ↔ Model) | `infrastructure/persistence/mapper/` | `data/remote/mapper/` |
| DAO (Room) | — | `data/local/dao/` |
| Output Port | `application/port/out/` | — |

> **QUAN TRỌNG**: Domain Model (`domain/model/`) và DB Entity (`infrastructure/` hoặc `data/`) là **2 class khác nhau**. Domain Model không có annotation `@Entity`, `@Table`, `@Column`. Mapper chuyển đổi giữa chúng.

---

## Tables

_Chưa triển khai. Cập nhật khi tạo bảng._

## Relationships

_Chưa triển khai. Cập nhật khi tạo quan hệ._

## Indexes

_Chưa triển khai. Cập nhật khi tạo index._
