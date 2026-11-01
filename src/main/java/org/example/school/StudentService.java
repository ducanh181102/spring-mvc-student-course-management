package org.example.school;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public StudentService(StudentRepository studentRepository, EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void addStudent(Student student) {
        if (studentRepository.existsById(student.getId())) {
            String msg = "Học viên " + student.getId() + " đã tồn tại.";
            throw new IllegalArgumentException(msg);
        }

        studentRepository.save(student);
    }

    public void updateStudent(long id, String name) {
        if (!studentRepository.existsById(id)) {
            String msg = "Học viên " + id + " không tồn tại.";
            throw new IllegalArgumentException(msg);
        }

        studentRepository.update(id, name);
    }

    @Transactional
    public void deleteStudent(long id) {
        if (!studentRepository.existsById(id)) {
            String msg = "Học viên " + id + " không tồn tại.";
            throw new IllegalArgumentException(msg);
        }

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(id);
        enrollments.forEach(enrollment -> enrollmentRepository.delete(enrollment.getStudentId(),
                enrollment.getCourseId()));

        if (enrollmentRepository.countByStudentId(id) == 0) studentRepository.delete(id);
    }
}
