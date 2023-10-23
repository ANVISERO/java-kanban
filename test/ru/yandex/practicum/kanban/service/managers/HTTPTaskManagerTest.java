package ru.yandex.practicum.kanban.service.managers;

import main.ru.yandex.practicum.kanban.service.managers.HttpTaskManager;
import main.ru.yandex.practicum.kanban.service.server.KVServer;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;

public class HTTPTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;
    @Override
    void setTaskManager() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        taskManager = new HttpTaskManager("http://localhost:8078/");
    }

    @AfterEach
    public void shutDown() {
        kvServer.stop();
    }

}
