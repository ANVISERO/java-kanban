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
    private static Gson gson;


    public HttpTaskServer() throws IOException {
        gson = new GsonBuilder().create();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        this.httpTaskManager = (HttpTaskManager) Managers.getDefault();
        server.createContext("/tasks", this::handle);
    }

    private void handle(HttpExchange exchange) throws IOException {
        try {
            String path = "";
            String key = "";
            String body = "";
            String json = "";
            switch (exchange.getRequestMethod()) {
                case "GET":
                    path = extractPath(exchange);
                    key = extractKey(exchange);
                    if (path.isEmpty() && key.isEmpty()) {
                        Set<Task> prioritizedTasks = httpTaskManager.getPrioritizedTasks();
                        if (prioritizedTasks == null) {
                            exchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(prioritizedTasks, new TypeToken<Set<Task>>() {}.getType());
                        sendText(exchange, json);
                    } else if ("task".equals(path) && key.isEmpty()) {
                        List<Task> tasks = httpTaskManager.getTasksList();
                        if (tasks == null) {
                            exchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(tasks, new TypeToken<List<Task>>() {}.getType());
                        sendText(exchange, json);
                    } else if ("task".equals(path)) {
                        Task task = httpTaskManager.getTask(Integer.parseInt(key));
                        if (task == null) {
                            exchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(task);
                        sendText(exchange, json);
                    } else if ("history".equals(path)) {
                        List<Task> history = httpTaskManager.getHistory();
                        if (history == null) {
                            exchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(history, new TypeToken<List<Task>>() {}.getType());
                        sendText(exchange, json);
                    } else if ("subtask".equals(path) && key.isEmpty()) {
                        List<Subtask> subtasks = httpTaskManager.getSubtasksList();
                        if (subtasks == null) {
                            exchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(subtasks, new TypeToken<List<Subtask>>() {}.getType());
                        sendText(exchange, json);
                    } else if ("subtask".equals(path)) {
                        Subtask subtask = httpTaskManager.getSubtask(Integer.parseInt(key));
                        if (subtask == null) {
                            exchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(subtask);
                        sendText(exchange, json);
                    } else if ("epic".equals(path) && key.isEmpty()) {
                        List<Epic> epics = httpTaskManager.getEpicsList();
                        if (epics == null) {
                            exchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(epics, new TypeToken<List<Epic>>() {}.getType());
                        sendText(exchange, json);
                    } else if ("epic".equals(path)) {
                        Epic epic = httpTaskManager.getEpic(Integer.parseInt(key));
                        if (epic == null) {
                            exchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(epic);
                        sendText(exchange, json);
                    } else if ("subtask/epic".equals(path)) {
                        List<Subtask> subtasksByEpic = httpTaskManager.getSubtasksByEpic(Integer.parseInt(key));
                        if (subtasksByEpic == null) {
                            exchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        json = gson.toJson(subtasksByEpic, new TypeToken<List<Subtask>>() {}.getType());
                        sendText(exchange, json);
                    } else {
                        exchange.sendResponseHeaders(404, 0);
                    }
                    break;
                case "POST":
                    path = extractPath(exchange);
                    body = readText(exchange);
                    if ("task".equals(path)) {
                        Task task = gson.fromJson(body, Task.class);
                        if (task.getId() != null && httpTaskManager.containsTask(task.getId())) {
                            httpTaskManager.updateTask(task);
                        } else {
                            httpTaskManager.createTask(task);
                        }
                        exchange.sendResponseHeaders(201, 0);
                    } else if ("subtask".equals(path)) {
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        if (subtask.getId() != null && httpTaskManager.containsSubtask(subtask.getId())) {
                            httpTaskManager.updateSubtask(subtask);
                        } else {
                            httpTaskManager.createSubtask(subtask);
                        }
                        exchange.sendResponseHeaders(201, 0);
                    } else if ("epic".equals(path)) {
                        Epic epic = gson.fromJson(body, Epic.class);
                        if (epic.getId() != null && httpTaskManager.containsEpic(epic.getId())) {
                            httpTaskManager.updateEpic(epic);
                        } else {
                            httpTaskManager.createEpic(epic);
                        }
                        exchange.sendResponseHeaders(201, 0);
                    } else {
                        exchange.sendResponseHeaders(404, 0);
                    }
                    break;
                case "DELETE":
                    path = extractPath(exchange);
                    key = extractKey(exchange);
                    if ("task".equals(path) && key.isEmpty()) {
                        httpTaskManager.deleteAllTasks();
                        sendText(exchange, "All tasks deleted.");
                    } else if ("task".equals(path)) {
                        httpTaskManager.deleteTask(Integer.parseInt(key));
                        sendText(exchange, "Task with id = " + key + " deleted.");
                    } else if ("subtask".equals(path) && key.isEmpty()) {
                        httpTaskManager.deleteAllSubtasks();
                        sendText(exchange, "All subtasks deleted.");
                    } else if ("subtask".equals(path)) {
                        httpTaskManager.deleteSubtask(Integer.parseInt(key));
                        sendText(exchange, "Subtask with id = " + key + " deleted.");
                    } else if ("epic".equals(path) && key.isEmpty()) {
                        httpTaskManager.deleteAllEpics();
                        sendText(exchange, "All epics deleted.");
                    } else if ("epic".equals(path)) {
                        httpTaskManager.deleteEpic(Integer.parseInt(key));
                        sendText(exchange, "Epic with id = " + key + " deleted.");
                    } else {
                        exchange.sendResponseHeaders(404, 0);
                    }
                        break;
                    default:
                    exchange.sendResponseHeaders(404, 0);
            }
        } finally {
            exchange.close();
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

    private String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), UTF_8);
    }

    private String extractPath(HttpExchange exchange) {
        return exchange.getRequestURI().getPath().substring("/tasks/".length());
    }

    private String extractKey(HttpExchange exchange) {
        if (exchange.getRequestURI().getQuery() == null) {
            return "";
        }
        return exchange.getRequestURI().getQuery().substring("id=".length());
    }

    private void sendText(HttpExchange exchange, String json) throws IOException {
        byte[] resp = json.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }
}
