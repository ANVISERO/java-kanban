package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

// Это менеджер, который управляет всеми задачами
public class TaskManager {
    private long idGenerator = 0;
    private HashMap<Long, Task> tasks;
    private HashMap<Long, Epic> epics;
    private HashMap<Long, Subtask> subtasks;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    // Получение списка всех задач
    public ArrayList<Task> getTasksList() {
        if (tasks.isEmpty()) {
            return null;
        }
        ArrayList<Task> tasksArrayList = new ArrayList<>();
        for (Task task : tasks.values()) {
            tasksArrayList.add(task);
        }
        return tasksArrayList;
    }

    // Удаление всех задач
    public void deleteAllTasks() {
        tasks.clear();
        System.out.println("Все задачи успешно удалены!");
    }

    // Получение задачи по идентификатору
    public Task getTask(long taskId) {
        if (!tasks.containsKey(taskId)) {
            System.out.println("Задачи с идентификатором " + taskId + " не существует!");
            return null;
        }
        return tasks.get(taskId);
    }

    // Создание задачи
    public long createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        System.out.println("Задача с идентификатором " + task.getId() + " успешно создана!");
        return task.getId();
    }

    // Обновление задачи
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
    public void deleteTask(long taskId) {
        if (!tasks.containsKey(taskId)) {
            System.out.println("Задачи с идентификатором " + taskId + " не существует!");
            return;
        }
        tasks.remove(taskId);
        System.out.println("Задача с идентификатором " + taskId + " успешно удалена!");
    }

    // Получение списка всех эпиков
    public ArrayList<Epic> getEpicsList() {
        if (epics.isEmpty()) {
            return null;
        }
        ArrayList<Epic> epicsArrayList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicsArrayList.add(epic);
        }
        return epicsArrayList;
    }

    // Удаление всех эпиков
    public void deleteAllEpics() {
        deleteAllSubtasks();
        epics.clear();
        System.out.println("Все эпики успешно удалены!");
    }

    // Получение эпика по идентификатору
    public Epic getEpic(long epicId) {
        if (!epics.containsKey(epicId)) {
            System.out.println("Эпика с идентификатором " + epicId + " не существует!");
            return null;
        }
        return epics.get(epicId);
    }

    // Создание эпика
    public long createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        System.out.println("Эпик с идентификатором " + epic.getId() + " успешно создан!");
        return epic.getId();
    }

    // Обновление эпика
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
    public void deleteEpic(long epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            System.out.println("Эпика с идентификатором " + epicId + " не существует!");
            return;
        }

        ArrayList<Long> subtaskIds = epic.getSubtaskIds();
        for (Long subtaskId : subtaskIds) {
            subtasks.remove(subtaskId);
        }

        epics.remove(epicId);
        System.out.println("Эпик с идентификатором " + epicId + " успешно удалён!");
    }

    // Получение списка всех подзадач
    public ArrayList<Subtask> getSubtasksList() {
        if (subtasks.isEmpty()) {
            return null;
        }
        ArrayList<Subtask> subtasksArrayList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtasksArrayList.add(subtask);
        }
        return subtasksArrayList;
    }

    // Удаление всех подзадач
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtaskId();
        }

        subtasks.clear();
        System.out.println("Все подзадачи успешно удалены!");
    }

    // Получение подзадачи по идентификатору
    public Subtask getSubtask(long subtaskId) {
        if (!subtasks.containsKey(subtaskId)) {
            System.out.println("Подзадачи с идентификатором " + subtaskId + " не существует!");
            return null;
        }
        return subtasks.get(subtaskId);
    }

    // Создание подзадачи
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
    public ArrayList<Subtask> getSubtasksByEpic(long epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            System.out.println("Эпик с идентификатором " + epicId + " не существует!");
            return null;
        }
        ArrayList<Long> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            return null;
        }
        ArrayList<Subtask> subtasksArrayList = new ArrayList<>();
        for (Long subtaskId : subtaskIds) {
            subtasksArrayList.add(subtasks.get(subtaskId));
        }
        return subtasksArrayList;
    }

    // Генерация идентификатора
    private long generateId() {
        return ++idGenerator;
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Long> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus("NEW");
            epics.put(epic.getId(), epic);
            return;
        }

        String status = null;
        for (Long subtaskId : subtaskIds) {
            String subtaskStatus = subtasks.get(subtaskId).getStatus();

            if (status == null) {
                status = subtaskStatus;
                continue;
            }

            if (!status.equals(subtaskStatus)) {
                status = "IN_PROGRESS";
                epic.setStatus(status);
                epics.put(epic.getId(), epic);
                return;
            }
        }
        epic.setStatus(status);
        epics.put(epic.getId(), epic);
    }
}
