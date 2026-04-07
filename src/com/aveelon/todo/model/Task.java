package com.aveelon.todo.model;

public class Task {
    private final int id;
    private String title;
    private TaskStatus status;

    public Task(int id, String title) {
        this(id, title, TaskStatus.NEW);
    }

    public Task(int id, String title, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%-4d | %-40s | %s", id, title, status);
    }
}
