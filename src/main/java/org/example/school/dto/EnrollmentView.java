package org.example.school.dto;

import org.example.school.model.Course;
import org.example.school.model.Student;

public class EnrollmentView {
    private final Student student;
    private final Course course;

    public EnrollmentView(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }
}
