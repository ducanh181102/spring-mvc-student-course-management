package org.example.school;

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
                "SELECT id, full_name FROM student ORDER BY id",
                (rs, rowNum) -> new Student(rs.getLong("id"),
                        rs.getString("full_name")));
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
}
