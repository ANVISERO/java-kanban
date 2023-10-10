package ru.yandex.practicum.kanban;

import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Status;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.service.FileBackedTasksManager;
import ru.yandex.practicum.kanban.service.TaskManager;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        final String PATH = "src/ru/yandex/practicum/kanban/resources/storage.csv";
        List<Task> tasks;
        List<Epic> epics;
        List<Subtask> subtasks;
        Task task1 = new Task("task1", "descTask1");
        Task task2 = new Task("task2", "descTask2");
        Epic epic1 = new Epic("epic1", "descEpic1");
        Epic epic2 = new Epic("epic2", "descEpic2");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(PATH);

        System.out.println("Добавление задачи 1:");
        long idTask1 = fileBackedTasksManager.createTask(task1);
        System.out.println("Вывод задачи 1: " + fileBackedTasksManager.getTask(idTask1));
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println("Добавление задачи 2:");
        long idTask2 = fileBackedTasksManager.createTask(task2);
        System.out.println("Вывод задачи 2: " + fileBackedTasksManager.getTask(idTask2));
        System.out.println();

        getHistory(fileBackedTasksManager);

        tasks = fileBackedTasksManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        System.out.println("Изменение статуса задачи 1 на IN_PROGRESS:");
        task1.setStatus(Status.IN_PROGRESS);
        fileBackedTasksManager.updateTask(task1);
        System.out.println("Вывод задачи 1: " + fileBackedTasksManager.getTask(idTask1));
        tasks = fileBackedTasksManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println("Изменение статуса задачи 1 на NEW:");
        task1.setStatus(Status.NEW);
        fileBackedTasksManager.updateTask(task1);
        System.out.println("Вывод задачи 1: " + fileBackedTasksManager.getTask(idTask1));
        tasks = fileBackedTasksManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println("Изменение статуса задачи 1 на DONE:");
        task1.setStatus(Status.DONE);
        fileBackedTasksManager.updateTask(task1);
        System.out.println("Вывод задачи 1: " + fileBackedTasksManager.getTask(idTask1));
        tasks = fileBackedTasksManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println("Изменение статуса задачи 2 на IN_PROGRESS:");
        task2.setStatus(Status.IN_PROGRESS);
        fileBackedTasksManager.updateTask(task2);
        System.out.println("Вывод задачи 2: " + fileBackedTasksManager.getTask(idTask2));
        tasks = fileBackedTasksManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println("Изменение статуса задачи 2 на DONE:");
        task2.setStatus(Status.DONE);
        fileBackedTasksManager.updateTask(task2);
        System.out.println("Вывод задачи 2: " + fileBackedTasksManager.getTask(idTask2));
        tasks = fileBackedTasksManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println("Изменение статуса задачи 2 на DONE:");
        task2.setStatus(Status.DONE);
        fileBackedTasksManager.updateTask(task2);
        System.out.println("Вывод задачи 2: " + fileBackedTasksManager.getTask(idTask2));
        tasks = fileBackedTasksManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println("Удаление задачи 1:");
        fileBackedTasksManager.deleteTask(idTask1);
        System.out.println("Вывод задачи 1: " + fileBackedTasksManager.getTask(idTask1));
        tasks = fileBackedTasksManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println("Добавление задачи 1:");
        long idTask1New = fileBackedTasksManager.createTask(task1);
        System.out.println("Вывод задачи 1: " + fileBackedTasksManager.getTask(idTask1New));
        System.out.println();

        getHistory(fileBackedTasksManager);

        tasks = fileBackedTasksManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        System.out.println("Добавление эпика 1:");
        long idEpic1 = fileBackedTasksManager.createEpic(epic1);
        System.out.println("Вывод эпика 1: " + fileBackedTasksManager.getEpic(idEpic1));
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println("Добавление эпика 2:");
        long idEpic2 = fileBackedTasksManager.createEpic(epic2);
        System.out.println("Вывод эпика 2: " + fileBackedTasksManager.getEpic(idEpic2));
        System.out.println();

        getHistory(fileBackedTasksManager);

        epics = fileBackedTasksManager.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        System.out.println("Добавление подзадачи 1:");
        Subtask subtask1 = new Subtask("subtask1", "descSubtask1", idEpic1);
        long idSubtask1 = fileBackedTasksManager.createSubtask(subtask1);
        System.out.println("Вывод подзадачи 1: " + fileBackedTasksManager.getSubtask(idSubtask1));
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println("Добавление подзадачи 2:");
        Subtask subtask2 = new Subtask("subtask2", "descSubtask2", idEpic1);
        long idSubtask2 = fileBackedTasksManager.createSubtask(subtask2);
        System.out.println("Вывод подзадачи 2: " + fileBackedTasksManager.getSubtask(idSubtask2));
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println("Добавление подзадачи 3:");
        Subtask subtask3 = new Subtask("subtask3", "descSubtask3", idEpic2);
        long idSubtask3 = fileBackedTasksManager.createSubtask(subtask3);
        System.out.println("Вывод подзадачи 3: " + fileBackedTasksManager.getSubtask(idSubtask3));
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println("Добавление подзадачи 4:");
        Subtask subtask4 = new Subtask("subtask4", "descSubtask4", idEpic2);
        long idSubtask4 = fileBackedTasksManager.createSubtask(subtask4);
        System.out.println("Вывод подзадачи 4: " + fileBackedTasksManager.getSubtask(idSubtask4));
        System.out.println();

        getHistory(fileBackedTasksManager);

        epics = fileBackedTasksManager.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        subtasks = fileBackedTasksManager.getSubtasksList();
        System.out.println("Вывод всех подзадач:");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("Изменение статуса подзадачи 1 на IN_PROGRESS:");
        subtask1.setStatus(Status.IN_PROGRESS);
        fileBackedTasksManager.updateSubtask(subtask1);
        System.out.println("Вывод подзадачи 1: " + fileBackedTasksManager.getSubtask(idSubtask1));
        System.out.println("Вывод эпика 1: " + fileBackedTasksManager.getEpic(idEpic1));
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println("Изменение статуса подзадачи 2 на IN_PROGRESS:");
        subtask2.setStatus(Status.IN_PROGRESS);
        fileBackedTasksManager.updateSubtask(subtask2);
        System.out.println("Вывод подзадачи 2: " + fileBackedTasksManager.getSubtask(idSubtask2));
        System.out.println("Вывод эпика 1: " + fileBackedTasksManager.getEpic(idEpic1));
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println("Изменение статуса подзадачи 3 на DONE:");
        subtask3.setStatus(Status.DONE);
        fileBackedTasksManager.updateSubtask(subtask3);
        System.out.println("Вывод подзадачи 3: " + fileBackedTasksManager.getSubtask(idSubtask3));
        System.out.println("Вывод эпика 2: " + fileBackedTasksManager.getEpic(idEpic2));
        System.out.println();

        getHistory(fileBackedTasksManager);

        System.out.println();
        System.out.println("Создали новый новый файловый менеджер");
        System.out.println();

        FileBackedTasksManager fileBackedTasksManager1 = FileBackedTasksManager.loadFromFile(PATH);

        System.out.println();

        getHistory(fileBackedTasksManager1);

        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        epics = fileBackedTasksManager1.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        subtasks = fileBackedTasksManager1.getSubtasksList();
        System.out.println("Вывод всех подзадач:");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("Изменение статуса подзадачи 4 на DONE:");
        subtask4.setStatus(Status.DONE);
        fileBackedTasksManager1.updateSubtask(subtask4);
        System.out.println("Вывод подзадачи 4: " + fileBackedTasksManager1.getSubtask(idSubtask4));
        System.out.println("Вывод эпика 2: " + fileBackedTasksManager1.getEpic(idEpic2));
        System.out.println();

        getHistory(fileBackedTasksManager1);

        System.out.println("Изменение статуса подзадачи 1 на NEW:");
        subtask1.setStatus(Status.NEW);
        fileBackedTasksManager1.updateSubtask(subtask1);
        System.out.println("Вывод подзадачи 1: " + fileBackedTasksManager1.getSubtask(idSubtask1));
        System.out.println("Вывод эпика 1: " + fileBackedTasksManager1.getEpic(idEpic1));
        System.out.println();

        getHistory(fileBackedTasksManager1);

        System.out.println("Изменение статуса подзадачи 2 на NEW:");
        subtask2.setStatus(Status.NEW);
        fileBackedTasksManager1.updateSubtask(subtask2);
        System.out.println("Вывод подзадачи 2: " + fileBackedTasksManager1.getSubtask(idSubtask2));
        System.out.println("Вывод эпика 1: " + fileBackedTasksManager1.getEpic(idEpic1));
        System.out.println();

        getHistory(fileBackedTasksManager1);

        epics = fileBackedTasksManager1.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        subtasks = fileBackedTasksManager1.getSubtasksList();
        System.out.println("Вывод всех подзадач:");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("Обновление эпика:");
        epic1.setDescription("new description");
        fileBackedTasksManager1.updateEpic(epic1);
        System.out.println("Вывод эпика 1: " + fileBackedTasksManager1.getEpic(idEpic1));
        System.out.println();

        getHistory(fileBackedTasksManager1);

        System.out.println("Добавление подзадачи 5:");
        Subtask subtask5 = new Subtask("subtask5", "descSubtask5", idEpic2);
        long idSubtask5 = fileBackedTasksManager1.createSubtask(subtask5);
        System.out.println("Вывод подзадачи 5: " + fileBackedTasksManager1.getSubtask(idSubtask5));
        System.out.println("Вывод эпика 2: " + fileBackedTasksManager1.getEpic(idEpic2));
        System.out.println();

        getHistory(fileBackedTasksManager1);

        epics = fileBackedTasksManager1.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        subtasks = fileBackedTasksManager1.getSubtasksList();
        System.out.println("Вывод всех подзадач:");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("Удаление подзадачи 5:");
        fileBackedTasksManager1.deleteSubtask(idSubtask5);
        epics = fileBackedTasksManager1.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        subtasks = fileBackedTasksManager1.getSubtasksList();
        System.out.println("Вывод всех подзадач:");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        getHistory(fileBackedTasksManager1);

        epics = fileBackedTasksManager1.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        System.out.println("Вывод всех подзадач 1 эпика:");
        subtasks = fileBackedTasksManager1.getSubtasksByEpic(idEpic1);
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("Вывод всех подзадач 2 эпика:");
        subtasks = fileBackedTasksManager1.getSubtasksByEpic(idEpic2);
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("Удаление 2 эпика:");
        fileBackedTasksManager1.deleteEpic(idEpic2);
        epics = fileBackedTasksManager1.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        getHistory(fileBackedTasksManager1);

        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        epics = fileBackedTasksManager1.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        subtasks = fileBackedTasksManager1.getSubtasksList();
        System.out.println("Вывод всех подзадач:");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();
    }

    private static void getHistory(TaskManager fileBackedTasksManager) {
        System.out.println("Вывод истории задач:");
        List<Task> taskHistory = fileBackedTasksManager.getHistory();
        int i = 0;
        for (Task task : taskHistory) {
            System.out.println((++i) + ") " + task);
        }
        System.out.println();
    }
}
