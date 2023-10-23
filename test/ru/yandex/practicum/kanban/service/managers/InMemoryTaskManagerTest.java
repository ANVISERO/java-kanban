package ru.yandex.practicum.kanban.service.managers;

import main.ru.yandex.practicum.kanban.service.managers.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    void setTaskManager() {
        taskManager = new InMemoryTaskManager();
    }
}
