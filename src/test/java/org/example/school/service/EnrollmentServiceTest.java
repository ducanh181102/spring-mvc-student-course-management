package org.example.school.service;

import org.example.school.model.Course;
import org.example.school.model.Enrollment;
import org.example.school.model.Student;
import org.example.school.repository.CourseRepository;
import org.example.school.repository.EnrollmentRepository;
import org.example.school.repository.StudentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnrollmentServiceTest {
    private StudentRepository studentRepository;
    private CourseRepository courseRepository;
    private EnrollmentRepository enrollmentRepository;
    private EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:enrollment_test_" + UUID.randomUUID().toString().replace("-", "")
                + ";DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        DataSource source = dataSource;
        DatabasePopulatorUtils.execute(
                new ResourceDatabasePopulator(new ClassPathResource("schema.sql")),
                source
        );

        JdbcTemplate jdbcTemplate = new JdbcTemplate(source);
        studentRepository = new StudentRepository(jdbcTemplate);
        courseRepository = new CourseRepository(jdbcTemplate);
        enrollmentRepository = new EnrollmentRepository(jdbcTemplate);
        enrollmentService = new EnrollmentService(
                enrollmentRepository,
                studentRepository,
                courseRepository
        );

        studentRepository.save(new Student(1, "An"));
        studentRepository.save(new Student(2, "Bình"));
        courseRepository.save(new Course(10, "Java", 2));
    }

    @Test
    void addsValidEnrollmentToDatabase() {
        enrollmentService.addEnrollment(new Enrollment(1, 10));

        assertTrue(enrollmentRepository.isExists(10, 1));
        assertEquals(1, enrollmentRepository.countByCourseId(10));
    }

    @Test
    void rejectsDuplicateEnrollment() {
        enrollmentService.addEnrollment(new Enrollment(1, 10));

        assertThrows(
                IllegalArgumentException.class,
                () -> enrollmentService.addEnrollment(new Enrollment(1, 10))
        );
        assertEquals(1, enrollmentRepository.countByCourseId(10));
    }

    @Test
    void rejectsEnrollmentWhenCourseIsFull() {
        courseRepository.update(10, 1);
        enrollmentService.addEnrollment(new Enrollment(1, 10));

        assertThrows(
                IllegalArgumentException.class,
                () -> enrollmentService.addEnrollment(new Enrollment(2, 10))
        );
        assertEquals(1, enrollmentRepository.countByCourseId(10));
    }

    @Test
    void rejectsEnrollmentForUnknownStudent() {
        assertThrows(
                IllegalArgumentException.class,
                () -> enrollmentService.addEnrollment(new Enrollment(99, 10))
        );
        assertEquals(0, enrollmentRepository.countByCourseId(10));
    }

    @Test
    void rejectsEnrollmentForUnknownCourse() {
        assertThrows(
                IllegalArgumentException.class,
                () -> enrollmentService.addEnrollment(new Enrollment(1, 99))
        );
        assertEquals(0, enrollmentRepository.countByStudentId(1));
    }
}
