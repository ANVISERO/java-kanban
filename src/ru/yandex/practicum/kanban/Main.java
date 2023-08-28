package ru.yandex.practicum.kanban;

import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Status;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.service.TaskManager;
import ru.yandex.practicum.kanban.util.Managers;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Task> tasks;
        List<Epic> epics;
        List<Subtask> subtasks;
        Task task1 = new Task("task1", "descTask1");
        Task task2 = new Task("task2", "descTask2");
        Epic epic1 = new Epic("epic1", "descEpic1");
        Epic epic2 = new Epic("epic2", "descEpic2");
        Epic epic3 = new Epic("epic3", "descEpic3");
        TaskManager taskManager = Managers.getDefault();

        System.out.println("Добавление задачи 1:");
        long idTask1 = taskManager.createTask(task1);
        System.out.println("Вывод задачи 1: " + taskManager.getTask(idTask1));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Добавление задачи 2:");
        long idTask2 = taskManager.createTask(task2);
        System.out.println("Вывод задачи 2: " + taskManager.getTask(idTask2));
        System.out.println();

        getHistory(taskManager);

        tasks = taskManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        System.out.println("Изменение статуса задачи 1 на IN_PROGRESS:");
        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        System.out.println("Вывод задачи 1: " + taskManager.getTask(idTask1));
        tasks = taskManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        getHistory(taskManager);

        System.out.println("Изменение статуса задачи 1 на NEW:");
        task1.setStatus(Status.NEW);
        taskManager.updateTask(task1);
        System.out.println("Вывод задачи 1: " + taskManager.getTask(idTask1));
        tasks = taskManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        getHistory(taskManager);

        System.out.println("Изменение статуса задачи 1 на DONE:");
        task1.setStatus(Status.DONE);
        taskManager.updateTask(task1);
        System.out.println("Вывод задачи 1: " + taskManager.getTask(idTask1));
        tasks = taskManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        getHistory(taskManager);

        System.out.println("Изменение статуса задачи 2 на IN_PROGRESS:");
        task2.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task2);
        System.out.println("Вывод задачи 2: " + taskManager.getTask(idTask2));
        tasks = taskManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        getHistory(taskManager);

        System.out.println("Изменение статуса задачи 2 на DONE:");
        task2.setStatus(Status.DONE);
        taskManager.updateTask(task2);
        System.out.println("Вывод задачи 2: " + taskManager.getTask(idTask2));
        tasks = taskManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        getHistory(taskManager);

        System.out.println("Изменение статуса задачи 2 на DONE:");
        task2.setStatus(Status.DONE);
        taskManager.updateTask(task2);
        System.out.println("Вывод задачи 2: " + taskManager.getTask(idTask2));
        tasks = taskManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        getHistory(taskManager);

        System.out.println("Удаление задачи 1:");
        taskManager.deleteTask(idTask1);
        System.out.println("Вывод задачи 1: " + taskManager.getTask(idTask1));
        tasks = taskManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        getHistory(taskManager);

        System.out.println("Добавление задачи 1:");
        long idTask1New = taskManager.createTask(task1);
        System.out.println("Вывод задачи 1: " + taskManager.getTask(idTask1New));
        System.out.println();

        getHistory(taskManager);

        tasks = taskManager.getTasksList();
        System.out.println("Вывод всех задач:");
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println();

        System.out.println("Удаление всех задач:");
        taskManager.deleteAllTasks();
        System.out.println("Вывод всех задач:");
        tasks = taskManager.getTasksList();
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст!");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
        System.out.println();

        getHistory(taskManager);

        System.out.println("Добавление эпика 1:");
        long idEpic1 = taskManager.createEpic(epic1);
        System.out.println("Вывод эпика 1: " + taskManager.getEpic(idEpic1));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Добавление эпика 2:");
        long idEpic2 = taskManager.createEpic(epic2);
        System.out.println("Вывод эпика 2: " + taskManager.getEpic(idEpic2));
        System.out.println();

        getHistory(taskManager);

        epics = taskManager.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        System.out.println("Добавление подзадачи 1:");
        Subtask subtask1 = new Subtask("subtask1", "descSubtask1", idEpic1);
        long idSubtask1 = taskManager.createSubtask(subtask1);
        System.out.println("Вывод подзадачи 1: " + taskManager.getSubtask(idSubtask1));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Добавление подзадачи 2:");
        Subtask subtask2 = new Subtask("subtask2", "descSubtask2", idEpic1);
        long idSubtask2 = taskManager.createSubtask(subtask2);
        System.out.println("Вывод подзадачи 2: " + taskManager.getSubtask(idSubtask2));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Добавление подзадачи 3:");
        Subtask subtask3 = new Subtask("subtask3", "descSubtask3", idEpic2);
        long idSubtask3 = taskManager.createSubtask(subtask3);
        System.out.println("Вывод подзадачи 3: " + taskManager.getSubtask(idSubtask3));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Добавление подзадачи 4:");
        Subtask subtask4 = new Subtask("subtask4", "descSubtask4", idEpic2);
        long idSubtask4 = taskManager.createSubtask(subtask4);
        System.out.println("Вывод подзадачи 4: " + taskManager.getSubtask(idSubtask4));
        System.out.println();

        getHistory(taskManager);

        epics = taskManager.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        subtasks = taskManager.getSubtasksList();
        System.out.println("Вывод всех подзадач:");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("Изменение статуса подзадачи 1 на IN_PROGRESS:");
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        System.out.println("Вывод подзадачи 1: " + taskManager.getSubtask(idSubtask1));
        System.out.println("Вывод эпика 1: " + taskManager.getEpic(idEpic1));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Изменение статуса подзадачи 2 на IN_PROGRESS:");
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask2);
        System.out.println("Вывод подзадачи 2: " + taskManager.getSubtask(idSubtask2));
        System.out.println("Вывод эпика 1: " + taskManager.getEpic(idEpic1));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Изменение статуса подзадачи 3 на DONE:");
        subtask3.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask3);
        System.out.println("Вывод подзадачи 3: " + taskManager.getSubtask(idSubtask3));
        System.out.println("Вывод эпика 2: " + taskManager.getEpic(idEpic2));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Изменение статуса подзадачи 4 на DONE:");
        subtask4.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask4);
        System.out.println("Вывод подзадачи 4: " + taskManager.getSubtask(idSubtask4));
        System.out.println("Вывод эпика 2: " + taskManager.getEpic(idEpic2));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Изменение статуса подзадачи 1 на NEW:");
        subtask1.setStatus(Status.NEW);
        taskManager.updateSubtask(subtask1);
        System.out.println("Вывод подзадачи 1: " + taskManager.getSubtask(idSubtask1));
        System.out.println("Вывод эпика 1: " + taskManager.getEpic(idEpic1));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Изменение статуса подзадачи 2 на NEW:");
        subtask2.setStatus(Status.NEW);
        taskManager.updateSubtask(subtask2);
        System.out.println("Вывод подзадачи 2: " + taskManager.getSubtask(idSubtask2));
        System.out.println("Вывод эпика 1: " + taskManager.getEpic(idEpic1));
        System.out.println();

        getHistory(taskManager);

        epics = taskManager.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        subtasks = taskManager.getSubtasksList();
        System.out.println("Вывод всех подзадач:");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("Обновление эпика:");
        epic1.setDescription("new description");
        taskManager.updateEpic(epic1);
        System.out.println("Вывод эпика 1: " + taskManager.getEpic(idEpic1));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Добавление подзадачи 5:");
        Subtask subtask5 = new Subtask("subtask5", "descSubtask5", idEpic2);
        long idSubtask5 = taskManager.createSubtask(subtask5);
        System.out.println("Вывод подзадачи 5: " + taskManager.getSubtask(idSubtask5));
        System.out.println("Вывод эпика 2: " + taskManager.getEpic(idEpic2));
        System.out.println();

        getHistory(taskManager);

        epics = taskManager.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        subtasks = taskManager.getSubtasksList();
        System.out.println("Вывод всех подзадач:");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("Удаление подзадачи 5:");
        taskManager.deleteSubtask(idSubtask5);
        epics = taskManager.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        subtasks = taskManager.getSubtasksList();
        System.out.println("Вывод всех подзадач:");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        getHistory(taskManager);

        System.out.println("Удаление всех подзадач:");
        taskManager.deleteAllSubtasks();
        epics = taskManager.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        if (epics.isEmpty()) {
            System.out.println("Список подзадач пуст!");
        } else {
            for (Epic epic : epics) {
                System.out.println(epic);
            }
        }
        System.out.println();

        subtasks = taskManager.getSubtasksList();
        System.out.println("Вывод всех подзадач:");
        if (subtasks.isEmpty()) {
            System.out.println("Список подзадач пуст!");
        } else {
            for (Subtask subtask : subtasks) {
                System.out.println(subtask);
            }
        }
        System.out.println();

        getHistory(taskManager);

        System.out.println("Добавление подзадачи 1:");
        long idSubtask1New = taskManager.createSubtask(subtask1);
        System.out.println("Вывод подзадачи 1: " + taskManager.getSubtask(idSubtask1New));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Добавление подзадачи 2:");
        long idSubtask2New = taskManager.createSubtask(subtask2);
        System.out.println("Вывод подзадачи 2: " + taskManager.getSubtask(idSubtask2New));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Добавление подзадачи 3:");
        long idSubtask3New = taskManager.createSubtask(subtask3);
        System.out.println("Вывод подзадачи 3: " + taskManager.getSubtask(idSubtask3New));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Добавление подзадачи 4:");
        long idSubtask4New = taskManager.createSubtask(subtask4);
        System.out.println("Вывод подзадачи 4: " + taskManager.getSubtask(idSubtask4New));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Добавление подзадачи 5:");
        long idSubtask5New = taskManager.createSubtask(subtask5);
        System.out.println("Вывод подзадачи 5: " + taskManager.getSubtask(idSubtask5New));
        System.out.println();

        getHistory(taskManager);

        epics = taskManager.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        subtasks = taskManager.getSubtasksList();
        System.out.println("Вывод всех подзадач:");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("Вывод всех подзадач 1 эпика:");
        subtasks = taskManager.getSubtasksByEpic(idEpic1);
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("Вывод всех подзадач 2 эпика:");
        subtasks = taskManager.getSubtasksByEpic(idEpic2);
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("Удаление 2 эпика:");
        taskManager.deleteEpic(idEpic2);
        epics = taskManager.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        getHistory(taskManager);

        subtasks = taskManager.getSubtasksList();
        System.out.println("Вывод всех подзадач:");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("Удаление всех эпиков:");
        taskManager.deleteAllEpics();
        epics = taskManager.getEpicsList();
        System.out.println();

        System.out.println("Вывод всех эпиков:");
        if (epics.isEmpty()) {
            System.out.println("Список эпиков пуст!");
        } else {
            for (Epic epic : epics) {
                System.out.println(epic);
            }
        }
        System.out.println();

        subtasks = taskManager.getSubtasksList();
        System.out.println("Вывод всех подзадач:");
        if (epics.isEmpty()) {
            System.out.println("Список подзадач пуст!");
        } else {
            for (Subtask subtask : subtasks) {
                System.out.println(subtask);
            }
        }
        System.out.println();

        getHistory(taskManager);

        System.out.println("Добавление эпика 3:");
        long idEpic3 = taskManager.createEpic(epic3);
        System.out.println("Вывод эпика 3: " + taskManager.getEpic(idEpic3));
        System.out.println();

        getHistory(taskManager);

        System.out.println("Удаление 3 эпика:");
        taskManager.deleteEpic(idEpic3);
        epics = taskManager.getEpicsList();
        System.out.println("Вывод всех эпиков:");
        for (Epic epic : epics) {
            System.out.println(epic);
        }
        System.out.println();

        getHistory(taskManager);
    }

    private static void getHistory(TaskManager taskManager) {
        System.out.println("Вывод истории задач:");
        List<Task> taskHistory = taskManager.getHistory();
        int i = 0;
        for (Task task : taskHistory) {
            System.out.println((++i) + ") " + task);
        }
        System.out.println();
    }
}
