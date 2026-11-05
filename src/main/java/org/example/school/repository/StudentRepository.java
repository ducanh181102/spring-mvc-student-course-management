package org.example.school.repository;

import org.example.school.model.Student;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRepository {
    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Student> findAll() {
        return jdbcTemplate.query(
                "SELECT id, full_name FROM student ORDER BY id DESC",
                (rs, rowNum) -> new Student(rs.getLong("id"),
                        rs.getString("full_name")));
    }

    public Student findById(long id) {
        return jdbcTemplate.queryForObject(
                "SELECT id, full_name FROM student WHERE id = ?",
                (rs, rowNum) -> new Student(rs.getLong("id"),
                        rs.getString("full_name")),
                id
        );
    }

    public void save(Student student) {
        jdbcTemplate.update(
                "INSERT INTO student (id, full_name) VALUES (?, ?)",
                student.getId(), student.getFullName()
        );
    }

    public boolean existsById(long id) {

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM student WHERE id = ?",
                Integer.class,
                id);
        return count != null && count > 0;
    }

    public void update(long id, String name) {
        jdbcTemplate.update(
                "UPDATE student SET full_name = ? WHERE id = ?",
                name, id
        );
    }

    public void delete(long id) {
        jdbcTemplate.update(
                "DELETE FROM student WHERE id = ?",
                id
        );
    }

    public long getNextId() {
        Integer maxId = jdbcTemplate.queryForObject(
                "SELECT MAX(id) FROM student",
                Integer.class
        );
        return maxId != null ? maxId + 1 : 1;
    }
}
