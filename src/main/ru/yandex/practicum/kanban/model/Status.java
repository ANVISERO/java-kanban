package main.ru.yandex.practicum.kanban.model;

public enum Status {
    NEW("Новый"),
    IN_PROGRESS("В процессе"),
    DONE("Готово");

    private final String rusStatus;

    Status(String rusStatus) {
        this.rusStatus = rusStatus;
    }

    public static Status getEnum(String status) {
        switch (status) {
            case "[Новый]":
                return NEW;
            case "[В процессе]":
                return IN_PROGRESS;
            case "[Готово]":
                return DONE;
        }
        return null;
    }

    @Override
    public String toString() {
        return "[" + rusStatus + "]";
    }
}
