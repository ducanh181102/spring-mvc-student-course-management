package org.example.school.service;

import org.example.school.dto.EnrollmentView;
import org.example.school.model.Course;
import org.example.school.model.Enrollment;
import org.example.school.model.Student;
import org.example.school.repository.CourseRepository;
import org.example.school.repository.EnrollmentRepository;
import org.example.school.repository.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<Enrollment> getEnrollmentAll() {
        return enrollmentRepository.findAll();
    }

    public void addEnrollment(Enrollment enrollment) {
        if (!this.studentRepository.existsById(enrollment.getStudentId())) {
            throw new IllegalArgumentException("Học viên " +
                    enrollment.getStudentId() + " không tồn tại.");
        }

        if (!this.courseRepository.existsById(enrollment.getCourseId())) {
            throw new IllegalArgumentException("Khoá học " +
                    enrollment.getCourseId() + " không tồn tại.");
        }

        if (this.enrollmentRepository.isExists(enrollment.getCourseId(), enrollment.getStudentId())) {
            throw new IllegalArgumentException("Học viên " +
                    enrollment.getStudentId() + " đã đăng ký khoá học " +
                    enrollment.getCourseId() + " trước đó rồi.");
        }

        if (this.enrollmentRepository.countByCourseId(enrollment.getCourseId()) >= courseRepository.getById(enrollment.getCourseId()).getCapacity()) {
            throw new IllegalArgumentException("Khoá " +
                    enrollment.getCourseId() + " đã đạt giới hạn");
        }

        this.enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getEnrollmentByStudentId(long studentId) {
        if (!this.studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("Học viên " +
                    studentId + " không tồn tại.");
        }
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Course> getCoursesByStudentId(long studentId) {
        if (!this.studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("Học viên " +
                    studentId + " không tồn tại.");
        }
        return getEnrollmentByStudentId(studentId).stream()
                .map(enrollment -> courseRepository
                        .getById(enrollment.getCourseId())).toList();
    }

    public List<Course> getEnrollmentedAllCourses() {
        return getEnrollmentAll().stream().map(enrollment -> courseRepository.getById(enrollment.getCourseId())).toList();
    }

    public List<Student> getEnrollmentedAllStudents() {
        return getEnrollmentAll().stream().map(enrollment -> studentRepository.findById(enrollment.getStudentId())).toList();
    }

    public int countByCourseId(long courseId) {
        return enrollmentRepository.countByCourseId(courseId);
    }

    public void cancelEnrollment(long studentId, long courseId) {
        if (!this.studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("Học viên " +
                    studentId + " không tồn tại.");
        }

        if (!this.courseRepository.existsById(courseId)) {
            throw new IllegalArgumentException("Khoá học " +
                    courseId + " không tồn tại.");
        }

        this.enrollmentRepository.delete(studentId, courseId);
    }

    public List<EnrollmentView> getEnrollmentViewList() {
        return enrollmentRepository.findAllViews();
    }

}
