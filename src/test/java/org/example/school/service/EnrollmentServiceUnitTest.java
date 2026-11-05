package org.example.school.service;

import org.example.school.dto.EnrollmentView;
import org.example.school.model.Course;
import org.example.school.model.Enrollment;
import org.example.school.model.Student;
import org.example.school.repository.CourseRepository;
import org.example.school.repository.EnrollmentRepository;
import org.example.school.repository.StudentRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EnrollmentServiceUnitTest {
    private final EnrollmentRepository enrollments = mock(EnrollmentRepository.class);
    private final StudentRepository students = mock(StudentRepository.class);
    private final CourseRepository courses = mock(CourseRepository.class);
    private final EnrollmentService service = new EnrollmentService(enrollments, students, courses);

    @Test
    void returnsAllEnrollments() {
        List<Enrollment> expected = List.of(new Enrollment(1, 10));
        when(enrollments.findAll()).thenReturn(expected);

        assertEquals(expected, service.getEnrollmentAll());
        verify(enrollments).findAll();
    }

    @Test
    void savesEnrollmentWhenStudentCourseAndCapacityAreValid() {
        Enrollment enrollment = new Enrollment(1, 10);
        when(students.existsById(1)).thenReturn(true);
        when(courses.existsById(10)).thenReturn(true);
        when(enrollments.isExists(10, 1)).thenReturn(false);
        when(enrollments.countByCourseId(10)).thenReturn(2);
        when(courses.getById(10)).thenReturn(new Course(10, "Java", 3));

        service.addEnrollment(enrollment);

        verify(enrollments).save(enrollment);
    }

    @Test
    void rejectsEnrollmentForMissingStudent() {
        when(students.existsById(99)).thenReturn(false);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.addEnrollment(new Enrollment(99, 10)));

        assertEquals("Học viên 99 không tồn tại.", error.getMessage());
        verify(courses, never()).existsById(10);
        verify(enrollments, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void rejectsEnrollmentForMissingCourse() {
        when(students.existsById(1)).thenReturn(true);
        when(courses.existsById(99)).thenReturn(false);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.addEnrollment(new Enrollment(1, 99)));

        assertEquals("Khoá học 99 không tồn tại.", error.getMessage());
        verify(enrollments, never()).isExists(99, 1);
        verify(enrollments, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void rejectsDuplicateEnrollment() {
        when(students.existsById(1)).thenReturn(true);
        when(courses.existsById(10)).thenReturn(true);
        when(enrollments.isExists(10, 1)).thenReturn(true);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.addEnrollment(new Enrollment(1, 10)));

        assertEquals("Học viên 1 đã đăng ký khoá học 10 trước đó rồi.", error.getMessage());
        verify(enrollments, never()).countByCourseId(10);
        verify(enrollments, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void rejectsEnrollmentWhenCourseIsFull() {
        when(students.existsById(1)).thenReturn(true);
        when(courses.existsById(10)).thenReturn(true);
        when(enrollments.isExists(10, 1)).thenReturn(false);
        when(enrollments.countByCourseId(10)).thenReturn(2);
        when(courses.getById(10)).thenReturn(new Course(10, "Java", 2));

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.addEnrollment(new Enrollment(1, 10)));

        assertEquals("Khoá 10 đã đạt giới hạn", error.getMessage());
        verify(enrollments, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void returnsEnrollmentsForStudent() {
        List<Enrollment> expected = List.of(new Enrollment(1, 10), new Enrollment(1, 11));
        when(students.existsById(1)).thenReturn(true);
        when(enrollments.findByStudentId(1)).thenReturn(expected);

        assertEquals(expected, service.getEnrollmentByStudentId(1));
    }

    @Test
    void rejectsEnrollmentLookupForMissingStudent() {
        when(students.existsById(99)).thenReturn(false);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.getEnrollmentByStudentId(99));

        assertEquals("Học viên 99 không tồn tại.", error.getMessage());
        verify(enrollments, never()).findByStudentId(99);
    }

    @Test
    void mapsStudentEnrollmentsToCourses() {
        when(students.existsById(1)).thenReturn(true);
        when(enrollments.findByStudentId(1)).thenReturn(List.of(
                new Enrollment(1, 10), new Enrollment(1, 11)));
        Course java = new Course(10, "Java", 30);
        Course mvc = new Course(11, "Spring MVC", 25);
        when(courses.getById(10)).thenReturn(java);
        when(courses.getById(11)).thenReturn(mvc);

        assertEquals(List.of(java, mvc), service.getCoursesByStudentId(1));
    }

    @Test
    void returnsEnrollmentCountForCourse() {
        when(enrollments.countByCourseId(10)).thenReturn(12);

        assertEquals(12, service.countByCourseId(10));
    }

    @Test
    void cancelsEnrollmentAfterCheckingBothEntities() {
        when(students.existsById(1)).thenReturn(true);
        when(courses.existsById(10)).thenReturn(true);

        service.cancelEnrollment(1, 10);

        verify(enrollments).delete(1, 10);
    }

    @Test
    void rejectsCancellationWhenStudentDoesNotExist() {
        when(students.existsById(99)).thenReturn(false);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.cancelEnrollment(99, 10));

        assertEquals("Học viên 99 không tồn tại.", error.getMessage());
        verify(enrollments, never()).delete(99, 10);
    }

    @Test
    void rejectsCancellationWhenCourseDoesNotExist() {
        when(students.existsById(1)).thenReturn(true);
        when(courses.existsById(99)).thenReturn(false);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.cancelEnrollment(1, 99));

        assertEquals("Khoá học 99 không tồn tại.", error.getMessage());
        verify(enrollments, never()).delete(1, 99);
    }

    @Test
    void returnsJoinedEnrollmentViewsFromRepository() {
        List<EnrollmentView> expected = List.of(new EnrollmentView(
                new Student(1, "An"), new Course(10, "Java", 30)));
        when(enrollments.findAllViews()).thenReturn(expected);

        assertEquals(expected, service.getEnrollmentViewList());
        verify(enrollments).findAllViews();
    }
}
