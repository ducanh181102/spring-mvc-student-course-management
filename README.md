# Spring MVC Student Course Management

Dự án quản lý học viên, khóa học và đăng ký khóa học, được dùng để học Spring Framework mà không dùng Spring Boot.

## Trạng thái hiện tại

Dự án đang ở giai đoạn nền tảng Spring Core: chương trình chạy bằng menu console, dùng Spring để quản lý dependency và Spring JDBC để làm việc với H2. Các controller, cấu hình Servlet và REST API bằng Spring MVC chưa được triển khai.

## Chức năng hiện có

- Quản lý học viên: xem danh sách, thêm, sửa tên và xóa.
- Quản lý khóa học: xem danh sách, thêm, sửa tên, cập nhật sức chứa và xóa.
- Quản lý đăng ký: xem danh sách, đăng ký học viên vào khóa học và hủy đăng ký.
- Kiểm tra học viên hoặc khóa học tồn tại, đăng ký trùng và giới hạn sức chứa.
- Xóa đăng ký liên quan trong transaction khi xóa học viên hoặc khóa học.

## Công nghệ

- Java 21
- Spring Context 7.0.10
- Spring JDBC 7.0.10 (`JdbcTemplate`)
- Spring Transaction (`DataSourceTransactionManager`, `@Transactional`)
- H2 Database 2.5.250, lưu trữ trên file
- JUnit Jupiter 6.0.3
- Maven

## Yêu cầu

- JDK 21
- Maven

## Chạy ứng dụng

Mở dự án bằng IntelliJ IDEA, tải lại cấu hình Maven, sau đó chạy `src/main/java/org/example/school/Main.java`.

Ứng dụng hiển thị menu console. Nhập số chức năng từ `0` đến `12`; chọn `0` để thoát.

## Chạy kiểm thử

```bash
mvn test
```

## Cơ sở dữ liệu

- Cấu hình kết nối nằm trong `AppConfig`.
- H2 dùng URL `jdbc:h2:file:./data/school`.
- Bảng được khởi tạo từ `src/main/resources/schema.sql`.
- Dữ liệu nằm trong thư mục `data/` theo working directory và được Git bỏ qua.

## Cấu trúc mã nguồn

```text
src/
├── main/
│   ├── java/org/example/school/
│   │   ├── AppConfig.java
│   │   ├── Main.java
│   │   ├── Student.java, Course.java, Enrollment.java
│   │   ├── *Repository.java
│   │   └── *Service.java
│   └── resources/schema.sql
└── test/java/org/example/school/EnrollmentServiceTest.java
```

## Hướng phát triển

Bước tiếp theo là thêm Spring MVC theo cách cấu hình thủ công: Servlet container, `DispatcherServlet`, cấu hình MVC và controller. Dự án không dùng Spring Boot.
