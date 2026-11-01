package org.example.school;

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


}
