package main.ru.yandex.practicum.kanban.service.managers;

import main.ru.yandex.practicum.kanban.model.Epic;
import main.ru.yandex.practicum.kanban.model.Subtask;
import main.ru.yandex.practicum.kanban.model.Task;

import java.util.List;
import java.util.Set;

// Данный интерфейс отражает методы, которые должны быть у любого менеджера задач
public interface TaskManager {

    // Получение списка всех задач
    List<Task> getTasksList();

    // Удаление всех задач
    void deleteAllTasks();

    // Получение задачи по идентификатору
    Task getTask(int taskId);

    // Создание задачи
    int createTask(Task task);

    // Обновление задачи
    void updateTask(Task task);

    // Удаление задачи по идентификатору
    void deleteTask(int taskId);

    // Получение списка всех эпиков
    List<Epic> getEpicsList();

    // Удаление всех эпиков
    void deleteAllEpics();

    // Получение эпика по идентификатору
    Epic getEpic(int epicId);

    // Создание эпика
    int createEpic(Epic epic);

    // Обновление эпика
    void updateEpic(Epic epic);

    // Удаление эпика по идентификатору
    void deleteEpic(int epicId);

    // Получение списка всех подзадач
    List<Subtask> getSubtasksList();

    // Удаление всех подзадач
    void deleteAllSubtasks();

    // Получение подзадачи по идентификатору
    Subtask getSubtask(int subtaskId);

    // Создание подзадачи
    Integer createSubtask(Subtask subtask);

    // Обновление подзадачи
    void updateSubtask(Subtask subtask);

    // Удаление подзадачи по идентификатору
    void deleteSubtask(int subtaskId);

    // Получение списка всех подзадач определённого эпика
    List<Subtask> getSubtasksByEpic(int epicId);

    // Возвращает историю просмотренных задач
    List<Task> getHistory();

    // Получение списка задач и подзадач в порядке увеличения времени начала задачи (startTime)
    Set<Task> getPrioritizedTasks();
}
