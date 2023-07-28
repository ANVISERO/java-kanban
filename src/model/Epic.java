package model;

import java.util.ArrayList;

// Данный класс представляет эпик, который содержит подзадачи
public class Epic extends Task {
    private ArrayList<Long> subtaskIds;  // список с хранимым подзадачами

    public Epic(String title, String description) {
        super(title, description);
        this.subtaskIds = new ArrayList<>();
    }

    public ArrayList<Long> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Long> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    // Добавление индекса в список индексов подзадач
    public void addSubtaskId(long subtaskId) {
        subtaskIds.add(subtaskId);
    }

    // Удаление индекса из списка индексов подзадач
    public void deleteSubtaskId(long subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    // Очистка списка индексов подзадач
    public void clearSubtaskId() {
        subtaskIds.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", subtaskIds.size=" + subtaskIds.size() +
                '}';

    }
}