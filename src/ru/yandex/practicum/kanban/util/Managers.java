package ru.yandex.practicum.kanban.util;

import ru.yandex.practicum.kanban.service.*;

// Утилитарный класс, который отвечает за создание менеджеров
public class Managers {
    // Создание одной из реализаций интерфейса TaskManager (на данный момент это FileBackedTasksManager)
    public static TaskManager getDefault() {
        return new FileBackedTasksManager("src/ru/yandex/practicum/kanban/resources/storage.csv");
    }

    // Создание реализации интерфейса HistoryManager (на данный момент это InMemoryHistoryManager)
    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
