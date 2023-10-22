package main.ru.yandex.practicum.kanban.service.exceptions;

public class KVTaskClientException extends RuntimeException {
    public KVTaskClientException(String message) {
        super(message);
    }
}
