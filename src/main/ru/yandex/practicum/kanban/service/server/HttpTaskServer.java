package main.ru.yandex.practicum.kanban.service.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import main.ru.yandex.practicum.kanban.model.Epic;
import main.ru.yandex.practicum.kanban.model.Subtask;
import main.ru.yandex.practicum.kanban.model.Task;
import main.ru.yandex.practicum.kanban.service.managers.HttpTaskManager;
import main.ru.yandex.practicum.kanban.util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private final HttpTaskManager httpTaskManager;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        this.httpTaskManager = (HttpTaskManager) Managers.getDefault();
        server.createContext("/tasks", this::handle);
    }

    private void handle(HttpExchange h) throws IOException {
        try {
            String path = "";
            String key = "";
            String body = "";
            String json = "";
            switch (h.getRequestMethod()) {
                case "GET":
                    path = extractPath(h);
                    key = extractKey(h);
                    if (path.isEmpty() && key.isEmpty()) {
                        Set<Task> prioritizedTasks = httpTaskManager.getPrioritizedTasks();
                        if (prioritizedTasks == null) {
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(prioritizedTasks, new TypeToken<Set<Task>>() {}.getType());
                        sendText(h, json);
                    } else if ("task".equals(path) && key.isEmpty()) {
                        List<Task> tasks = httpTaskManager.getTasksList();
                        if (tasks == null) {
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(tasks, new TypeToken<List<Task>>() {}.getType());
                        sendText(h, json);
                    } else if ("task".equals(path)) {
                        Task task = httpTaskManager.getTask(Integer.parseInt(key));
                        if (task == null) {
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(task);
                        sendText(h, json);
                    } else if ("history".equals(path)) {
                        List<Task> history = httpTaskManager.getHistory();
                        if (history == null) {
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(history, new TypeToken<List<Task>>() {}.getType());
                        sendText(h, json);
                    } else if ("subtask".equals(path) && key.isEmpty()) {
                        List<Subtask> subtasks = httpTaskManager.getSubtasksList();
                        if (subtasks == null) {
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(subtasks, new TypeToken<List<Subtask>>() {}.getType());
                        sendText(h, json);
                    } else if ("subtask".equals(path)) {
                        Subtask subtask = httpTaskManager.getSubtask(Integer.parseInt(key));
                        if (subtask == null) {
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(subtask);
                        sendText(h, json);
                    } else if ("epic".equals(path) && key.isEmpty()) {
                        List<Epic> epics = httpTaskManager.getEpicsList();
                        if (epics == null) {
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(epics, new TypeToken<List<Epic>>() {}.getType());
                        sendText(h, json);
                    } else if ("epic".equals(path)) {
                        Epic epic = httpTaskManager.getEpic(Integer.parseInt(key));
                        if (epic == null) {
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(epic);
                        sendText(h, json);
                    } else if ("subtask/epic".equals(path)) {
                        List<Subtask> subtasksByEpic = httpTaskManager.getSubtasksByEpic(Integer.parseInt(key));
                        if (subtasksByEpic == null) {
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(subtasksByEpic, new TypeToken<List<Subtask>>() {}.getType());
                        sendText(h, json);
                    } else {
                        h.sendResponseHeaders(404, 0);
                    }
                    break;
                case "POST":
                    path = extractPath(h);
                    body = readText(h);
                    if ("task".equals(path)) {
                        Task task = gson.fromJson(body, Task.class);
                        if (task.getId() != null && httpTaskManager.containsTask(task.getId())) {
                            httpTaskManager.updateTask(task);
                        } else {
                            httpTaskManager.createTask(task);
                        }
                        h.sendResponseHeaders(201, 0);
                    } else if ("subtask".equals(path)) {
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        if (subtask.getId() != null && httpTaskManager.containsSubtask(subtask.getId())) {
                            httpTaskManager.updateSubtask(subtask);
                        } else {
                            httpTaskManager.createSubtask(subtask);
                        }
                        h.sendResponseHeaders(201, 0);
                    } else if ("epic".equals(path)) {
                        Epic epic = gson.fromJson(body, Epic.class);
                        if (epic.getId() != null && httpTaskManager.containsEpic(epic.getId())) {
                            httpTaskManager.updateEpic(epic);
                        } else {
                            httpTaskManager.createEpic(epic);
                        }
                        h.sendResponseHeaders(201, 0);
                    } else {
                        h.sendResponseHeaders(404, 0);
                    }
                    break;
                case "DELETE":
                    path = extractPath(h);
                    key = extractKey(h);
                    if ("task".equals(path) && key.isEmpty()) {
                        httpTaskManager.deleteAllTasks();
                        sendText(h, "All tasks deleted.");
                    } else if ("task".equals(path)) {
                        httpTaskManager.deleteTask(Integer.parseInt(key));
                        sendText(h, "Task with id = " + key + " deleted.");
                    } else if ("subtask".equals(path) && key.isEmpty()) {
                        httpTaskManager.deleteAllSubtasks();
                        sendText(h, "All subtasks deleted.");
                    } else if ("subtask".equals(path)) {
                        httpTaskManager.deleteSubtask(Integer.parseInt(key));
                        sendText(h, "Subtask with id = " + key + " deleted.");
                    } else if ("epic".equals(path) && key.isEmpty()) {
                        httpTaskManager.deleteAllEpics();
                        sendText(h, "All epics deleted.");
                    } else if ("epic".equals(path)) {
                        httpTaskManager.deleteEpic(Integer.parseInt(key));
                        sendText(h, "Epic with id = " + key + " deleted.");
                    } else {
                        h.sendResponseHeaders(404, 0);
                    }
                        break;
                    default:
                    h.sendResponseHeaders(404, 0);
            }
        } finally {
            h.close();
        }
    }

    public void startHttpTaskServer() {
        System.out.println("Запускаем HttpTask сервер на порту " + PORT);
        server.start();
    }

    public void stopHttpTaskServer() {
        server.stop(0);
        System.out.println("HttpTask сервер остановлен на порту " + PORT);
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    private String extractPath(HttpExchange h) {
        return h.getRequestURI().getPath().substring("/tasks/".length());
    }

    private String extractKey(HttpExchange h) {
        if (h.getRequestURI().getQuery() == null) {
            return "";
        }
        return h.getRequestURI().getQuery().substring("id=".length());
    }

    private void sendText(HttpExchange h, String json) throws IOException {
        byte[] resp = json.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
