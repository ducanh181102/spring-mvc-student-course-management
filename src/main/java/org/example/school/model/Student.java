package org.example.school.model;


import java.util.List;

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

    public String getInitials() {
        if (fullName == null || fullName.isBlank()) {
            return "";
        }

        String[] words = fullName.trim().split("\\s+");
        if (words.length == 1) {
            return words[0].substring(0, 1).toUpperCase();
        }

        int last = words.length - 1;
        return (
                words[last - 1].charAt(0)
                        + words[last].substring(0, 1)
        ).toUpperCase();
    }

    @Override
    public String toString() {
        return id + ". " + fullName;
    }
}
