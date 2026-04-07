package com.aveelon.todo.repository;

import com.aveelon.todo.model.Task;
import com.aveelon.todo.model.TaskStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static final String DELIMITER = "|";
    private final Path storagePath;

    public TaskRepository(Path storagePath) {
        this.storagePath = storagePath;
    }

    public List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();

        try {
            if (!Files.exists(storagePath)) {
                Files.createFile(storagePath);
                return tasks;
            }

            List<String> lines = Files.readAllLines(storagePath, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                tasks.add(parseTask(line));
            }
        } catch (IOException e) {
            System.out.println("Не удалось загрузить задачи из файла: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Обнаружена некорректная запись в файле задач: " + e.getMessage());
        }

        return tasks;
    }

    public void saveTasks(List<Task> tasks) {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(serializeTask(task));
        }

        try {
            Files.write(storagePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Не удалось сохранить задачи в файл: " + e.getMessage());
        }
    }

    private Task parseTask(String line) {
        List<String> parts = splitPreservingEscapedDelimiter(line);
        if (parts.size() != 3) {
            throw new IllegalArgumentException("ожидалось 3 поля: " + line);
        }

        int id = Integer.parseInt(parts.get(0));
        String title = unescape(parts.get(1));
        TaskStatus status = TaskStatus.valueOf(parts.get(2));

        return new Task(id, title, status);
    }

    private String serializeTask(Task task) {
        return task.getId() + DELIMITER + escape(task.getTitle()) + DELIMITER + task.getStatus();
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace(DELIMITER, "\\|");
    }

    private String unescape(String value) {
        StringBuilder result = new StringBuilder();
        boolean escaped = false;

        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (escaped) {
                result.append(current);
                escaped = false;
            } else if (current == '\\') {
                escaped = true;
            } else {
                result.append(current);
            }
        }

        if (escaped) {
            result.append('\\');
        }

        return result.toString();
    }

    private List<String> splitPreservingEscapedDelimiter(String value) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean escaped = false;

        for (int i = 0; i < value.length(); i++) {
            char symbol = value.charAt(i);

            if (escaped) {
                current.append('\\').append(symbol);
                escaped = false;
            } else if (symbol == '\\') {
                escaped = true;
            } else if (symbol == DELIMITER.charAt(0)) {
                parts.add(current.toString());
                current.setLength(0);
            } else {
                current.append(symbol);
            }
        }

        if (escaped) {
            current.append('\\');
        }

        parts.add(current.toString());
        return parts;
    }
}
