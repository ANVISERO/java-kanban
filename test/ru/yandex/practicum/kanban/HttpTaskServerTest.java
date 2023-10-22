package ru.yandex.practicum.kanban;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import main.ru.yandex.practicum.kanban.model.Epic;
import main.ru.yandex.practicum.kanban.model.Subtask;
import main.ru.yandex.practicum.kanban.model.Task;
import main.ru.yandex.practicum.kanban.service.managers.HttpTaskManager;
import main.ru.yandex.practicum.kanban.service.server.HttpTaskServer;
import main.ru.yandex.practicum.kanban.service.server.KVServer;
import main.ru.yandex.practicum.kanban.util.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    HttpTaskServer httpTaskServer;
    HttpTaskManager httpTaskManager;
    HttpClient httpClient = HttpClient.newHttpClient();
    KVServer kvServer;
    private static final String PATH = "http://localhost:8080/";
    private static final Gson gson = new GsonBuilder().
            create();
    private Task task1;
    private Task task2;
    private Task task3;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask1;
    private Subtask subtask2;
    private Subtask subtask3;
    private Subtask subtask4;

    @BeforeEach
    void setUp() throws IOException {
        startAllServers();
        initTestDataForTasks();
        initTestDataForEpicsAndSubtasks();
        httpTaskManager = (HttpTaskManager) Managers.getDefault();
    }

    @AfterEach
    void shutDown() {
        stopAllServers();
    }

    @Test
    void getTasksListTest() {
        HttpRequest requestGetTasksList = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/task"))
                .GET()
                .build();
        httpTaskManager.createTask(task1);
        httpTaskManager.createTask(task2);
        httpTaskManager.createTask(task3);
        try {
            HttpResponse<String> responseGetTasksList = httpClient.send(requestGetTasksList,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGetTasksList.statusCode());
            List<Task> tasks = gson.fromJson(responseGetTasksList.body(), new TypeToken<List<Task>>() {
            }.getType());
            assertEquals(3, tasks.size());
            assertEquals(task1.toString(), tasks.get(0).toString(), "Задачи не совпадают.");
            assertEquals(task2.toString(), tasks.get(1).toString(), "Задачи не совпадают.");
            assertEquals(task3.toString(), tasks.get(2).toString(), "Задачи не совпадают.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется поучение списка всех задач.");
        }
    }

    @Test
    void getSubtasksListTest() {
        HttpRequest requestGetSubtasksList = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/subtask"))
                .GET()
                .build();
        httpTaskManager.createEpic(epic1);
        httpTaskManager.createSubtask(subtask1);
        httpTaskManager.createSubtask(subtask2);
        try {
            HttpResponse<String> responseSubtasksList = httpClient.send(requestGetSubtasksList,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseSubtasksList.statusCode());
            List<Subtask> subtasks = gson.fromJson(responseSubtasksList.body(),
                    new TypeToken<List<Subtask>>() {
                    }.getType());
            assertEquals(2, subtasks.size());
            assertEquals(subtask1.toString(), subtasks.get(0).toString(), "Подзадачи не совпадают.");
            assertEquals(subtask2.toString(), subtasks.get(1).toString(), "Подзадачи не совпадают.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется поучение списка всех подзадач.");
        }
    }

    @Test
    void getEpicsListTest() {
        HttpRequest requestGetEpicsList = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/epic"))
                .GET()
                .build();
        httpTaskManager.createEpic(epic1);
        httpTaskManager.createEpic(epic2);
        try {
            HttpResponse<String> responseGetEpicsList = httpClient.send(requestGetEpicsList,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGetEpicsList.statusCode());
            List<Epic> epics = gson.fromJson(responseGetEpicsList.body(), new TypeToken<List<Epic>>() {
            }.getType());
            assertEquals(2, epics.size());
            assertEquals(epic1.toString(), epics.get(0).toString(), "Эпики не совпадают.");
            assertEquals(epic2.toString(), epics.get(1).toString(), "Эпики не совпадают.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется поучение списка всех эпиков.");
        }
    }

    @Test
    void deleteAllTasksTest() {
        HttpRequest requestDeleteAllTasks = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/task"))
                .DELETE()
                .build();
        httpTaskManager.createTask(task1);
        httpTaskManager.createTask(task2);
        httpTaskManager.createTask(task3);
        try {
            HttpResponse<String> responseDeleteAllTasks = httpClient.send(requestDeleteAllTasks,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseDeleteAllTasks.statusCode());
            String deleteInformation = responseDeleteAllTasks.body();
            assertEquals("All tasks deleted.", deleteInformation, "Задачи не удалены.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется удаление всех задач.");
        }
    }

    @Test
    void deleteAllSubtasksTest() {
        HttpRequest requestDeleteAllSubtasks = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/subtask"))
                .DELETE()
                .build();
        httpTaskManager.createEpic(epic1);
        httpTaskManager.createEpic(epic2);
        httpTaskManager.createSubtask(subtask1);
        httpTaskManager.createSubtask(subtask2);
        httpTaskManager.createSubtask(subtask3);
        try {
            HttpResponse<String> responseDeleteAllSubtasks = httpClient.send(requestDeleteAllSubtasks,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseDeleteAllSubtasks.statusCode());
            String deleteInformation = responseDeleteAllSubtasks.body();
            assertEquals("All subtasks deleted.", deleteInformation, "Подзадачи не удалены.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется удаление всех подзадач.");
        }
    }

    @Test
    void deleteAllEpicsTest() {
        HttpRequest requestDeleteAllEpics = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/epic"))
                .DELETE()
                .build();
        httpTaskManager.createEpic(epic1);
        httpTaskManager.createEpic(epic2);
        try {
            HttpResponse<String> responseDeleteAllEpics = httpClient.send(requestDeleteAllEpics,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseDeleteAllEpics.statusCode());
            String deleteInformation = responseDeleteAllEpics.body();
            assertEquals("All epics deleted.", deleteInformation, "Эпики не удалены.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется удаление всех эпиков.");
        }
    }

    @Test
    void getTaskTest() {
        HttpRequest requestGetTask = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/task?id=1"))
                .GET()
                .build();
        httpTaskManager.createTask(task1);
        try {
            HttpResponse<String> responseGetTask = httpClient.send(requestGetTask,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGetTask.statusCode());
            Task task = gson.fromJson(responseGetTask.body(), Task.class);
            assertEquals(task1.toString(), task.toString(), "Задачи не совпадают.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется поучение задачи по id.");
        }
    }

    @Test
    void getSubtaskTest() {
        HttpRequest requestGetSubtask = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/subtask?id=2"))
                .GET()
                .build();
        httpTaskManager.createEpic(epic1);
        httpTaskManager.createSubtask(subtask1);
        try {
            HttpResponse<String> responseGetSubtask = httpClient.send(requestGetSubtask,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGetSubtask.statusCode());
            Subtask subtask = gson.fromJson(responseGetSubtask.body(), Subtask.class);
            assertEquals(subtask1.toString(), subtask.toString(), "Подзадачи не совпадают.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется поучение подзадачи по id.");
        }
    }

    @Test
    void getEpicTest() {
        HttpRequest requestGetEpic = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/epic?id=1"))
                .GET()
                .build();
        httpTaskManager.createEpic(epic1);
        try {
            HttpResponse<String> responseGetEpic = httpClient.send(requestGetEpic,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGetEpic.statusCode());
            Epic epic = gson.fromJson(responseGetEpic.body(), Epic.class);
            assertEquals(epic1.toString(), epic.toString(), "Эпики не совпадают.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется поучение эпика по id.");
        }
    }

    @Test
    void createTaskTest() {
        String json = gson.toJson(task1);
        HttpRequest requestCreateTask = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> responseCreateTask = httpClient.send(requestCreateTask,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responseCreateTask.statusCode());
            assertEquals(1, httpTaskManager.getTasksList().size());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется создание задачи.");
        }
    }

    @Test
    void updateTaskTest() {
        final int taskId = httpTaskManager.createTask(task1);
        task1.setDescription("new description");
        String json = gson.toJson(task1);
        HttpRequest requestUpdateTask = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/task"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> responseUpdateTask = httpClient.send(requestUpdateTask,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responseUpdateTask.statusCode());
            assertEquals(1, httpTaskManager.getTasksList().size());
            assertEquals(task1.toString(), httpTaskManager.getTask(taskId).toString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется обновление задачи.");
        }
    }

    @Test
    void createSubtaskTest() {
        httpTaskManager.createEpic(epic1);
        String json = gson.toJson(subtask1);
        HttpRequest requestCreateSubtask = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> responseCreateSubtask = httpClient.send(requestCreateSubtask,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responseCreateSubtask.statusCode());
            assertEquals(1, httpTaskManager.getSubtasksList().size());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется создание подзадачи.");
        }
    }

    @Test
    void updateSubtaskTest() {
        httpTaskManager.createEpic(epic1);
        final int subtaskId = httpTaskManager.createSubtask(subtask1);
        subtask1.setDescription("new description");
        String json = gson.toJson(subtask1);
        HttpRequest requestUpdateSubtask = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/subtask"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> responseUpdateSubtask = httpClient.send(requestUpdateSubtask,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responseUpdateSubtask.statusCode());
            assertEquals(1, httpTaskManager.getSubtasksList().size());
            assertEquals(subtask1.toString(), httpTaskManager.getSubtask(subtaskId).toString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется обновление подзадачи.");
        }
    }

    @Test
    void createEpicTest() {
        String json = gson.toJson(epic1);
        HttpRequest requestCreateTask = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> responseCreateEpic = httpClient.send(requestCreateTask,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responseCreateEpic.statusCode());
            assertEquals(1, httpTaskManager.getEpicsList().size());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется создание эпика.");
        }
    }

    @Test
    void updateEpicTest() {
        final int epicId = httpTaskManager.createEpic(epic1);
        epic1.setDescription("new description");
        String json = gson.toJson(epic1);
        HttpRequest requestUpdateEpic = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/epic"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> responseUpdateEpic= httpClient.send(requestUpdateEpic,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(201, responseUpdateEpic.statusCode());
            assertEquals(1, httpTaskManager.getEpicsList().size());
            assertEquals(epic1.toString(), httpTaskManager.getEpic(epicId).toString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется обновление эпика.");
        }
    }

    @Test
    void deleteTaskTest() {
        HttpRequest requestDeleteTask = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/task?id=1"))
                .DELETE()
                .build();
        httpTaskManager.createTask(task1);
        try {
            HttpResponse<String> responseDeleteTask = httpClient.send(requestDeleteTask,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseDeleteTask.statusCode());
            String deleteInformation = responseDeleteTask.body();
            assertEquals("Task with id = 1 deleted.", deleteInformation,
                    "Задача не удалена.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется удаление задачи по id.");
        }
    }

    @Test
    void deleteSubtaskTest() {
        HttpRequest requestDeleteSubtask = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/subtask?id=2"))
                .DELETE()
                .build();
        httpTaskManager.createEpic(epic1);
        httpTaskManager.createSubtask(subtask1);
        try {
            HttpResponse<String> responseDeleteSubtask = httpClient.send(requestDeleteSubtask,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseDeleteSubtask.statusCode());
            String deleteInformation = responseDeleteSubtask.body();
            assertEquals("Subtask with id = 2 deleted.", deleteInformation,
                    "Подзадача не удалена.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется удаление подзадачи по id.");
        }
    }

    @Test
    void deleteEpicTest() {
        HttpRequest requestDeleteEpic = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/epic?id=1"))
                .DELETE()
                .build();
        httpTaskManager.createEpic(epic1);
        try {
            HttpResponse<String> responseDeleteEpic = httpClient.send(requestDeleteEpic,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseDeleteEpic.statusCode());
            String deleteInformation = responseDeleteEpic.body();
            assertEquals("Epic with id = 1 deleted.", deleteInformation,
                    "Эпик не удалён.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется удаление эпика по id.");
        }
    }

    @Test
    void getSubtasksByEpicTest() {
        HttpRequest requestGetSubtasksByEpic = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/subtask/epic?id=1"))
                .GET()
                .build();
        httpTaskManager.createEpic(epic1);
        httpTaskManager.createEpic(epic2);
        httpTaskManager.createSubtask(subtask1);
        httpTaskManager.createSubtask(subtask2);
        httpTaskManager.createSubtask(subtask3);
        httpTaskManager.createSubtask(subtask4);
        try {
            HttpResponse<String> responseGetSubtasksByEpic = httpClient.send(requestGetSubtasksByEpic,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGetSubtasksByEpic.statusCode());
            List<Subtask> subtasks = gson.fromJson(responseGetSubtasksByEpic.body(),
                    new TypeToken<List<Subtask>>() {
                    }.getType());
            assertEquals(2, subtasks.size(), "Неверное количество подзадач в эпике.");
            assertEquals(subtask1.toString(), subtasks.get(0).toString(), "Подзадачи не совпадают.");
            assertEquals(subtask2.toString(), subtasks.get(1).toString(), "Подзадачи не совпадают.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется получение списка всех подзадач эпика.");
        }
    }

    @Test
    void getHistoryTest() {
        HttpRequest requestGetHistory = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/history"))
                .GET()
                .build();
        httpTaskManager.createTask(task1);
        httpTaskManager.createTask(task2);
        httpTaskManager.createTask(task3);
        HttpRequest requestGetTask3 = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/task?id=3"))
                .GET()
                .build();
        HttpRequest requestGetTask2 = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/task?id=2"))
                .GET()
                .build();
        HttpRequest requestGetTask1 = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/task?id=1"))
                .GET()
                .build();
        try {
            HttpResponse<String> responseGetTask3 = httpClient.send(requestGetTask3,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGetTask3.statusCode());
            HttpResponse<String> responseGetTask2 = httpClient.send(requestGetTask2,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGetTask2.statusCode());
            HttpResponse<String> responseGetTask1 = httpClient.send(requestGetTask1,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGetTask1.statusCode());
            HttpResponse<String> responseGetHistory = httpClient.send(requestGetHistory,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGetHistory.statusCode());
            List<Task> history = gson.fromJson(responseGetHistory.body(), new TypeToken<List<Task>>() {
            }.getType());
            assertEquals(3, history.size());
            assertEquals(task3.toString(), history.get(0).toString(), "Задачи не совпадают.");
            assertEquals(task2.toString(), history.get(1).toString(), "Задачи не совпадают.");
            assertEquals(task1.toString(), history.get(2).toString(), "Задачи не совпадают.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется поучение истории.");
        }
    }

    @Test
    void getPrioritizedTasksTest() {
        HttpRequest requestGetPrioritizedTasks = HttpRequest.newBuilder()
                .uri(URI.create(PATH + "tasks/"))
                .GET()
                .build();
        httpTaskManager.createTask(task1);
        httpTaskManager.createTask(task2);
        httpTaskManager.createTask(task3);
        try {
            HttpResponse<String> responseGetPrioritizedTasks = httpClient.send(requestGetPrioritizedTasks,
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, responseGetPrioritizedTasks.statusCode());
            Set<Task> tasks = gson.fromJson(responseGetPrioritizedTasks.body(), new TypeToken<Set<Task>>() {
            }.getType());
            assertEquals(3, tasks.size());
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка в тесте, где тестируется поучение списка всех задач по приоритетности.");
        }
    }

    private void initTestDataForTasks() {
        task1 = new Task("Task1", "descriptionTask1");
        task2 = new Task("Task2", "descriptionTask2");
        task3 = new Task("Task3", "descriptionTask3");
    }

    private void initTestDataForEpicsAndSubtasks() {
        epic1 = new Epic("epic1", "descEpic1");
        epic2 = new Epic("epic2", "descEpic2");
        subtask1 = new Subtask("subtask1", "descSubtask1", 1);
        subtask2 = new Subtask("subtask2", "descSubtask2", 1);
        subtask3 = new Subtask("subtask3", "descSubtask3", 2);
        subtask4 = new Subtask("subtask4", "descSubtask4", 2);
    }

    private void startAllServers() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.startHttpTaskServer();
    }

    private void stopAllServers() {
        httpTaskServer.stopHttpTaskServer();
        kvServer.stop();
    }
}
