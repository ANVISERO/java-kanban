package main.ru.yandex.practicum.kanban.service;

import main.ru.yandex.practicum.kanban.model.*;
import main.ru.yandex.practicum.kanban.service.managers.HistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// В данном классе находится логика по работе с CSV файлами
public class CSVFormatter {
    // Заголовок CSV файла
    private static final String HEADER = "id,type,name,status,description,startTime,duration,epic";

    // Возвращает заголовок CSV файла
    public static String getHeader() {
        return HEADER;
    }

    // Метод сохранения задачи или эпика в строку
    public static String toString(Task task) {
        return String.join(",", task.getId().toString(), task.getType().toString(), task.getTitle(),
                task.getStatus().toString(), task.getDescription(),
                task.getStartTime() == null ? "" : task.getStartTime().toString(),
                task.getTaskDuration() == null ? "" : task.getTaskDuration().toString(), "");
    }

    // Метод сохранения подзадачи в строку
    public static String toString(Subtask subtask) {
        return String.join(",", subtask.getId().toString(), subtask.getType().toString(), subtask.getTitle(),
                subtask.getStatus().toString(), subtask.getDescription(),
                subtask.getStartTime() == null ? "" : subtask.getStartTime().toString(),
                subtask.getTaskDuration() == null ? "" : subtask.getTaskDuration().toString(),
                subtask.getEpicId().toString());
    }

    // Метод для сохранения менеджера истории в CSV файл
    public static String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        for (Task task : manager.getHistory()) {
            history.append(task.getId()).append(",");
        }
        if (history.toString().isEmpty()) {
            return "";
        }
        return history.substring(0, history.length() - 1);
    }

    // Метод создания задачи из строки
    public static Task taskFromString(String value) {
        String[] attributes = value.split(",", -1);
        Integer id = Integer.parseInt(attributes[0]);
        TypeOfTask type = TypeOfTask.valueOf(attributes[1]);
        String name = attributes[2];
        Status status = Status.getEnum(attributes[3]);
        String description = attributes[4];
        LocalDateTime startTime = attributes[5].isEmpty() ? null : LocalDateTime.parse(attributes[5]);
        Duration taskDuration = attributes[6].isEmpty() ? null : Duration.parse(attributes[6]);
        switch (type) {
            case TASK:
                Task task = new Task(name, description);
                task.setId(id);
                task.setStatus(status);
                task.setStartTime(startTime);
                task.setTaskDuration(taskDuration);
                return task;
            case SUBTASK:
                Integer epicId = Integer.parseInt(attributes[7]);
                Subtask subtask = new Subtask(name, description, epicId);
                subtask.setId(id);
                subtask.setStatus(status);
                subtask.setStartTime(startTime);
                subtask.setTaskDuration(taskDuration);
                return subtask;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                epic.setStartTime(startTime);
                epic.setTaskDuration(taskDuration);
                return epic;
        }
        return null;
    }

    // Метод для восстановления менеджера истории из CSV файла
    public static List<Integer> historyFromString(String value) {
        List<Integer> historyIds = new ArrayList<>();
        for (String id : value.split(",")) {
            historyIds.add(Integer.parseInt(id));
        }
        return historyIds;
    }
}
