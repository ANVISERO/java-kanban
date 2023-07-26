package model;

// Данный класс представляет отдельно стоящую задачу
public class Task {
    private String title; // Название
    private String description; // Описание
    private long id; // Уникальный идентификационный номер задачи
    private String status; // Статус (NEW, IN_PROGRESS, DONE)

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = "NEW";
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
