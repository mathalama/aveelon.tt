package com.aveelon.todo;

import com.aveelon.todo.repository.TaskRepository;
import com.aveelon.todo.service.TaskManager;
import com.aveelon.todo.ui.ConsoleApp;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path storagePath = Paths.get("tasks.txt");
        TaskRepository repository = new TaskRepository(storagePath);
        TaskManager taskManager = new TaskManager(repository);
        ConsoleApp consoleApp = new ConsoleApp(taskManager);

        consoleApp.start();
    }
}
