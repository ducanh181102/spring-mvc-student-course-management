# Spring MVC Student Course Management

Ứng dụng web quản lý sinh viên, khóa học và việc đăng ký khóa học. Dự án sử dụng Spring MVC cấu hình thủ công, không dùng Spring Boot; các trang được render bằng JSP.

## Chức năng

- **Sinh viên:** xem danh sách, tìm kiếm/lọc, xem thông tin, thêm, sửa tên và xóa.
- **Khóa học:** xem danh sách, tìm kiếm/lọc, xem thông tin, thêm, sửa tên và sức chứa, xóa.
- **Đăng ký khóa học:** xem lượt đăng ký, thêm đăng ký và hủy đăng ký.
- Kiểm tra sinh viên/khóa học tồn tại, ngăn đăng ký trùng và từ chối đăng ký khi khóa học đã đủ chỗ.
- Khi xóa sinh viên hoặc khóa học, các đăng ký liên quan được xử lý trong transaction.
- **Cài đặt giao diện:** đổi theme và ngôn ngữ; lựa chọn được lưu ở trình duyệt.
- Trang lỗi dùng chung để hiển thị lỗi nghiệp vụ.

## Công nghệ

- Java 21
- Maven
- Spring Framework 7.0.10: Spring Context, Spring MVC, Spring JDBC và Spring Transaction
- Jakarta Servlet API 6.1.0; chạy trên Servlet container như Apache Tomcat 11
- JSP và Jakarta JSTL 3.0.1
- H2 Database 2.5.250, lưu trên file
- JUnit Jupiter 6.0.3, Spring Test 7.0.10 và Mockito 5.24.0
- HTML, CSS và JavaScript

## Cấu trúc ứng dụng

```text
src/
├── main/
│   ├── java/org/example/school/
│   │   ├── config/       cấu hình Spring, Servlet, database và dữ liệu mẫu
│   │   ├── controller/   nhận request và chọn trang trả về
│   │   ├── dto/          dữ liệu tổng hợp để hiển thị
│   │   ├── model/        Student, Course, Enrollment
│   │   ├── repository/   truy vấn database bằng JdbcTemplate
│   │   └── service/      quy tắc nghiệp vụ
│   ├── resources/schema.sql
│   └── webapp/
│       ├── WEB-INF/views/ JSP và các fragment dùng chung
│       ├── css/           kiểu giao diện
│       └── js/            tương tác, theme và ngôn ngữ
└── test/java/             unit test, test MVC và test tích hợp
```

Luồng xử lý chính:

```text
Trình duyệt → Tomcat → DispatcherServlet → Controller
           → Service → Repository/JdbcTemplate → H2
           → Controller/Model → JSP → HTML trả về trình duyệt
```

## Đường dẫn chính

- `/students` — quản lý sinh viên
- `/courses` — quản lý khóa học
- `/enrollments` — quản lý đăng ký
- `/settings` — cài đặt giao diện

Các đường dẫn này nằm bên trong context path do Tomcat cấp cho ứng dụng.

## Database và dữ liệu mẫu

- Cấu hình database và Spring bean nằm trong `src/main/java/org/example/school/config/AppConfig.java`.
- H2 dùng URL `jdbc:h2:file:./data/school`; vị trí tương đối với working directory của tiến trình chạy ứng dụng.
- Các bảng được tạo từ `src/main/resources/schema.sql`.
- Lần khởi động đầu, ứng dụng tạo 200 sinh viên, 30 khóa học và 1.000 lượt đăng ký mẫu. Bảng `sample_data_seed` ghi nhận dữ liệu đã được tạo để tránh thêm lặp ở lần khởi động sau.
- Thư mục `data/` chứa database local và được Git bỏ qua.

## Chạy ứng dụng

Yêu cầu JDK 21, Maven và Apache Tomcat 11. Mở dự án dưới dạng Maven project, build WAR:

```bash
mvn clean package
```

WAR được tạo tại:

```text
target/spring-mvc-student-course-management-1.0-SNAPSHOT.war
```

Triển khai WAR lên Tomcat rồi mở ứng dụng theo context path mà Tomcat gán cho WAR.

## Chạy kiểm thử

```bash
mvn test
```

Test bao gồm unit test cho service với repository mock, test luồng Spring MVC và test tích hợp service với database.
