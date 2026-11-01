package org.example.school;

public class Enrollment {
    private final long studentId;
    private final long courseId;

    public Enrollment(long studentId, long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public long getCourseId() {
        return courseId;
    }

    public long getStudentId() {
        return studentId;
    }

    @Override
    public String toString() {
        return studentId + " - " + courseId;
    }
}
