package main.ru.yandex.practicum.kanban.service.managers;

import main.ru.yandex.practicum.kanban.model.Epic;
import main.ru.yandex.practicum.kanban.model.Subtask;
import main.ru.yandex.practicum.kanban.model.Task;
import main.ru.yandex.practicum.kanban.model.TypeOfTask;
import main.ru.yandex.practicum.kanban.service.CSVFormatter;
import main.ru.yandex.practicum.kanban.service.exceptions.ManagerSaveException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// В данном классе находится логика автосохранения в файл
public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String path; // путь к файлу

    public FileBackedTasksManager(final String path) {
        this.path = path;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public Task getTask(int taskId) {
        Task task = super.getTask(taskId);
        save();
        return task;
    }

    @Override
    public int createTask(Task task) {
        int taskId = super.createTask(task);
        save();
        return taskId;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = super.getEpic(epicId);
        save();
        return epic;
    }

    @Override
    public int createEpic(Epic epic) {
        int epicId = super.createEpic(epic);
        save();
        return epicId;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Subtask getSubtask(int subtaskId) {
        Subtask subtask = super.getSubtask(subtaskId);
        save();
        return subtask;
    }

    @Override
    public Integer createSubtask(Subtask subtask) {
        Integer subtaskId = super.createSubtask(subtask);
        save();
        return subtaskId;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        super.deleteSubtask(subtaskId);
        save();
    }

    // Данный метод сохраняет состояние программы в файл (задачи и историю)
    protected void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, false))) {
            bufferedWriter.write(CSVFormatter.getHeader());
            bufferedWriter.newLine();
            for (Integer key : tasks.keySet()) {
                bufferedWriter.write(CSVFormatter.toString(tasks.get(key)));
                bufferedWriter.newLine();
            }
            for (Integer key : epics.keySet()) {
                bufferedWriter.write(CSVFormatter.toString(epics.get(key)));
                bufferedWriter.newLine();
            }
            for (Integer key : subtasks.keySet()) {
                bufferedWriter.write(CSVFormatter.toString(subtasks.get(key)));
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            bufferedWriter.write(CSVFormatter.historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных программы в файл!" + e.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(String path) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(path);
        Map<Integer, Task> tasksFromFile = new HashMap<>();
        Map<Integer, Subtask> subtasksFromFile = new HashMap<>();
        Map<Integer, Epic> epicsFromFile = new HashMap<>();
        List<Integer> historyFromFile = new ArrayList<>();
        Integer maxId = 0;
        try {
            List<String> lines = Files.readAllLines(Path.of(path));
            if (lines.isEmpty()) {
                return new FileBackedTasksManager(path);
            }
            for (String line : lines) {
                if (line.equals("id,type,name,status,description,startTime,duration,epic")) {
                    continue;
                }
                if (line.isEmpty()) {
                    if (!lines.get(lines.size() - 1).isEmpty()) {
                        historyFromFile = CSVFormatter.historyFromString(lines.get(lines.size() - 1));
                    }
                    break;
                }
                Task task = CSVFormatter.taskFromString(line);
                TypeOfTask type = task.getType();
                switch (type) {
                    case TASK:
                        tasksFromFile.put(task.getId(), task);
                        prioritizedTasks.add(task);
                        break;
                    case SUBTASK:
                        subtasksFromFile.put(task.getId(), (Subtask) task);
                        prioritizedTasks.add(task);
                        break;
                    case EPIC:
                        epicsFromFile.put(task.getId(), (Epic) task);
                        break;
                }
                if (task.getId() > maxId) {
                    maxId = task.getId();
                }
            }
        } catch (IOException | NullPointerException e) {
            throw new ManagerSaveException("Ошибка восстановления данных программы из файла!" + e.getMessage());
        }
        setId(maxId);
        setAllTasks(tasksFromFile, subtasksFromFile, epicsFromFile);
        for (Integer subtaskId : subtasksFromFile.keySet()) {
            epicsFromFile.get(subtasksFromFile.get(subtaskId).getEpicId()).addSubtaskId(subtaskId);
        }
        HistoryManager historyManager = fileBackedTasksManager.getHistoryManager();
        for (Integer id : historyFromFile) {
            Task task = null;
            if (tasksFromFile.containsKey(id)) {
                task = tasksFromFile.get(id);
            } else if (subtasksFromFile.containsKey(id)) {
                task = subtasksFromFile.get(id);
            } else if (epicsFromFile.containsKey(id)) {
                task = epicsFromFile.get(id);
            }
            historyManager.add(task);
        }
        return fileBackedTasksManager;
    }
}
