package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.model.*;

import java.util.ArrayList;
import java.util.List;

// В данном классе находится логика по работе с CSV файлами
public class CSVFormatter {
    // Заголовок CSV файла
    private static final String HEADER = "id,type,name,status,description,epic";

    // Возвращает заголовок CSV файла
    public static String getHeader() {
        return HEADER;
    }

    // Метод сохранения задачи или эпика в строку
    public static String toString(Task task) {
        return String.join(",", task.getId().toString(), task.getType().toString(), task.getTitle(),
                task.getStatus().toString(), task.getDescription(), "");
    }

    // Метод сохранения подзадачи в строку
    public static String toString(Subtask subtask) {
        return String.join(",", subtask.getId().toString(), subtask.getType().toString(),
                subtask.getTitle(), subtask.getStatus().toString(), subtask.getDescription(),
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
        String[] attributes = value.split(",");
        Long id = Long.parseLong(attributes[0]);
        TypeOfTask type = TypeOfTask.valueOf(attributes[1]);
        String name = attributes[2];
        Status status = Status.getEnum(attributes[3]);
        String description = attributes[4];
        switch (type) {
            case TASK:
                Task task = new Task(name, description);
                task.setId(id);
                task.setStatus(status);
                return task;
            case SUBTASK:
                Long epicId = Long.parseLong(attributes[5]);
                Subtask subtask = new Subtask(name, description, epicId);
                subtask.setId(id);
                subtask.setStatus(status);
                return subtask;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
        }
        return null;
    }

    // Метод для восстановления менеджера истории из CSV файла
    public static List<Long> historyFromString(String value) {
        List<Long> historyIds = new ArrayList<>();
        for (String id : value.split(",")) {
            historyIds.add(Long.parseLong(id));
        }
        return historyIds;
    }
}
