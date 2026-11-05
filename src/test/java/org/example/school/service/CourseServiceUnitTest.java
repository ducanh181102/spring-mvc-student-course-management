package org.example.school.service;

import org.example.school.model.Course;
import org.example.school.model.Enrollment;
import org.example.school.repository.CourseRepository;
import org.example.school.repository.EnrollmentRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CourseServiceUnitTest {
    private final CourseRepository courses = mock(CourseRepository.class);
    private final EnrollmentRepository enrollments = mock(EnrollmentRepository.class);
    private final CourseService service = new CourseService(courses, enrollments);

    @Test
    void returnsAllCourses() {
        List<Course> expected = List.of(new Course(1, "Java", 30));
        when(courses.findAll()).thenReturn(expected);

        assertEquals(expected, service.getCourseList());
        verify(courses).findAll();
    }

    @Test
    void returnsCourseById() {
        Course expected = new Course(8, "Spring MVC", 25);
        when(courses.getById(8)).thenReturn(expected);

        assertEquals(expected, service.getCourse(8));
        verify(courses).getById(8);
    }

    @Test
    void returnsNextCourseId() {
        when(courses.getNextId()).thenReturn(110L);

        assertEquals(110L, service.getNextId());
        verify(courses).getNextId();
    }

    @Test
    void savesNewCourse() {
        Course course = new Course(20, "Database", 40);
        when(courses.existsById(20)).thenReturn(false);

        service.addCourse(course);

        verify(courses).save(course);
    }

    @Test
    void rejectsDuplicateCourseId() {
        when(courses.existsById(20)).thenReturn(true);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.addCourse(new Course(20, "Duplicate", 10)));

        assertEquals("Khoá học 20 đã tồn tại.", error.getMessage());
        verify(courses, never()).save(any());
    }

    @Test
    void rejectsNegativeCourseCapacity() {
        when(courses.existsById(21)).thenReturn(false);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.addCourse(new Course(21, "Invalid", -1)));

        assertEquals("Không thể tạo khoá học có số lượng âm.", error.getMessage());
        verify(courses, never()).save(any());
    }

    @Test
    void updatesCourseTitle() {
        when(courses.existsById(4)).thenReturn(true);

        service.updateTitleCourse(4, "Advanced Java");

        verify(courses).update(4, "Advanced Java");
    }

    @Test
    void rejectsTitleUpdateForMissingCourse() {
        when(courses.existsById(404)).thenReturn(false);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.updateTitleCourse(404, "Missing"));

        assertEquals("Khoá học 404 không tồn tại.", error.getMessage());
        verify(courses, never()).update(org.mockito.ArgumentMatchers.eq(404L),
                org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void updatesCapacityWhenCapacityIsAboveEnrollmentCount() {
        when(courses.existsById(4)).thenReturn(true);
        when(enrollments.countByCourseId(4)).thenReturn(3);

        service.updateCapacityCourse(4, 10);

        verify(courses).update(4, 10);
    }

    @Test
    void allowsCapacityEqualToEnrollmentCount() {
        when(courses.existsById(4)).thenReturn(true);
        when(enrollments.countByCourseId(4)).thenReturn(3);

        service.updateCapacityCourse(4, 3);

        verify(courses).update(4, 3);
    }

    @Test
    void rejectsCapacityBelowEnrollmentCount() {
        when(courses.existsById(4)).thenReturn(true);
        when(enrollments.countByCourseId(4)).thenReturn(3);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.updateCapacityCourse(4, 2));

        assertEquals("Số lượng không được nhỏ hơn số đăng ký hiện tại.", error.getMessage());
        verify(courses, never()).update(4, 2);
    }

    @Test
    void rejectsCapacityUpdateForMissingCourse() {
        when(courses.existsById(404)).thenReturn(false);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.updateCapacityCourse(404, 10));

        assertEquals("Khoá học 404 không tồn tại.", error.getMessage());
        verify(enrollments, never()).countByCourseId(404);
    }

    @Test
    void deletesCourseAfterRemovingEnrollments() {
        when(courses.existsById(8)).thenReturn(true);
        when(enrollments.findByCourseId(8)).thenReturn(List.of(
                new Enrollment(1, 8), new Enrollment(2, 8)));
        when(enrollments.countByCourseId(8)).thenReturn(0);

        service.deleteCourse(8);

        verify(enrollments).delete(1, 8);
        verify(enrollments).delete(2, 8);
        verify(courses).delete(8);
    }

    @Test
    void deletesCourseWithNoEnrollments() {
        when(courses.existsById(9)).thenReturn(true);
        when(enrollments.findByCourseId(9)).thenReturn(List.of());
        when(enrollments.countByCourseId(9)).thenReturn(0);

        service.deleteCourse(9);

        verify(enrollments, never()).delete(org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyLong());
        verify(courses).delete(9);
    }

    @Test
    void rejectsDeletionForMissingCourse() {
        when(courses.existsById(99)).thenReturn(false);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.deleteCourse(99));

        assertEquals("Khoá học 99 không tồn tại.", error.getMessage());
        verify(enrollments, never()).findByCourseId(99);
        verify(courses, never()).delete(99);
    }
}
