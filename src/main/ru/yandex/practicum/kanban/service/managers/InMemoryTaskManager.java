package main.ru.yandex.practicum.kanban.service.managers;

import main.ru.yandex.practicum.kanban.model.Epic;
import main.ru.yandex.practicum.kanban.service.exceptions.TimeOverlayException;
import main.ru.yandex.practicum.kanban.util.Managers;
import main.ru.yandex.practicum.kanban.model.Status;
import main.ru.yandex.practicum.kanban.model.Subtask;
import main.ru.yandex.practicum.kanban.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

// Это менеджер, который управляет всеми задачами
public class InMemoryTaskManager implements TaskManager {
    private static int idGenerator;
    protected static Map<Integer, Task> tasks;
    protected static Map<Integer, Subtask> subtasks;
    protected static Map<Integer, Epic> epics;
    protected static Set<Task> prioritizedTasks;    // Задачи, расставленные в порядке увеличения времени начала задачи
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        idGenerator = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        prioritizedTasks = new TreeSet<>(
                (task1, task2) -> (task1.getStartTime() == null && task2.getStartTime() == null
                        || task1.getStartTime() != null && task2.getStartTime() == null)
                        ? (task1.getId() - task2.getId()) : ((task1.getStartTime() == null && task2.getStartTime() != null)
                        ? 1 : ((task1.getStartTime() != null && task2.getStartTime() == null)
                        ? -1 : task1.getStartTime().compareTo(task2.getStartTime()))));
        historyManager = Managers.getDefaultHistoryManager();
    }

    // Получение списка всех задач
    @Override
    public List<Task> getTasksList() {
        if (tasks.values() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(tasks.values());
    }

    // Удаление всех задач
    @Override
    public void deleteAllTasks() {
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
            prioritizedTasks.remove(tasks.get(taskId));
        }
        tasks.clear();
        System.out.println("Все задачи успешно удалены!");
    }

    // Получение задачи по идентификатору
    @Override
    public Task getTask(int taskId) {
        if (!tasks.containsKey(taskId)) {
            System.out.println("Задачи с идентификатором " + taskId + " не существует!");
            throw new IllegalArgumentException("Задачи с идентификатором " + taskId + " не существует!");
        }

        Task task = tasks.get(taskId);
        historyManager.add(task);

        return task;
    }

    // Создание задачи
    @Override
    public int createTask(Task task) {
        checkTimeOverlay(task);

        task.setId(generateId());
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
        System.out.println("Задача с идентификатором " + task.getId() + " успешно создана!");

        return task.getId();
    }

    // Обновление задачи
    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Задачи " + task.getTitle() + " с идентификатором " + task.getId() + " не существует!");
            throw new IllegalArgumentException("Задачи с идентификатором " + task.getId() + " не существует!");
        }

        prioritizedTasks.remove(tasks.get(task.getId()));
        checkTimeOverlay(task);
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
        System.out.println("Задача с идентификатором " + task.getId() + " успешно обновлена. \n" +
                "Текущий статус задачи: " + task.getStatus());
    }

    // Удаление задачи по идентификатору
    @Override
    public void deleteTask(int taskId) {
        if (!tasks.containsKey(taskId)) {
            System.out.println("Задачи с идентификатором " + taskId + " не существует!");
            throw new IllegalArgumentException("Задачи с идентификатором " + taskId + " не существует!");
        }
        historyManager.remove(taskId);
        prioritizedTasks.remove(tasks.get(taskId));
        tasks.remove(taskId);
        System.out.println("Задача с идентификатором " + taskId + " успешно удалена!");
    }

    // Получение списка всех эпиков
    @Override
    public List<Epic> getEpicsList() {
        if (epics.values() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(epics.values());
    }

    // Удаление всех эпиков
    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();
        for (Integer epicId : epics.keySet()) {
            historyManager.remove(epicId);
        }
        epics.clear();
        System.out.println("Все эпики успешно удалены!");
    }

    // Получение эпика по идентификатору
    @Override
    public Epic getEpic(int epicId) {
        if (!epics.containsKey(epicId)) {
            System.out.println("Эпика с идентификатором " + epicId + " не существует!");
            throw new IllegalArgumentException("Эпика с идентификатором " + epicId + " не существует!");
        }

        Epic epic = epics.get(epicId);
        historyManager.add(epic);

        return epic;
    }

    // Создание эпика
    @Override
    public int createEpic(Epic epic) {
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
            throw new IllegalArgumentException("Эпика с идентификатором " + epic.getId() + " не существует!");
        }

        updateEpicStatus(epic);
        System.out.println("Эпик с идентификатором " + epic.getId() + " успешно обновлён. \n" +
                "Текущий статус эпика: " + epic.getStatus());
    }

    // Удаление эпика по идентификатору
    @Override
    public void deleteEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            System.out.println("Эпика с идентификатором " + epicId + " не существует!");
            throw new IllegalArgumentException("Эпика с идентификатором " + epicId + " не существует!");
        }

        List<Integer> subtaskIds = epic.getSubtaskIds();
        for (Integer subtaskId : subtaskIds) {
            historyManager.remove(subtaskId);
            prioritizedTasks.remove(subtasks.get(subtaskId));
            subtasks.remove(subtaskId);
        }

        historyManager.remove(epicId);
        epics.remove(epicId);
        System.out.println("Эпик с идентификатором " + epicId + " успешно удалён!");
    }

    // Получение списка всех подзадач
    @Override
    public List<Subtask> getSubtasksList() {
        if (subtasks.values() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(subtasks.values());
    }

    // Удаление всех подзадач
    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtaskId();
        }
        for (Integer subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subtasks.values().forEach(prioritizedTasks::remove);
        subtasks.clear();
        System.out.println("Все подзадачи успешно удалены!");
    }

    // Получение подзадачи по идентификатору
    @Override
    public Subtask getSubtask(int subtaskId) {
        if (!subtasks.containsKey(subtaskId)) {
            System.out.println("Подзадачи с идентификатором " + subtaskId + " не существует!");
            throw new IllegalArgumentException("Подзадачи с идентификатором " + subtaskId + " не существует!");
        }

        Subtask subtask = subtasks.get(subtaskId);
        historyManager.add(subtask);

        return subtask;
    }

    // Создание подзадачи
    @Override
    public Integer createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Данная подзадача не привязана ни к какому из существующих эпиков!");
            throw new IllegalArgumentException("Данная подзадача не привязана ни к какому из существующих эпиков!");
        }

        checkTimeOverlay(subtask);

        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.add(subtask);
        epic.addSubtaskId(subtask.getId());
        System.out.println("Подзадача с идентификатором " + subtask.getId() + " успешно создана!");

        updateEpicStatus(epic);
        updateEpicTime(epic);


        return subtask.getId();
    }

    // Обновление подзадачи
    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Подзадачи " + subtask.getTitle() + " с идентификатором " + subtask.getId()
                    + " не существует!");
            throw new IllegalArgumentException("Подзадачи с идентификатором " + subtask.getId() + " не существует!");
        }

        prioritizedTasks.remove(subtasks.get(subtask.getId()));
        checkTimeOverlay(subtask);
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.add(subtask);
        System.out.println("Подзадача с идентификатором " + subtask.getId() + " успешно обновлена. \n" +
                "Текущий статус подзадачи: " + subtask.getStatus());

        updateEpicStatus(epics.get(subtask.getEpicId()));
        updateEpicTime(epics.get(subtask.getEpicId()));
    }

    // Удаление подзадачи по идентификатору
    @Override
    public void deleteSubtask(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask == null) {
            System.out.println("Подзадачи с идентификатором " + subtaskId + " не существует!");
            throw new IllegalArgumentException("Подзадачи с идентификатором " + subtaskId + " не существует!");
        }

        Epic epic = epics.get(subtask.getEpicId());
        epic.deleteSubtaskId(subtaskId);
        historyManager.remove(subtaskId);
        prioritizedTasks.remove(subtask);
        subtasks.remove(subtaskId);
        System.out.println("Подзадача с идентификатором " + subtaskId + " успешно удалена!");
        updateEpicStatus(epic);
    }

    // Получение списка всех подзадач определённого эпика
    @Override
    public List<Subtask> getSubtasksByEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            System.out.println("Эпика с идентификатором " + epicId + " не существует!");
            return new ArrayList<>();
        }
        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<Subtask> subtasksList = new ArrayList<>();
        for (Integer subtaskId : subtaskIds) {
            subtasksList.add(subtasks.get(subtaskId));
        }
        return subtasksList;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    protected HistoryManager getHistoryManager() {
        return historyManager;
    }

    // Генерация идентификатора
    private int generateId() {
        return ++idGenerator;
    }

    // Установить значение id на котором остановился счёт
    protected static void setId(Integer id) {
        idGenerator = id;
    }

    // Установить значение всех задач
    protected static void setAllTasks(Map<Integer, Task> tasksFromFile, Map<Integer, Subtask> subtasksFromFile,
                                      Map<Integer, Epic> epicsFromFile) {
        tasks = tasksFromFile;
        subtasks = subtasksFromFile;
        epics = epicsFromFile;
    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            epics.put(epic.getId(), epic);
            return;
        }

        Status status = null;
        for (Integer subtaskId : subtaskIds) {
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

    // Метод, который проверяет, что задачи и подзадачи не пересекаются по времени выполнения
    private void checkTimeOverlay(Task task) {
        if (task.getStartTime() == null) {
            return;
        }
        for (Task task1 : prioritizedTasks) {
            if (task1.getStartTime() == null) {
                return;
            }
            if (task.getStartTime().equals(task1.getStartTime()) || task.getEndTime().equals(task1.getEndTime())
                    || task.getStartTime().isAfter(task1.getStartTime())
                    && task.getStartTime().isBefore(task1.getEndTime())
                    || task.getEndTime().isAfter(task1.getStartTime())
                    && task.getEndTime().isBefore(task1.getEndTime())
            ) {
                throw new TimeOverlayException("Задача " + task + " пересекается по времени с задачей " + task1);
            }
        }
    }

    // Метод, который пересчитывает время начала и завершения эпика, а также его продолжительность
    private void updateEpicTime(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();
        LocalDateTime startTime = LocalDateTime.MAX;
        LocalDateTime endTime = LocalDateTime.MIN;

        for (Integer subtaskId : subtaskIds) {
            if (subtasks.get(subtaskId).getStartTime() == null) {
                continue;
            }
            if (subtasks.get(subtaskId).getStartTime().isBefore(startTime)) {
                startTime = subtasks.get(subtaskId).getStartTime();
            }
            if (subtasks.get(subtaskId).getEndTime().isAfter(endTime)) {
                endTime = subtasks.get(subtaskId).getEndTime();
            }
        }
        if (startTime.equals(LocalDateTime.MAX) && endTime.equals(LocalDateTime.MIN)) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setTaskDuration(null);
        } else if (startTime.equals(LocalDateTime.MAX)) {
            epic.setStartTime(null);
            epic.setEndTime(endTime);
            epic.setTaskDuration(null);
        } else if (endTime.equals(LocalDateTime.MIN)) {
            epic.setStartTime(startTime);
            epic.setEndTime(null);
            epic.setTaskDuration(null);
        } else {
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            epic.setTaskDuration(Duration.between(startTime, endTime));
        }
    }
}
