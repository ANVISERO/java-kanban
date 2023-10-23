package ru.yandex.practicum.kanban.service.managers;

import main.ru.yandex.practicum.kanban.model.Epic;
import main.ru.yandex.practicum.kanban.model.Task;
import main.ru.yandex.practicum.kanban.service.managers.FileBackedTasksManager;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @Override
    void setTaskManager() {
        taskManager = new FileBackedTasksManager(
                "test/resources/fileBackedTasksManagerTest.csv");
    }

    @Test
    void testLoadFromEmptyFile() {
        FileBackedTasksManager manager
                = FileBackedTasksManager.loadFromFile("test/resources/empty.csv");
        assertTrue(manager.getTasksList().isEmpty()
                && manager.getEpicsList().isEmpty()
                && manager.getSubtasksList().isEmpty());
    }

    @Test
    void testLoadFromFileWhereEpicHaveNoSubtasks() {
        FileBackedTasksManager fileBackedTasksManager
                = new FileBackedTasksManager("test/resources/epicHaveNoSubtasks.csv");
        Task task = new Task("task", "descTask",
                LocalDateTime.of(2023, 10, 17, 15, 5), Duration.ofMinutes(60));
        Epic epic = new Epic("epic", "descEpic");
        fileBackedTasksManager.createTask(task);
        final int epicId = fileBackedTasksManager.createEpic(epic);
        FileBackedTasksManager newFileBackedTasksManager = FileBackedTasksManager
                .loadFromFile("test/resources/epicHaveNoSubtasks.csv");
        assertTrue(newFileBackedTasksManager.getEpic(epicId).getSubtaskIds().isEmpty(),
                "Предполагалась загрузка в программу пустого эпика, но что то пошло не так.");
    }

    @Test
    void testLoadFromFileWhereHistoryIsEmpty() {
        FileBackedTasksManager fileBackedTasksManager
                = new FileBackedTasksManager("test/resources/emptyHistory.csv");
        Task task = new Task("task", "descTask",
                LocalDateTime.of(2023, 10, 17, 15, 5), Duration.ofMinutes(60));
        Epic epic = new Epic("epic", "descEpic");
        fileBackedTasksManager.createTask(task);
        fileBackedTasksManager.createEpic(epic);
        FileBackedTasksManager newFileBackedTasksManager = FileBackedTasksManager
                .loadFromFile("test/resources/emptyHistory.csv");
        assertTrue(newFileBackedTasksManager.getHistory().isEmpty(),
                "Предполагалась загрузка в программу пустой истории, но что то пошло не так.");
    }

    @Test
    void testLoadFromFileWithDefaultHistory() {
        FileBackedTasksManager fileBackedTasksManager
                = new FileBackedTasksManager("test/resources/defaultHistory.csv");
        Task task = new Task("task", "descTask",
                LocalDateTime.of(2023, 10, 17, 15, 5), Duration.ofMinutes(60));
        Task task1 = new Task("task1", "descTask1",
                LocalDateTime.of(2024, 10, 17, 15, 5), Duration.ofMinutes(60));
        Epic epic = new Epic("epic", "descEpic");
        final int taskId = fileBackedTasksManager.createTask(task);
        final int taskId1 = fileBackedTasksManager.createTask(task1);
        final int epicId = fileBackedTasksManager.createEpic(epic);
        fileBackedTasksManager.getTask(taskId);
        fileBackedTasksManager.getTask(taskId1);
        fileBackedTasksManager.getEpic(epicId);
        FileBackedTasksManager newFileBackedTasksManager = FileBackedTasksManager
                .loadFromFile("test/resources/defaultHistory.csv");
        assertFalse(newFileBackedTasksManager.getHistory().isEmpty(),
                "Предполагалась загрузка в программу пустой истории, но что то пошло не так.");
    }
}
