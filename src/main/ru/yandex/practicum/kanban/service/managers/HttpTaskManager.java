package main.ru.yandex.practicum.kanban.service.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import main.ru.yandex.practicum.kanban.model.Epic;
import main.ru.yandex.practicum.kanban.model.Subtask;
import main.ru.yandex.practicum.kanban.model.Task;
import main.ru.yandex.practicum.kanban.service.server.KVTaskClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private static final String TASKS = "tasks";
    private static final String SUBTASKS = "subtasks";
    private static final String EPICS = "epics";
    private static final String HISTORY = "history";
    private static final Gson gson = new GsonBuilder().create();

    public HttpTaskManager(final String uri) {
        super(uri);
        this.kvTaskClient = new KVTaskClient(uri);
    }

    @Override
    protected void save() {
        kvTaskClient.put(TASKS, gson.toJson(getTasksList()));
        kvTaskClient.put(SUBTASKS, gson.toJson(getSubtasksList()));
        kvTaskClient.put(EPICS, gson.toJson(getEpicsList()));
        kvTaskClient.put(HISTORY, gson.toJson(getHistory()));
    }

    public void loadTasksFromKVServer() {
        String tasksStringJson = kvTaskClient.load(TASKS);
        String subtasksStringJson = kvTaskClient.load(SUBTASKS);
        String epicsStringJson = kvTaskClient.load(EPICS);
        String historyStringJson = kvTaskClient.load(HISTORY);
        List<Task> tasksFromKVServer = new ArrayList<>();
        List<Subtask> subtasksFromKVServer = new ArrayList<>();
        List<Epic> epicsFromKVServer = new ArrayList<>();
        List<Task> historyFromKVServer = new ArrayList<>();

        if (tasksStringJson != null) {
            tasksFromKVServer = gson.fromJson(tasksStringJson, new TypeToken<List<Task>>(){}.getType());
        }
        if (subtasksStringJson != null) {
            subtasksFromKVServer = gson.fromJson(subtasksStringJson, new TypeToken<List<Subtask>>(){}.getType());
        }
        if (epicsStringJson != null) {
            epicsFromKVServer = gson.fromJson(epicsStringJson, new TypeToken<List<Epic>>(){}.getType());
        }
        if (historyStringJson != null) {
            historyFromKVServer = gson.fromJson(historyStringJson, new TypeToken<List<Task>>(){}.getType());
        }

        Integer maxId = 0;
        for (Task task : tasksFromKVServer) {
            tasks.put(task.getId(), task);
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        for (Subtask subtask : subtasksFromKVServer) {
            subtasks.put(subtask.getId(), subtask);
            if (subtask.getId() > maxId) {
                maxId = subtask.getId();
            }
        }
        for (Epic epic : epicsFromKVServer) {
            epics.put(epic.getId(), epic);
            if (epic.getId() > maxId) {
                maxId = epic.getId();
            }
        }
        for (Task task : historyFromKVServer) {
            getHistoryManager().add(task);
        }
        for (Integer subtaskId : subtasks.keySet()) {
            epics.get(subtasks.get(subtaskId).getEpicId()).addSubtaskId(subtaskId);
        }
    }

    public boolean containsTask(Integer id) {
        return tasks.containsKey(id);
    }

    public boolean containsSubtask(Integer id) {
        return subtasks.containsKey(id);
    }
    public boolean containsEpic(Integer id) {
        return epics.containsKey(id);
    }
}
