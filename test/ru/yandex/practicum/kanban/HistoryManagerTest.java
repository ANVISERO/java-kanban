package ru.yandex.practicum.kanban;

import main.ru.yandex.practicum.kanban.model.Epic;
import main.ru.yandex.practicum.kanban.model.Subtask;
import main.ru.yandex.practicum.kanban.model.Task;
import main.ru.yandex.practicum.kanban.service.managers.HistoryManager;
import main.ru.yandex.practicum.kanban.util.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    private static HistoryManager historyManager;
    private static Task task1;
    private static Task task2;
    private static Task task3;

    @BeforeEach
    public void setup() {
        setHistoryManager();
        initTestDataForTasks();
    }

    @Test
    void testAddTaskToHistory() {
        task1.setId(12);
        historyManager.add(task1);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая, но не должна быть таковой.");
        assertEquals(1, history.size(), "Количество элементов в истории должно быть равно 1.");
    }

    @Test
    void testEmptyHistory() {
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История должна быть пустой, но сейчас она таковой не является.");
        assertTrue(history.isEmpty(), "История должна быть пустой, но сейчас она таковой не является.");
    }

    @Test
    void testAddTwoSameTasksToHistory() {
        task1.setId(12);
        historyManager.add(task1);
        historyManager.add(task1);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая, но не должна быть таковой.");
        assertEquals(1, history.size(), "Неверное количество элементов в истории.");
    }

    @Test
    public void testDeleteTaskFromTheBeginningOfTheHistory() {
        task1.setId(1);
        historyManager.add(task1);
        task2.setId(2);
        historyManager.add(task2);
        task3.setId(3);
        historyManager.add(task3);
        historyManager.remove(1);
        final List<Task> history = historyManager.getHistory();
        assertFalse(history.contains(task1), "Задача не удалена из истории.");
        assertEquals(2, history.size(), "Неверное количество элементов в истории.");
    }

    @Test
    public void testDeleteTaskFromTheMiddleOfTheHistory() {
        task1.setId(1);
        historyManager.add(task1);
        task2.setId(2);
        historyManager.add(task2);
        task3.setId(3);
        historyManager.add(task3);
        historyManager.remove(2);
        final List<Task> history = historyManager.getHistory();
        assertFalse(history.contains(task2), "Задача не удалена из истории.");
        assertEquals(2, history.size(), "Неверное количество элементов в истории.");
    }

    @Test
    public void testDeleteTaskFromTheEndOfTheHistory() {
        task1.setId(1);
        historyManager.add(task1);
        task2.setId(2);
        historyManager.add(task2);
        task3.setId(3);
        historyManager.add(task3);
        historyManager.remove(3);
        final List<Task> history = historyManager.getHistory();
        assertFalse(history.contains(task3), "Задача не удалена из истории.");
        assertEquals(2, history.size(), "Неверное количество элементов в истории.");
    }

    void setHistoryManager() {
        historyManager = Managers.getDefaultHistoryManager();
    }

    private void initTestDataForTasks() {
        task1 = new Task("Task1", "descriptionTask1");
        task2 = new Task("Task2", "descriptionTask2");
        task3 = new Task("Task3", "descriptionTask3");
    }
}
