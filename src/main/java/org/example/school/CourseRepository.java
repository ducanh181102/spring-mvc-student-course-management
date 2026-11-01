package org.example.school;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseRepository {
    /*private final List<Course> courseList = new ArrayList<>();*/
    private final JdbcTemplate jdbcTemplate;

    public CourseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Course> findAll() {
        return jdbcTemplate.query(
                "SELECT id, title, capacity FROM course ORDER BY id",
                ((rs, rowNum) -> new Course(rs.getLong("id"),
                        rs.getString("title"),
                        rs.getInt("capacity")))
        );
    }

    public void save(Course course) {
        jdbcTemplate.update(
                "INSERT INTO course (id, title, capacity) VALUES ( ?,?,? )",
                course.getId(),
                course.getTitle(),
                course.getCapacity()
        );
    }

    public boolean existsById(long id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM course WHERE id = ?",
                Integer.class,
                id
        );

        return count != null && count > 0;

        /*for (Course course : courseList) {
            if (id == course.getId()) {
                return true;
            }
        }
        return false;*/
    }

    public Course getById(long id) {
        /*for (Course course : courseList) {
            if (id == course.getId()) {
                return course;
            }
        }
        return null;*/
        return jdbcTemplate.queryForObject(
                "SELECT id, title, capacity FROM course WHERE id = ?",
                ((rs, rowNum) -> new Course(rs.getLong("id"),
                        rs.getString("title"),
                        rs.getInt("capacity"))),
                id
        );
    }

    public void update(long id, String title) {
        /*for (Course course : courseList) {
            if (id == course.getId()) {
                course.setTitle(title);
            }
        }*/
        jdbcTemplate.update(
                "UPDATE course SET title = ? WHERE id = ?",
                title,
                id
        );
    }

    public void update(long id, int capacity) {
        /*for (Course course : courseList) {
            if (id == course.getId()) {
                course.setCapacity(capacity);
            }
        }*/
        jdbcTemplate.update(
                "UPDATE course SET capacity = ? WHERE id = ?",
                capacity,
                id
        );
    }

    public void delete(long id) {
        /*courseList.removeIf(course -> course.getId() == id);*/
        jdbcTemplate.update(
                "DELETE FROM course WHERE id = ?",
                id
        );
    }
}
