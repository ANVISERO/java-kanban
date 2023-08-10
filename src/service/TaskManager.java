package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {

    // Получение списка всех задач
    List<Task> getTasksList();

    // Удаление всех задач
    void deleteAllTasks();

    // Получение задачи по идентификатору
    Task getTask(long taskId);

    // Создание задачи
    long createTask(Task task);

    // Обновление задачи
    void updateTask(Task task);

    // Удаление задачи по идентификатору
    void deleteTask(long taskId);

    // Получение списка всех эпиков
    List<Epic> getEpicsList();

    // Удаление всех эпиков
    void deleteAllEpics();

    // Получение эпика по идентификатору
    Epic getEpic(long epicId);

    // Создание эпика
    long createEpic(Epic epic);

    // Обновление эпика
    void updateEpic(Epic epic);

    // Удаление эпика по идентификатору
    void deleteEpic(long epicId);

    // Получение списка всех подзадач
    List<Subtask> getSubtasksList();

    // Удаление всех подзадач
    void deleteAllSubtasks();

    // Получение подзадачи по идентификатору
    Subtask getSubtask(long subtaskId);

    // Создание подзадачи
    Long createSubtask(Subtask subtask);

    // Обновление подзадачи
    void updateSubtask(Subtask subtask);

    // Удаление подзадачи по идентификатору
    void deleteSubtask(long subtaskId);

    // Получение списка всех подзадач определённого эпика
    List<Subtask> getSubtasksByEpic(long epicId);
}
