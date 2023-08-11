package service;

import model.Task;

import java.util.List;

// Данный интерфейс отражает методы, которые должны быть у любого менеджера истории просмотров
public interface HistoryManager {

    // Помечает задачи как просмотренные
    void add(Task task);

    // Возвращает список просмотренных задач
    List<Task> getHistory();
}
