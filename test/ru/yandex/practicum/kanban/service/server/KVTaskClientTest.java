package ru.yandex.practicum.kanban.service.server;

import com.google.gson.Gson;
import main.ru.yandex.practicum.kanban.model.Task;
import main.ru.yandex.practicum.kanban.service.exceptions.KVTaskClientException;
import main.ru.yandex.practicum.kanban.service.server.KVServer;
import main.ru.yandex.practicum.kanban.service.server.KVTaskClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KVTaskClientTest {

    private static final String KV_SERVER_URI = "http://localhost:8078/";
    private static Gson gson;
    private static KVTaskClient kvTaskClient;
    private static HttpClient httpClient;
    private static KVServer kvServer;

    @BeforeAll
    static void initialize() {
        gson = new Gson();
        httpClient = HttpClient.newHttpClient();
    }

    @BeforeEach
    void setUp() {
        startKVServer();
        kvTaskClient = new KVTaskClient(KV_SERVER_URI);
    }

    @AfterEach
    void shutDown() {
        stopKVServer();
    }

    @Test
    void testPutStandardBehavior() {
        Task task = new Task("Task", "descTask");
        String json = gson.toJson(task);
        kvTaskClient.put("task", json);
        HttpRequest requestTask = HttpRequest.newBuilder()
                .uri(URI.create(KV_SERVER_URI + "load/task?API_TOKEN=DEBUG"))
                .version(HttpClient.Version.HTTP_1_1)
                .GET()
                .build();
        try {
            HttpResponse<String> responseTask = httpClient.send(requestTask,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseTask.statusCode());
            assertEquals(task.toString(), gson.fromJson(responseTask.body(), Task.class).toString(),
                    "Задачи не совпадают.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется сохранение состояния менеджера задач на KVServer.");
        }
    }

    @Test
    void testPutSThrowsKVTaskClientException() {
        Task task = new Task("Task", "descTask");
        String json = gson.toJson(task);
        KVTaskClientException exception = assertThrows(KVTaskClientException.class,
                () -> kvTaskClient.put("", json));
        assertEquals("Не удалось сохранить состояние менеджера задач на KVServer.\n" +
                "Код ответа: 400", exception.getMessage());
    }

    @Test
    void testLoadStandardBehavior() {
        Task task = new Task("Task", "descTask");
        String json = gson.toJson(task);
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create(KV_SERVER_URI + "save/task?API_TOKEN=DEBUG"))
                .version(HttpClient.Version.HTTP_1_1)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> putResponse = httpClient.send(putRequest,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, putResponse.statusCode());
            assertEquals(task.toString(), gson.fromJson(kvTaskClient.load("task"), Task.class).toString(),
                    "Задачи не совпадают.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется сохранение состояния менеджера задач на KVServer.");
        }
    }

    @Test
    void testLoadSThrowsKVTaskClientException() {
        KVTaskClientException exception = assertThrows(KVTaskClientException.class,
                () -> kvTaskClient.load(""));
        assertEquals("Данные от KVServer не получены.\n" +
                "Код ответа: 400", exception.getMessage());
    }

    private void startKVServer() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void stopKVServer() {
        kvServer.stop();
    }
}
