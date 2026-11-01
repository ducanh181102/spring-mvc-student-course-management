package org.example.school;

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

    @Override
    public String toString() {
        return id + ". " + title + " - SL: " + capacity;
    }
}
