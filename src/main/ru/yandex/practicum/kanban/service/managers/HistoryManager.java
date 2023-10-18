package main.ru.yandex.practicum.kanban.service.managers;

import main.ru.yandex.practicum.kanban.model.Task;

import java.util.List;

// Данный интерфейс отражает методы, которые должны быть у любого менеджера истории просмотров
public interface HistoryManager {

    // Помечает задачи как просмотренные
    void add(Task task);

    // Удаляет задачу из просмотра по id
    void remove(int id);

    // Возвращает список просмотренных задач
    List<Task> getHistory();
}
