package main.ru.yandex.practicum.kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;

// Данный класс представляет отдельно стоящую задачу
public class Task {
    private String title; // Название
    private String description; // Описание
    private Integer id; // Уникальный идентификационный номер задачи
    private Status status; // Статус (NEW, IN_PROGRESS, DONE)
    private LocalDateTime startTime = null;    // дата, когда предполагается приступить к выполнению задачи.
    private Duration taskDuration = null;  // продолжительность задачи (в минутах)

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String title, String description, LocalDateTime startTime, Duration taskDuration) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.taskDuration = taskDuration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TypeOfTask getType() {
        return TypeOfTask.TASK;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public Duration getTaskDuration() {
        return taskDuration;
    }

    public void setTaskDuration(Duration taskDuration) {
        this.taskDuration = taskDuration;
    }

    // данный метод считает время окончания задачи
    public LocalDateTime getEndTime() {
        return startTime.plus(taskDuration);
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                (startTime == null ? ", startTime=null" : ", startTime='" + startTime + '\'') +
                (taskDuration == null ? ", taskDuration=null" : ", taskDuration='" + taskDuration + '\'') +
                '}';
    }
}
