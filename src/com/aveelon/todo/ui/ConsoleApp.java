package com.aveelon.todo.ui;

import com.aveelon.todo.model.Task;
import com.aveelon.todo.model.TaskStatus;
import com.aveelon.todo.service.TaskManager;

import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private final TaskManager taskManager;
    private final Scanner scanner;

    public ConsoleApp(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== To-Do List ===");
        System.out.println("Задачи загружены из файла tasks.txt");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addTask();
                    break;
                case "2":
                    showAllTasks();
                    break;
                case "3":
                    updateTaskStatus();
                    break;
                case "4":
                    deleteTask();
                    break;
                case "0":
                    running = false;
                    System.out.println("Работа завершена. Задачи сохранены в файл.");
                    break;
                default:
                    System.out.println("Неизвестная команда. Выберите пункт из меню.");
                    break;
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Добавить задачу");
        System.out.println("2. Показать все задачи");
        System.out.println("3. Изменить статус задачи");
        System.out.println("4. Удалить задачу");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void addTask() {
        System.out.print("Введите название задачи: ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("Название задачи не может быть пустым.");
            return;
        }

        Task task = taskManager.addTask(title);
        System.out.println("Задача добавлена: " + task);
    }

    private void showAllTasks() {
        List<Task> tasks = taskManager.getAllTasks();

        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }

        System.out.println();
        System.out.println("ID   | Название                                 | Статус");
        System.out.println("---------------------------------------------------------------");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    private void updateTaskStatus() {
        Integer id = readTaskId("Введите id задачи для изменения статуса: ");
        if (id == null) {
            return;
        }

        System.out.println("Доступные статусы: NEW, IN_PROGRESS, DONE");
        System.out.print("Введите новый статус: ");
        String statusInput = scanner.nextLine().trim().toUpperCase();

        try {
            TaskStatus status = TaskStatus.valueOf(statusInput);
            boolean updated = taskManager.updateTaskStatus(id, status);
            if (updated) {
                System.out.println("Статус задачи успешно обновлён.");
            } else {
                System.out.println("Задача с таким id не найдена.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректный статус. Используйте NEW, IN_PROGRESS или DONE.");
        }
    }

    private void deleteTask() {
        Integer id = readTaskId("Введите id задачи для удаления: ");
        if (id == null) {
            return;
        }

        boolean deleted = taskManager.deleteTask(id);
        if (deleted) {
            System.out.println("Задача удалена.");
        } else {
            System.out.println("Задача с таким id не найдена.");
        }
    }

    private Integer readTaskId(String message) {
        System.out.print(message);
        String input = scanner.nextLine().trim();

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Id должен быть целым числом.");
            return null;
        }
    }
}
