package org.example.school;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner INPUT = new Scanner(System.in);


    public static void main(String[] args) {

        /*Thay the bang Spring*/
        /* StudentRepository studentRepository = new StudentRepository();
        StudentService studentService = new StudentService(studentRepository); */
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        StudentService studentService = context.getBean(StudentService.class);
        CourseService courseService = context.getBean(CourseService.class);
        EnrollmentService enrollmentService = context.getBean(EnrollmentService.class);

        boolean running = true;
        while (running) {
            System.out.println("QUẢN LÝ HỌC VIÊN - SPRING CORE");

            printMenu();
            int choice = readNumber("Chọn chức năng: ");

            try {
                switch (choice) {
                    case 1:
                        showStudents(studentService);
                        break;
                    case 2:
                        addStudent(studentService);
                        break;
                    case 3:
                        updateStudent(studentService);
                        break;
                    case 4:
                        deleteStudent(studentService);
                        break;
                    case 5:
                        showCourses(courseService);
                        break;
                    case 6:
                        addCourse(courseService);
                        break;
                    case 7:
                        updateTitleCourse(courseService);
                        break;
                    case 8:
                        updateCapacityCourse(courseService);
                        break;
                    case 9:
                        deleteCourse(courseService);
                        break;
                    case 10:
                        showEnrollment(enrollmentService);
                        break;
                    case 11:
                        addEnrollment(enrollmentService);
                        break;
                    case 12:
                        cancelEnrollment(enrollmentService);
                        break;
                    case 0:
                        running = false;
                        System.out.println("Đã thoát chương trình.");
                        break;
                    default:
                        System.out.println("Chức năng không hợp lệ. Hãy chọn từ 0 đến 12.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Không thể thực hiện: " + e.getMessage());
            }
        }

        // Giai phong Container Spring
        context.close();
    }

    public static void printMenu() {
        System.out.println("Học viên");
        System.out.println("1. Xem danh sách");
        System.out.println("2. Thêm học viên");
        System.out.println("3. Sửa thông tin");
        System.out.println("4. Xóa học viên");

        System.out.println("Khóa học");
        System.out.println("5. Xem danh sách");
        System.out.println("6. Thêm khóa học");
        System.out.println("7. Sửa tên của khóa học");
        System.out.println("8. Sửa số lượng của khóa học");
        System.out.println("9. Xóa khóa học");

        System.out.println("Đăng ký");
        System.out.println("10. Xem danh sách");
        System.out.println("11. Đăng ký học viên vào khóa học");
        System.out.println("12. Hủy đăng ký");

        System.out.println("------");
        System.out.println("0. Thoát");
    }

    private static int readNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = INPUT.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                System.out.println("Hãy nhập một số nguyên.");
            }
        }
    }

    private static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = INPUT.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            } else {
                System.out.println("Không để trống.");
            }
        }
    }

    private static void showStudents(StudentService studentService) {
        System.out.println("DANH SÁCH HỌC VIÊN: ");
        for (Student student : studentService.getAllStudents()) {
            System.out.println(student.toString());
        }
    }

    private static void showCourses(CourseService courseService) {
        System.out.println("DANH SÁCH KHOÁ HỌC: ");
        for (Course course : courseService.getCourseList()) {
            System.out.println(course.toString());
        }
    }

    private static void showEnrollment(EnrollmentService enrollmentService) {
        System.out.println("DANH SÁCH ĐĂNG KÝ: ");
        for (Enrollment enrollment : enrollmentService.getEnrollmentAll()) {
            System.out.println(enrollment.toString());
        }
    }

    private static void addStudent(StudentService studentService) throws IllegalArgumentException {
        System.out.println("THÊM HỌC VIÊN: ");
        String title = readString("Nhập tên học viên: ");
        studentService.addStudent(getNewStudent(title, studentService));
        System.out.println("Đã thêm học viên thành công.");
    }

    private static void addCourse(CourseService courseService) {
        System.out.println("THÊM KHOÁ HỌC: ");
        String title = readString("Nhập tên khoá học: ");
        int capacity = readNumber("Nhập số lượng: ");
        courseService.addCourse(getNewCourse(title, capacity, courseService));
        System.out.println("Đã thêm khoá học thành công.");
    }

    private static void addEnrollment(EnrollmentService enrollmentService) {
        System.out.println("ĐĂNG KÝ KHOÁ HỌC: ");
        long studentId = readNumber("Nhập mã học viên: ");
        long courseId = readNumber("Nhập mã khoá học: ");
        enrollmentService.addEnrollment(getNewEnrollment(studentId, courseId));
        System.out.println("Đã đăng ký khoá học thành công.");
    }

    private static void updateStudent(StudentService studentService) {
        System.out.println("CẬP NHẬT THÔNG TIN HỌC VIÊN: ");
        long studentId = readNumber("Nhập mã học viên: ");
        String name = readString("Nhập tên học viên: ");
        studentService.updateStudent(studentId, name);
        System.out.println("Đã chỉnh sửa thông tin học viên thành công.");
    }

    private static void updateTitleCourse(CourseService courseService) {
        System.out.println("CẬP NHẬT THÔNG TIN KHOÁ HỌC: ");
        long courseId = readNumber("Nhập mã khoá học: ");
        String title = readString("Nhập tên khoá học: ");
        courseService.updateTitleCourse(courseId, title);
        System.out.println("Đã chỉnh sửa thông tin khoá học thành công.");
    }

    private static void updateCapacityCourse(CourseService courseService) {
        System.out.println("CẬP NHẬT THÔNG TIN KHOÁ HỌC: ");
        long courseId = readNumber("Nhập mã khoá học: ");
        int capacity = readNumber("Nhập số lượng: ");
        courseService.updateCapacityCourse(courseId, capacity);
        System.out.println("Đã chỉnh sửa thông tin khoá học thành công.");
    }

    private static void deleteStudent(StudentService studentService) {
        System.out.println("XOÁ HỌC VIÊN: ");
        long studentId = readNumber("Nhập mã học viên: ");
        studentService.deleteStudent(studentId);
        System.out.println("Đã xoá học viên thành công.");
    }

    private static void deleteCourse(CourseService courseService) {
        System.out.println("XOÁ KHOÁ HỌC: ");
        long courseId = readNumber("Nhập mã khoá học: ");
        courseService.deleteCourse(courseId);
        System.out.println("Đã xoá khoá học thành công.");
    }

    private static void cancelEnrollment(EnrollmentService enrollmentService) {
        System.out.println("HUỶ ĐĂNG KÝ: ");
        long studentId = readNumber("Nhập mã học viên: ");
        long courseId = readNumber("Nhập mã khoá học: ");
        enrollmentService.cancelEnrollment(studentId, courseId);
        System.out.println("Đã huỷ đăng ký thành công.");
    }

    private static Student getNewStudent(String title, StudentService studentService) {
        return new Student(studentService.getAllStudents().stream()
                .mapToLong(Student::getId)
                .max().orElse(0L) + 1, title);
    }

    private static Course getNewCourse(String title, int capacity, CourseService courseService) {
        return new Course(courseService.getCourseList().stream()
                .mapToLong(Course::getId)
                .max().orElse(0L) + 1, title, capacity);
    }

    private static Enrollment getNewEnrollment(long studentId, long courseId) {
        return new Enrollment(studentId, courseId);
    }
}
