package ru.yandex.practicum.kanban.util;

import ru.yandex.practicum.kanban.service.*;

// Утилитарный класс, который отвечает за создание менеджеров
public class Managers {

    // Создание одной из реализаций интерфейса TaskManager (на данный момент это InMemoryTaskManager)
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    // Создание реализации интерфейса HistoryManager (на данный момент это InMemoryHistoryManager)
    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    // Создание экземпляра класса, отвечающего за хранение данных в файле
    public static FileBackedTasksManager getDefaultFileManager(final String path) {
        return new FileBackedTasksManager(path);
    }
}
