package org.example.school;

public class Student {
    private final long id;
    private String fullName;

    public Student(long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return id + ". " + fullName;
    }
}
