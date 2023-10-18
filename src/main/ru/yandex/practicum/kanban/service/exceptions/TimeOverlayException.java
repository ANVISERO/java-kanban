package main.ru.yandex.practicum.kanban.service.exceptions;

// Данное исключение срабатывает, когда задачи или подзадачи пересекаются по времени выполнения
public class TimeOverlayException extends RuntimeException{
    public TimeOverlayException(String message) {
        super(message);
    }
}
