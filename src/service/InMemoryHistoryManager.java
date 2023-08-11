package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

// Это менеджер, который управляет историей просмотров
public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> taskHistory;

    public InMemoryHistoryManager() {
        this.taskHistory = new ArrayList<>();
    }

    private static final int CAPACITY = 10;
    private static final byte OLDEST_INDEX_IN_HISTORY = 0;

    // Добавляет задачу в историю задач
    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        taskHistory.add(task);

        if (taskHistory.size() > CAPACITY) {
            taskHistory.remove(OLDEST_INDEX_IN_HISTORY);
        }
    }

    // Возвращает список просмотренных задач
    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(taskHistory);
    }
}
