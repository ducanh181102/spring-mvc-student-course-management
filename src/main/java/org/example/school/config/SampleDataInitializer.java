package org.example.school.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

public class SampleDataInitializer implements InitializingBean {

    private static final String SEED_KEY = "student-course-sample-data-v1";
    private static final int STUDENT_COUNT = 200;
    private static final int COURSE_COUNT = 30;
    private static final int ENROLLMENT_COUNT = 1_000;

    private static final String[] FAMILY_NAMES = {
            "Nguyễn", "Trần", "Lê", "Phạm", "Hoàng",
            "Huỳnh", "Phan", "Vũ", "Võ", "Đặng"
    };

    private static final String[] MIDDLE_NAMES = {
            "Văn", "Thị", "Minh", "Ngọc", "Thanh",
            "Quốc", "Gia", "Hữu", "Thảo", "Đức"
    };

    private static final String[] GIVEN_NAMES = {
            "An", "Bình", "Chi", "Dũng", "Anh", "Hà", "Hải", "Huy", "Khánh", "Lan",
            "Linh", "Long", "Mai", "Nam", "Ngân", "Phúc", "Quân", "Tâm", "Trang", "Tuấn"
    };

    private static final String[] COURSE_TITLES = {
            "Lập trình Java cơ bản",
            "Lập trình hướng đối tượng",
            "Cấu trúc dữ liệu và giải thuật",
            "Cơ sở dữ liệu",
            "SQL nâng cao",
            "Phát triển ứng dụng Web",
            "HTML và CSS",
            "JavaScript căn bản",
            "Spring Framework",
            "Spring MVC",
            "Thiết kế REST API",
            "Kiểm thử phần mềm",
            "Git và GitHub",
            "Linux căn bản",
            "Mạng máy tính",
            "Hệ điều hành",
            "Phân tích thiết kế hệ thống",
            "UML và thiết kế phần mềm",
            "Lập trình Python",
            "Lập trình C căn bản",
            "Lập trình C++",
            "Kiến trúc phần mềm",
            "Bảo mật ứng dụng Web",
            "DevOps căn bản",
            "Docker căn bản",
            "Thiết kế giao diện người dùng",
            "Trí tuệ nhân tạo nhập môn",
            "Phân tích dữ liệu",
            "Tiếng Anh chuyên ngành CNTT",
            "Đồ án phát triển phần mềm"
    };

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public SampleDataInitializer(JdbcTemplate jdbcTemplate,
                                 PlatformTransactionManager transactionManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public void afterPropertiesSet() {
        transactionTemplate.executeWithoutResult(status -> seedIfNeeded());
    }

    private void seedIfNeeded() {
        Integer alreadySeeded = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sample_data_seed WHERE seed_key = ?",
                Integer.class,
                SEED_KEY
        );
        if (alreadySeeded != null && alreadySeeded > 0) {
            return;
        }

        Long currentMaxStudentId = jdbcTemplate.queryForObject(
                "SELECT MAX(id) FROM student", Long.class);
        Long currentMaxCourseId = jdbcTemplate.queryForObject(
                "SELECT MAX(id) FROM course", Long.class);
        long firstStudentId = Math.max(24_000L,
                currentMaxStudentId == null ? 0L : currentMaxStudentId) + 1;
        long firstCourseId = Math.max(100L,
                currentMaxCourseId == null ? 0L : currentMaxCourseId) + 1;

        seedStudents(firstStudentId);
        seedCourses(firstCourseId);
        seedEnrollments(firstStudentId, firstCourseId);

        jdbcTemplate.update(
                "INSERT INTO sample_data_seed (seed_key) VALUES (?)",
                SEED_KEY
        );
    }

    private void seedStudents(long firstStudentId) {
        List<Object[]> students = new ArrayList<>(STUDENT_COUNT);
        for (int index = 0; index < STUDENT_COUNT; index++) {
            int group = index / 100;
            int withinGroup = index % 100;
            int familyIndex = withinGroup % FAMILY_NAMES.length;
            int middleIndex = withinGroup / FAMILY_NAMES.length;
            int givenIndex = familyIndex + group * FAMILY_NAMES.length;
            String fullName = FAMILY_NAMES[familyIndex] + " "
                    + MIDDLE_NAMES[middleIndex] + " "
                    + GIVEN_NAMES[givenIndex];
            students.add(new Object[]{firstStudentId + index, fullName});
        }

        jdbcTemplate.batchUpdate(
                "INSERT INTO student (id, full_name) VALUES (?, ?)",
                students
        );
    }

    private void seedCourses(long firstCourseId) {
        List<Object[]> courses = new ArrayList<>(COURSE_COUNT);
        for (int index = 0; index < COURSE_COUNT; index++) {
            int capacity = index < 10 ? 34 : 40;
            courses.add(new Object[]{firstCourseId + index, COURSE_TITLES[index], capacity});
        }

        jdbcTemplate.batchUpdate(
                "INSERT INTO course (id, title, capacity) VALUES (?, ?, ?)",
                courses
        );
    }

    private void seedEnrollments(long firstStudentId, long firstCourseId) {
        List<Object[]> enrollments = new ArrayList<>(ENROLLMENT_COUNT);
        for (int courseIndex = 0; courseIndex < COURSE_COUNT; courseIndex++) {
            int studentsInCourse = courseIndex < 10 ? 34 : 33;
            for (int slot = 0; slot < studentsInCourse; slot++) {
                int studentIndex = (courseIndex * 17 + slot * 37) % STUDENT_COUNT;
                enrollments.add(new Object[]{
                        firstStudentId + studentIndex,
                        firstCourseId + courseIndex
                });
            }
        }

        if (enrollments.size() != ENROLLMENT_COUNT) {
            throw new IllegalStateException("Số lượt đăng ký mẫu phải bằng 1.000.");
        }

        jdbcTemplate.batchUpdate(
                "INSERT INTO enrollment (student_id, course_id) VALUES (?, ?)",
                enrollments
        );
    }
}
