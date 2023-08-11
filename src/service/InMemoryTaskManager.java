package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import util.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Это менеджер, который управляет всеми задачами
public class InMemoryTaskManager implements TaskManager {
    private long idGenerator = 0;
    private final HashMap<Long, Task> tasks;
    private final HashMap<Long, Epic> epics;
    private final HashMap<Long, Subtask> subtasks;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    // Получение списка всех задач
    @Override
    public List<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    // Удаление всех задач
    @Override
    public void deleteAllTasks() {
        tasks.clear();
        System.out.println("Все задачи успешно удалены!");
    }

    // Получение задачи по идентификатору
    @Override
    public Task getTask(long taskId) {
        if (!tasks.containsKey(taskId)) {
            System.out.println("Задачи с идентификатором " + taskId + " не существует!");
            return null;
        }

        Task task = tasks.get(taskId);
        historyManager.add(task);

        return task;
    }

    // Создание задачи
    @Override
    public long createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        System.out.println("Задача с идентификатором " + task.getId() + " успешно создана!");
        return task.getId();
    }

    // Обновление задачи
    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Задачи " + task.getTitle() + " с идентификатором " + task.getId() + " не существует!");
            return;
        }

        tasks.put(task.getId(), task);
        System.out.println("Задача с идентификатором " + task.getId() + " успешно обновлена. \n" +
                "Текущий статус задачи: " + task.getStatus());
    }

    // Удаление задачи по идентификатору
    @Override
    public void deleteTask(long taskId) {
        if (!tasks.containsKey(taskId)) {
            System.out.println("Задачи с идентификатором " + taskId + " не существует!");
            return;
        }
        tasks.remove(taskId);
        System.out.println("Задача с идентификатором " + taskId + " успешно удалена!");
    }

    // Получение списка всех эпиков
    @Override
    public List<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    // Удаление всех эпиков
    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();
        epics.clear();
        System.out.println("Все эпики успешно удалены!");
    }

    // Получение эпика по идентификатору
    @Override
    public Epic getEpic(long epicId) {
        if (!epics.containsKey(epicId)) {
            System.out.println("Эпика с идентификатором " + epicId + " не существует!");
            return null;
        }

        Epic epic = epics.get(epicId);
        historyManager.add(epic);

        return epic;
    }

    // Создание эпика
    @Override
    public long createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        System.out.println("Эпик с идентификатором " + epic.getId() + " успешно создан!");
        return epic.getId();
    }

    // Обновление эпика
    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Эпика " + epic.getTitle() + " с идентификатором " + epic.getId()
                    + " не существует!");
            return;
        }

        updateEpicStatus(epic);
        System.out.println("Эпик с идентификатором " + epic.getId() + " успешно обновлён. \n" +
                "Текущий статус эпика: " + epic.getStatus());
    }

    // Удаление эпика по идентификатору
    @Override
    public void deleteEpic(long epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            System.out.println("Эпика с идентификатором " + epicId + " не существует!");
            return;
        }

        List<Long> subtaskIds = epic.getSubtaskIds();
        for (Long subtaskId : subtaskIds) {
            subtasks.remove(subtaskId);
        }

        epics.remove(epicId);
        System.out.println("Эпик с идентификатором " + epicId + " успешно удалён!");
    }

    // Получение списка всех подзадач
    @Override
    public List<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    // Удаление всех подзадач
    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtaskId();
        }

        subtasks.clear();
        System.out.println("Все подзадачи успешно удалены!");
    }

    // Получение подзадачи по идентификатору
    @Override
    public Subtask getSubtask(long subtaskId) {
        if (!subtasks.containsKey(subtaskId)) {
            System.out.println("Подзадачи с идентификатором " + subtaskId + " не существует!");
            return null;
        }

        Subtask subtask = subtasks.get(subtaskId);
        historyManager.add(subtask);

        return subtask;
    }

    // Создание подзадачи
    @Override
    public Long createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Данная подзадача не привязана ни к какому эпику!");
            return null;
        }

        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtaskId(subtask.getId());
        System.out.println("Подзадача с идентификатором " + subtask.getId() + " успешно создана!");

        updateEpicStatus(epic);

        return subtask.getId();
    }

    // Обновление подзадачи
    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Подзадачи " + subtask.getTitle() + " с идентификатором " + subtask.getId() + " не существует!");
            return;
        }

        subtasks.put(subtask.getId(), subtask);
        System.out.println("Подзадача с идентификатором " + subtask.getId() + " успешно обновлена. \n" +
                "Текущий статус подзадачи: " + subtask.getStatus());

        updateEpicStatus(epics.get(subtask.getEpicId()));
    }

    // Удаление подзадачи по идентификатору
    @Override
    public void deleteSubtask(long subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask == null) {
            System.out.println("Подзадачи с идентификатором " + subtaskId + " не существует!");
            return;
        }

        Epic epic = epics.get(subtask.getEpicId());
        epic.deleteSubtaskId(subtaskId);
        subtasks.remove(subtaskId);
        System.out.println("Задача с идентификатором " + subtaskId + " успешно удалена!");
        updateEpicStatus(epic);
    }

    // Получение списка всех подзадач определённого эпика
    @Override
    public List<Subtask> getSubtasksByEpic(long epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            System.out.println("Эпик с идентификатором " + epicId + " не существует!");
            return new ArrayList<>();
        }
        List<Long> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<Subtask> subtasksList = new ArrayList<>();
        for (Long subtaskId : subtaskIds) {
            subtasksList.add(subtasks.get(subtaskId));
        }
        return subtasksList;
    }

    // Генерация идентификатора
    private long generateId() {
        return ++idGenerator;
    }

    private void updateEpicStatus(Epic epic) {
        List<Long> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            epics.put(epic.getId(), epic);
            return;
        }

        Status status = null;
        for (Long subtaskId : subtaskIds) {
            Status subtaskStatus = subtasks.get(subtaskId).getStatus();

            if (status == null) {
                status = subtaskStatus;
                continue;
            }

            if (!status.equals(subtaskStatus)) {
                status = Status.IN_PROGRESS;
                epic.setStatus(status);
                epics.put(epic.getId(), epic);
                return;
            }
        }
        epic.setStatus(status);
        epics.put(epic.getId(), epic);
    }
}
