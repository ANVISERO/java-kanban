package main.ru.yandex.practicum.kanban.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Данный класс представляет эпик, который содержит подзадачи
public class Epic extends Task {
    private List<Integer> subtaskIds;  // список с хранимым подзадачами
    private LocalDateTime endTime;  // время окончания эпика

    public Epic(String title, String description) {
        super(title, description);
        this.subtaskIds = new ArrayList<>();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    // Добавление индекса в список индексов подзадач
    public void addSubtaskId(Integer subtaskId) {
        subtaskIds.add(subtaskId);
    }

    // Удаление индекса из списка индексов подзадач
    public void deleteSubtaskId(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    // Очистка списка индексов подзадач
    public void clearSubtaskId() {
        subtaskIds.clear();
    }

    @Override
    public TypeOfTask getType() {
        return TypeOfTask.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                (getStartTime() == null ? ", startTime=null" : ", startTime='" + getStartTime() + '\'') +
                (getTaskDuration() == null ? ", taskDuration=null" : ", taskDuration='" + getTaskDuration() + '\'') +
                ", subtaskIds.size=" + subtaskIds.size() +
                '}';

    }
}
