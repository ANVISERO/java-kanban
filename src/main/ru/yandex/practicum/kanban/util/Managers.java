package main.ru.yandex.practicum.kanban.util;

import main.ru.yandex.practicum.kanban.service.managers.*;

// Утилитарный класс, который отвечает за создание менеджеров
public class Managers {
    // Создание одной из реализаций интерфейса TaskManager (на данный момент это HttpTaskManager)
    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8078/");
    }

    // Создание реализации интерфейса HistoryManager (на данный момент это InMemoryHistoryManager)
    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
