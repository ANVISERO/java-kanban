package ru.yandex.practicum.kanban.model;

// Данный класс представляет подзадачу, которая входит в состав эпика
public class Subtask extends Task {
    private Long epicId; // поле для определения того, в каком эпике содержится данная задача

    public Subtask(String title, String description, Long epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Long getEpicId() {
        return epicId;
    }

    public void setEpicId(Long epicId) {
        this.epicId = epicId;
    }

    @Override
    public TypeOfTask getType() {
        return TypeOfTask.SUBTASK;
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
