package org.example.school.service;

import org.example.school.model.Enrollment;
import org.example.school.model.Student;
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

class StudentServiceUnitTest {
    private final StudentRepository students = mock(StudentRepository.class);
    private final EnrollmentRepository enrollments = mock(EnrollmentRepository.class);
    private final StudentService service = new StudentService(students, enrollments);

    @Test
    void returnsAllStudentsFromRepository() {
        List<Student> expected = List.of(new Student(1, "An"), new Student(2, "Bình"));
        when(students.findAll()).thenReturn(expected);

        assertEquals(expected, service.getAllStudents());
        verify(students).findAll();
    }

    @Test
    void returnsStudentById() {
        Student expected = new Student(7, "Nguyễn Minh Anh");
        when(students.findById(7)).thenReturn(expected);

        assertEquals(expected, service.getStudent(7));
        verify(students).findById(7);
    }

    @Test
    void returnsNextStudentId() {
        when(students.getNextId()).thenReturn(42L);

        assertEquals(42L, service.getNextId());
        verify(students).getNextId();
    }

    @Test
    void savesNewStudentWhenIdIsAvailable() {
        Student student = new Student(42, "Phan Đức Anh");
        when(students.existsById(42)).thenReturn(false);

        service.addStudent(student);

        verify(students).save(student);
    }

    @Test
    void rejectsStudentWithDuplicateId() {
        when(students.existsById(1)).thenReturn(true);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.addStudent(new Student(1, "Duplicate")));

        assertEquals("Học viên 1 đã tồn tại.", error.getMessage());
        verify(students, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void updatesExistingStudentName() {
        when(students.existsById(3)).thenReturn(true);

        service.updateStudent(3, "Tên mới");

        verify(students).update(3, "Tên mới");
    }

    @Test
    void rejectsUpdateForMissingStudent() {
        when(students.existsById(99)).thenReturn(false);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.updateStudent(99, "Không tồn tại"));

        assertEquals("Học viên 99 không tồn tại.", error.getMessage());
        verify(students, never()).update(org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void deletesStudentAfterRemovingAllEnrollments() {
        when(students.existsById(5)).thenReturn(true);
        when(enrollments.findByStudentId(5)).thenReturn(List.of(
                new Enrollment(5, 101), new Enrollment(5, 102)));
        when(enrollments.countByStudentId(5)).thenReturn(0);

        service.deleteStudent(5);

        verify(enrollments).delete(5, 101);
        verify(enrollments).delete(5, 102);
        verify(students).delete(5);
    }

    @Test
    void deletesStudentWithNoEnrollments() {
        when(students.existsById(6)).thenReturn(true);
        when(enrollments.findByStudentId(6)).thenReturn(List.of());
        when(enrollments.countByStudentId(6)).thenReturn(0);

        service.deleteStudent(6);

        verify(enrollments, never()).delete(org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.anyLong());
        verify(students).delete(6);
    }

    @Test
    void rejectsDeletionForMissingStudent() {
        when(students.existsById(100)).thenReturn(false);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.deleteStudent(100));

        assertEquals("Học viên 100 không tồn tại.", error.getMessage());
        verify(enrollments, never()).findByStudentId(100);
        verify(students, never()).delete(100);
    }
}
