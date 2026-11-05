package org.example.school.repository;

import org.example.school.dto.EnrollmentView;
import org.example.school.model.Course;
import org.example.school.model.Enrollment;
import org.example.school.model.Student;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnrollmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public EnrollmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Enrollment> findAll() {
        return jdbcTemplate.query(
                "SELECT student_id, course_id FROM enrollment ORDER BY student_id DESC",
                ((rs, rowNum) -> new Enrollment(rs.getLong("student_id"),
                        rs.getLong("course_id")))
        );
    }

    public List<EnrollmentView> findAllViews() {
        return jdbcTemplate.query(
                "SELECT e.student_id, s.full_name, e.course_id, c.title, c.capacity " +
                        "FROM enrollment e " +
                        "JOIN student s ON s.id = e.student_id " +
                        "JOIN course c ON c.id = e.course_id " +
                        "ORDER BY e.student_id ASC, e.course_id DESC",
                (rs, rowNum) -> new EnrollmentView(
                        new Student(rs.getLong("student_id"), rs.getString("full_name")),
                        new Course(rs.getLong("course_id"), rs.getString("title"),
                                rs.getInt("capacity"))
                )
        );
    }

    public void save(Enrollment enrollment) {
        jdbcTemplate.update(
                "INSERT INTO enrollment (student_id, course_id) VALUES ( ?, ? )",
                enrollment.getStudentId(),
                enrollment.getCourseId()
        );
    }

    public boolean isExists(long courseId, long studentId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM enrollment WHERE student_id = ? AND course_id = ?",
                Integer.class,
                studentId,
                courseId
        );
        return count != null && count > 0;
    }

    public int countByCourseId(long courseId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM enrollment WHERE course_id = ?",
                Integer.class,
                courseId
        );
        return count == null ? 0 : count;
    }

    public int countByStudentId(long studentId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM enrollment WHERE student_id = ?",
                Integer.class,
                studentId
        );
        return count == null ? 0 : count;
    }

    public void delete(long studentId, long courseId) {
        jdbcTemplate.update(
                "DELETE FROM enrollment WHERE student_id = ? AND course_id = ?",
                studentId,
                courseId
        );
    }

    public List<Enrollment> findByCourseId(long courseId) {
        /*List<Enrollment> enrollmentList = enrollments.stream().filter(
                enrollment -> enrollment.getCourseId() == courseId
        ).toList();
        return List.copyOf(enrollmentList);*/
        return jdbcTemplate.query(
                "SELECT student_id, course_id FROM enrollment WHERE course_id = ? ORDER BY student_id",
                ((rs, rowNum) -> new Enrollment(rs.getLong("student_id"),
                        rs.getLong("course_id"))),
                courseId
        );
    }

    public List<Enrollment> findByStudentId(long studentId) {
        /*List<Enrollment> enrollmentList = enrollments.stream().filter(
                enrollment -> enrollment.getStudentId() == studentId
        ).toList();
        return List.copyOf(enrollmentList);*/

        return jdbcTemplate.query(
                "SELECT student_id, course_id FROM enrollment WHERE student_id = ? ORDER BY course_id",
                ((rs, rowNum) -> new Enrollment(rs.getLong("student_id"),
                        rs.getLong("course_id"))),
                studentId
        );
    }
}
