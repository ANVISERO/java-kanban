package main.ru.yandex.practicum.kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

// Данный класс представляет подзадачу, которая входит в состав эпика
public class Subtask extends Task {
    private Integer epicId; // поле для определения того, в каком эпике содержится данная задача

    public Subtask(String title, String description, Integer epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, LocalDateTime startTime, Duration taskDuration,
                   Integer epicId) {
        super(title, description, startTime, taskDuration);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
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
                (getStartTime() == null ? ", startTime=null" : ", startTime='" + getStartTime() + '\'') +
                (getTaskDuration() == null ? ", taskDuration=null" : ", taskDuration='" + getTaskDuration() + '\'') +
                ", epicId=" + epicId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subtask subtask = (Subtask) o;

        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return epicId != null ? epicId.hashCode() : 0;
    }
}
