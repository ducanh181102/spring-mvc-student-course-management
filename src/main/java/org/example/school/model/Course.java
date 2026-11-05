package org.example.school.model;


public class Course {
    private final long id;
    private String title;
    private int capacity;

    public Course(long id, String title, int capacity) {
        this.id = id;
        this.title = title;
        this.capacity = capacity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getInitials() {
        if (title == null || title.isBlank()) {
            return "";
        }

        String[] words = title.trim().split("\\s+");
        if (words.length == 1) {
            return words[0].substring(0, 1).toUpperCase();
        }

        int last = words.length - 1;
        return (
                words[last].substring(0, 1)
        ).toUpperCase();
    }

    @Override
    public String toString() {
        return id + ". " + title + " - SL: " + capacity;
    }
}
