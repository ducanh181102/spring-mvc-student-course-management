package org.example.school;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseService(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Course> getCourseList() {
        return courseRepository.findAll();
    }

    public void addCourse(Course course) {
        if (this.courseRepository.existsById(course.getId())) {
            String msg = "Khoá học " + course.getId() + " đã tồn tại.";
            throw new IllegalArgumentException(msg);
        }

        if (course.getCapacity() < 0) {
            String msg = "Không thể tạo khoá học có số lượng âm.";
            throw new IllegalArgumentException(msg);
        }

        courseRepository.save(course);
    }

    public void updateTitleCourse(long id, String title) {
        if (!this.courseRepository.existsById(id)) {
            String msg = "Khoá học " + id + " không tồn tại.";
            throw new IllegalArgumentException(msg);
        }

        courseRepository.update(id, title);
    }

    public void updateCapacityCourse(long id, int capacity) {
        if (!this.courseRepository.existsById(id)) {
            String msg = "Khoá học " + id + " không tồn tại.";
            throw new IllegalArgumentException(msg);
        }

        if (capacity < enrollmentRepository.countByCourseId(id)) {
            String msg = "Số lượng không được nhỏ hơn số đăng ký hiện tại.";
            throw new IllegalArgumentException(msg);
        }

        courseRepository.update(id, capacity);
    }

    @Transactional
    public void deleteCourse(long id) {
        if (!this.courseRepository.existsById(id)) {
            String msg = "Khoá học " + id + " không tồn tại.";
            throw new IllegalArgumentException(msg);
        }

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(id);
        enrollments.forEach(enrollment -> enrollmentRepository.delete(enrollment.getStudentId(),
                enrollment.getCourseId()));
        if (enrollmentRepository.countByCourseId(id) == 0) courseRepository.delete(id);
    }

}
