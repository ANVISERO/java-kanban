package ru.yandex.practicum.kanban.model;

// Данный класс представляет подзадачу, которая входит в состав эпика
public class Subtask extends Task {
    private long epicId; // поле для определения того, в каком эпике содержится данная задача

    public Subtask(String title, String description, long epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    public void setEpicId(long epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}
