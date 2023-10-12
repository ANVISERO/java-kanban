package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TypeOfTask;
import ru.yandex.practicum.kanban.service.exceptions.ManagerSaveException;

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
    public Task getTask(long taskId) {
        Task task = super.getTask(taskId);
        save();
        return task;
    }

    @Override
    public long createTask(Task task) {
        long taskId = super.createTask(task);
        save();
        return taskId;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(long taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Epic getEpic(long epicId) {
        Epic epic = super.getEpic(epicId);
        save();
        return epic;
    }

    @Override
    public long createEpic(Epic epic) {
        long epicId = super.createEpic(epic);
        save();
        return epicId;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(long epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Subtask getSubtask(long subtaskId) {
        Subtask subtask = super.getSubtask(subtaskId);
        save();
        return subtask;
    }

    @Override
    public Long createSubtask(Subtask subtask) {
        Long subtaskId = super.createSubtask(subtask);
        save();
        return subtaskId;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(long subtaskId) {
        super.deleteSubtask(subtaskId);
        save();
    }

    // Данный метод сохраняет состояние программы в файл (задачи и историю)
    private void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, false))) {
            bufferedWriter.write(CSVFormatter.getHeader());
            bufferedWriter.newLine();
            for (Long key : tasks.keySet()) {
                bufferedWriter.write(CSVFormatter.toString(tasks.get(key)));
                bufferedWriter.newLine();
            }
            for (Long key : epics.keySet()) {
                bufferedWriter.write(CSVFormatter.toString(epics.get(key)));
                bufferedWriter.newLine();
            }
            for (Long key : subtasks.keySet()) {
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
        Map<Long, Task> tasksFromFile = new HashMap<>();
        Map<Long, Subtask> subtasksFromFile = new HashMap<>();
        Map<Long, Epic> epicsFromFile = new HashMap<>();
        List<Long> historyFromFile = new ArrayList<>();
        Long maxId = 0L;
        try {
            List<String> lines = Files.readAllLines(Path.of(path));
            if (lines.isEmpty()) {
                return new FileBackedTasksManager(path);
            }
            for (String line : lines) {
                if (line.equals("id,type,name,status,description,epic")) {
                    continue;
                }
                if (line.isEmpty()) {
                    historyFromFile = CSVFormatter.historyFromString(lines.get(lines.size() - 1));
                    break;
                }
                Task task = CSVFormatter.taskFromString(line);
                TypeOfTask type = task.getType();
                switch (type) {
                    case TASK:
                        tasksFromFile.put(task.getId(), task);
                        break;
                    case SUBTASK:
                        subtasksFromFile.put(task.getId(), (Subtask) task);
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
        for (Long subtaskId : subtasksFromFile.keySet()) {
            epicsFromFile.get(subtasksFromFile.get(subtaskId).getEpicId()).addSubtaskId(subtaskId);
        }
        HistoryManager historyManager = fileBackedTasksManager.getHistoryManager();
        for (Long id : historyFromFile) {
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
