package ru.yandex.practicum.kanban.model;

// Данный класс представляет отдельно стоящую задачу
public class Task {
    private String title; // Название
    private String description; // Описание
    private Long id; // Уникальный идентификационный номер задачи
    private Status status; // Статус (NEW, IN_PROGRESS, DONE)

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
