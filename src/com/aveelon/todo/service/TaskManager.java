package com.aveelon.todo.service;

import com.aveelon.todo.model.Task;
import com.aveelon.todo.model.TaskStatus;
import com.aveelon.todo.repository.TaskRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskManager {
    private final TaskRepository repository;
    private final List<Task> tasks;
    private int nextId;

    public TaskManager(TaskRepository repository) {
        this.repository = repository;
        this.tasks = new ArrayList<>(repository.loadTasks());
        this.nextId = calculateNextId();
    }

    public Task addTask(String title) {
        Task task = new Task(nextId++, title.trim());
        tasks.add(task);
        repository.saveTasks(tasks);
        return task;
    }

    public List<Task> getAllTasks() {
        List<Task> copy = new ArrayList<>(tasks);
        copy.sort(Comparator.comparingInt(Task::getId));
        return Collections.unmodifiableList(copy);
    }

    public boolean updateTaskStatus(int id, TaskStatus status) {
        Task task = findTaskById(id);
        if (task == null) {
            return false;
        }

        task.setStatus(status);
        repository.saveTasks(tasks);
        return true;
    }

    public boolean deleteTask(int id) {
        Task task = findTaskById(id);
        if (task == null) {
            return false;
        }

        tasks.remove(task);
        repository.saveTasks(tasks);
        return true;
    }

    private Task findTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    private int calculateNextId() {
        int maxId = 0;
        for (Task task : tasks) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        return maxId + 1;
    }
}
