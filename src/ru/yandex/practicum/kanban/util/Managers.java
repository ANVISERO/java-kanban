package ru.yandex.practicum.kanban.util;

import ru.yandex.practicum.kanban.service.HistoryManager;
import ru.yandex.practicum.kanban.service.InMemoryHistoryManager;
import ru.yandex.practicum.kanban.service.InMemoryTaskManager;
import ru.yandex.practicum.kanban.service.TaskManager;

// Утилитарный класс, который отвечает за создание менеджеров
public class Managers {

    // Создание одной из реализаций интерфейса TaskManager (на данный момент это InMemoryTaskManager)
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    // Создание реализации интерфейса HistoryManager (на данный момент это InMemoryHistoryManager)
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
