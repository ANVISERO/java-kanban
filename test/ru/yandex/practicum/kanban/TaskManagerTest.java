package ru.yandex.practicum.kanban;

import main.ru.yandex.practicum.kanban.model.Epic;
import main.ru.yandex.practicum.kanban.model.Status;
import main.ru.yandex.practicum.kanban.model.Subtask;
import main.ru.yandex.practicum.kanban.model.Task;
import main.ru.yandex.practicum.kanban.service.managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @BeforeEach
    public void beforeEach() {
        setTaskManager();
    }

    @Test
    public void testGetTasksListStandardBehavior() {
        Task task1 = new Task("Task1", "descriptionTask1");
        Task task2 = new Task("Task2", "descriptionTask2");
        Task task3 = new Task("Task3", "descriptionTask3");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        final List<Task> tasks = taskManager.getTasksList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
        assertEquals(task2, tasks.get(1), "Задачи не совпадают.");
        assertEquals(task3, tasks.get(2), "Задачи не совпадают.");
    }

    @Test
    public void testGetTasksListFromEmptyListOfTasks() {
        final List<Task> tasks = taskManager.getTasksList();

        assertNotNull(tasks, "Задачи не возвращаются или не возвращается пустой список задач.");
        assertTrue(tasks.isEmpty(), "Список задач должен быть пуст.");
    }

    @Test
    public void testDeleteAllTasksStandardBehavior() {
        Task task1 = new Task("Task1", "descriptionTask1");
        Task task2 = new Task("Task2", "descriptionTask2");
        Task task3 = new Task("Task3", "descriptionTask3");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        taskManager.deleteAllTasks();

        final List<Task> tasks = taskManager.getTasksList();

        assertNotNull(tasks, "Задачи не возвращаются или не возвращается пустой список задач.");
        assertTrue(tasks.isEmpty(), "Задачи не были удалены.");
    }

    @Test
    public void testDeleteAllTasksWithEmptyListOfTasks() {
        taskManager.deleteAllTasks();

        final List<Task> tasks = taskManager.getTasksList();

        assertNotNull(tasks, "Задачи на возвращаются или не возвращается пустой список задач.");
        assertTrue(taskManager.getTasksList().isEmpty(), "Задачи не были удалены.");
    }

    @Test
    public void testGetTaskStandardBehavior() {
        Task task1 = new Task("Task1", "descriptionTask1");
        Task task2 = new Task("Task2", "descriptionTask2");
        Task task3 = new Task("Task3", "descriptionTask3");
        final int taskId1 = taskManager.createTask(task1);
        final int taskId2 = taskManager.createTask(task2);
        final int taskId3 = taskManager.createTask(task3);

        assertNotNull(taskManager.getTask(taskId1), "Задача с идентификатором " + taskId1 + " не найдена.");
        assertNotNull(taskManager.getTask(taskId2), "Задача с идентификатором " + taskId2 + " не найдена.");
        assertNotNull(taskManager.getTask(taskId3), "Задача с идентификатором " + taskId3 + " не найдена.");
        assertEquals(taskManager.getTask(taskId1), task1, "Задачи не совпадают.");
        assertEquals(taskManager.getTask(taskId2), task2, "Задачи не совпадают.");
        assertEquals(taskManager.getTask(taskId3), task3, "Задачи не совпадают.");
    }

    @Test
    public void testGetTaskWithInvalidTaskIDThrowsIllegalArgumentException() {
        Task task1 = new Task("Task1", "descriptionTask1");
        Task task2 = new Task("Task2", "descriptionTask2");
        Task task3 = new Task("Task3", "descriptionTask3");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.getTask(12));
        assertEquals("Задачи с идентификатором 12 не существует!", exception.getMessage());
    }

    @Test
    public void testCreateTask() {
        Task task = new Task("Task", "descriptionTask");
        final int taskId = taskManager.createTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasksList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void testUpdateTaskStandardBehavior() {
        Task task = new Task("Task", "descriptionTask");
        final int taskId = taskManager.createTask(task);

        task.setStatus(Status.DONE);
        task.setDescription("moreDescriptionTask");
        taskManager.updateTask(task);

        final Task updatedTask = taskManager.getTask(taskId);

        assertNotNull(updatedTask, "Задача не найдена.");
        assertEquals(task, updatedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasksList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void testUpdateTaskWithInvalidTaskIDThrowsIllegalArgumentException() {
        Task task1 = new Task("Task1", "descriptionTask1");
        Task task2 = new Task("Task2", "descriptionTask2");
        task2.setId(12);
        taskManager.createTask(task1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.updateTask(task2));
        assertEquals("Задачи с идентификатором 12 не существует!", exception.getMessage());
    }

    @Test
    public void testDeleteTaskStandardBehavior() {
        Task task = new Task("Task", "descriptionTask");
        final int taskId = taskManager.createTask(task);

        taskManager.deleteTask(taskId);

        final List<Task> tasks = taskManager.getTasksList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertTrue(tasks.isEmpty(), "Задача не была удалена.");
    }

    @Test
    public void testDeleteTaskWithInvalidTaskIDThrowsIllegalArgumentException() {
        Task task = new Task("Task", "descriptionTask");
        taskManager.createTask(task);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.deleteTask(12));
        assertEquals("Задачи с идентификатором 12 не существует!", exception.getMessage());
    }

    @Test
    public void testGetEpicsListStandardBehavior() {
        Epic epic1 = new Epic("epic1", "descEpic1");
        Epic epic2 = new Epic("epic2", "descEpic2");
        final int epicId1 = taskManager.createEpic(epic1);
        final int epicId2 = taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "descSubtask1", epicId1);
        Subtask subtask2 = new Subtask("subtask2", "descSubtask2", epicId1);
        Subtask subtask3 = new Subtask("subtask3", "descSubtask3", epicId2);
        Subtask subtask4 = new Subtask("subtask4", "descSubtask4", epicId2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);

        final List<Epic> epics = taskManager.getEpicsList();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество эпиков.");

        final List<Integer> subtaskIdsFirstEpic = epics.get(0).getSubtaskIds();
        final List<Integer> subtaskIdsSecondEpic = epics.get(1).getSubtaskIds();

        assertEquals(2, subtaskIdsFirstEpic.size(), "Неверное количество подзадач эпика.");
        assertEquals(2, subtaskIdsFirstEpic.size(), "Неверное количество подзадач эпика.");
        assertEquals(subtask1, taskManager.getSubtask(subtaskIdsFirstEpic.get(0)),
                "Подзадачи не совпадают.");
        assertEquals(subtask2, taskManager.getSubtask(subtaskIdsFirstEpic.get(1)),
                "Подзадачи не совпадают.");
        assertEquals(subtask3, taskManager.getSubtask(subtaskIdsSecondEpic.get(0)),
                "Подзадачи не совпадают.");
        assertEquals(subtask4, taskManager.getSubtask(subtaskIdsSecondEpic.get(1)),
                "Подзадачи не совпадают.");
    }

    @Test
    public void testGetEpicsListFromEmptyListOfTasks() {
        final List<Epic> epics = taskManager.getEpicsList();

        assertNotNull(epics, "Эпики не возвращаются или не возвращается пустой список эпиков.");
        assertTrue(epics.isEmpty(), "Эпики не были удалены.");
    }

    @Test
    public void testDeleteAllEpicsStandardBehavior() {
        Epic epic1 = new Epic("epic1", "descEpic1");
        Epic epic2 = new Epic("epic2", "descEpic2");
        final int epicId1 = taskManager.createEpic(epic1);
        final int epicId2 = taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "descSubtask1", epicId1);
        Subtask subtask2 = new Subtask("subtask2", "descSubtask2", epicId1);
        Subtask subtask3 = new Subtask("subtask3", "descSubtask3", epicId2);
        Subtask subtask4 = new Subtask("subtask4", "descSubtask4", epicId2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);

        taskManager.deleteAllEpics();

        final List<Epic> epics = taskManager.getEpicsList();
        final List<Subtask> subtasks = taskManager.getSubtasksList();

        assertNotNull(epics, "Эпики не возвращаются или не возвращается пустой список эпиков.");
        assertTrue(epics.isEmpty(), "Эпики не были удалены.");
        assertNotNull(subtasks, "Подзадачи не возвращаются или не возвращается пустой список подзадач.");
        assertTrue(subtasks.isEmpty(), "Подзадачи не были удалены.");
    }

    @Test
    public void testDeleteAllEpicsWithEmptyListOfEpics() {
        taskManager.deleteAllEpics();

        final List<Epic> epics = taskManager.getEpicsList();
        final List<Subtask> subtasks = taskManager.getSubtasksList();

        assertNotNull(epics, "Эпики не возвращаются или не возвращается пустой список эпиков.");
        assertTrue(epics.isEmpty(), "Список эпиков должен быть пуст.");
        assertNotNull(subtasks, "Подзадачи не возвращаются или не возвращается пустой список подзадач.");
        assertTrue(subtasks.isEmpty(), "Список подзадач должен быть пуст.");
    }

    @Test
    public void testGetEpicStandardBehavior() {
        Epic epic1 = new Epic("epic1", "descEpic1");
        Epic epic2 = new Epic("epic2", "descEpic2");
        final int epicId1 = taskManager.createEpic(epic1);
        final int epicId2 = taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "descSubtask1", epicId1);
        Subtask subtask2 = new Subtask("subtask2", "descSubtask2", epicId1);
        Subtask subtask3 = new Subtask("subtask3", "descSubtask3", epicId2);
        Subtask subtask4 = new Subtask("subtask4", "descSubtask4", epicId2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);

        assertNotNull(taskManager.getEpic(epicId1), "Эпик с идентификатором " + epicId1 + " не найден.");
        assertNotNull(taskManager.getEpic(epicId2), "Эпик с идентификатором " + epicId2 + " не найден.");
        assertEquals(epic1, taskManager.getEpic(epicId1), "Эпики не совпадают.");
        assertEquals(epic2, taskManager.getEpic(epicId2), "Эпики не совпадают.");
    }

    @Test
    public void testGetEpicWithInvalidEpicIDThrowsIllegalArgumentException() {
        Epic epic1 = new Epic("epic1", "descEpic1");
        Epic epic2 = new Epic("epic2", "descEpic2");
        final int epicId1 = taskManager.createEpic(epic1);
        final int epicId2 = taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "descSubtask1", epicId1);
        Subtask subtask2 = new Subtask("subtask2", "descSubtask2", epicId1);
        Subtask subtask3 = new Subtask("subtask3", "descSubtask3", epicId2);
        Subtask subtask4 = new Subtask("subtask4", "descSubtask4", epicId2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.getEpic(12));
        assertEquals("Эпика с идентификатором 12 не существует!", exception.getMessage());
    }

    @Test
    public void testCreateEpic() {
        Epic epic = new Epic("epic", "descEpic");
        final int epicId = taskManager.createEpic(epic);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpicsList();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    public void testUpdateEpicStandardBehavior() {
        Epic epic = new Epic("epic", "descEpic");
        final int epicId = taskManager.createEpic(epic);

        epic.setStatus(Status.DONE);

        taskManager.updateEpic(epic);

        final Epic updatedEpic = taskManager.getEpic(epicId);

        assertNotNull(updatedEpic, "Эпик не найдена.");
        assertEquals(epic, updatedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpicsList();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    public void testUpdateEpicWithInvalidEpicIDThrowsIllegalArgumentException() {
        Epic epic1 = new Epic("epic1", "descEpic1");
        Epic epic2 = new Epic("epic2", "descEpic2");
        taskManager.createEpic(epic1);

        epic2.setStatus(Status.DONE);
        epic2.setId(12);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.updateEpic(epic2));
        assertEquals("Эпика с идентификатором 12 не существует!", exception.getMessage());
    }

    @Test
    public void testDeleteEpicStandardBehavior() {
        Epic epic = new Epic("epic", "descEpic");
        final int epicId = taskManager.createEpic(epic);

        taskManager.deleteEpic(epicId);

        final List<Epic> epics = taskManager.getEpicsList();
        final List<Subtask> subtasks = taskManager.getSubtasksByEpic(epicId);

        assertNotNull(epics, "Эпики не возвращаются.");
        assertTrue(epics.isEmpty(), "Эпики не были удалены.");
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertTrue(subtasks.isEmpty(), "Подзадачи не были удалены.");
    }

    @Test
    public void testDeleteEpicWithInvalidEpicIDThrowsIllegalArgumentException() {
        Epic epic = new Epic("epic", "descEpic");
        taskManager.createEpic(epic);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.deleteEpic(12));
        assertEquals("Эпика с идентификатором 12 не существует!", exception.getMessage());
    }

    @Test
    public void testGetSubtasksListStandardBehavior() {
        Epic epic1 = new Epic("epic1", "descEpic1");
        Epic epic2 = new Epic("epic2", "descEpic2");
        final int epicId1 = taskManager.createEpic(epic1);
        final int epicId2 = taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "descSubtask1", epicId1);
        Subtask subtask2 = new Subtask("subtask2", "descSubtask2", epicId1);
        Subtask subtask3 = new Subtask("subtask3", "descSubtask3", epicId2);
        Subtask subtask4 = new Subtask("subtask4", "descSubtask4", epicId2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);

        final List<Subtask> subtasks = taskManager.getSubtasksList();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(subtask1, subtasks.get(0), "Подзадачи не совпадают.");
        assertEquals(subtask2, subtasks.get(1), "Подзадачи не совпадают.");
        assertEquals(subtask3, subtasks.get(2), "Подзадачи не совпадают.");
        assertEquals(subtask4, subtasks.get(3), "Подзадачи не совпадают.");
    }

    @Test
    public void testGetSubtasksListFromEmptyListOfSubtasks() {
        final List<Subtask> subtasks = taskManager.getSubtasksList();

        assertNotNull(subtasks, "Подзадачи не возвращаются или не возвращается пустой список подзадач.");
        assertTrue(subtasks.isEmpty(), "Список подзадач должен быть пуст.");
    }

    @Test
    public void testDeleteAllSubtasksStandardBehavior() {
        Epic epic1 = new Epic("epic1", "descEpic1");
        Epic epic2 = new Epic("epic2", "descEpic2");
        final int epicId1 = taskManager.createEpic(epic1);
        final int epicId2 = taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "descSubtask1", epicId1);
        Subtask subtask2 = new Subtask("subtask2", "descSubtask2", epicId1);
        Subtask subtask3 = new Subtask("subtask3", "descSubtask3", epicId2);
        Subtask subtask4 = new Subtask("subtask4", "descSubtask4", epicId2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);

        taskManager.deleteAllSubtasks();

        final List<Subtask> subtasks = taskManager.getSubtasksList();

        assertNotNull(subtasks, "Подзадачи не возвращаются или не возвращается пустой список подзадач.");
        assertTrue(subtasks.isEmpty(), "Подзадачи не были удалены.");

        assertNotNull(taskManager.getEpic(epicId1).getSubtaskIds(),
                "Список идентификаторов подзадач не возвращаются или не возвращается пустой список " +
                        "идентификаторов подзадач.");
        assertTrue(taskManager.getEpic(epicId1).getSubtaskIds().isEmpty(),
                "Список идентификаторов подзадач не был удалён.");
        assertNotNull(taskManager.getEpic(epicId1).getSubtaskIds(),
                "Список идентификаторов подзадач не возвращаются или не возвращается пустой список " +
                        "идентификаторов подзадач.");
        assertTrue(taskManager.getEpic(epicId1).getSubtaskIds().isEmpty(),
                "Список идентификаторов подзадач не был удалён.");
        assertNotNull(taskManager.getEpic(epicId2).getSubtaskIds(),
                "Список идентификаторов подзадач не возвращаются или не возвращается пустой список " +
                        "идентификаторов подзадач.");
        assertTrue(taskManager.getEpic(epicId2).getSubtaskIds().isEmpty(),
                "Список идентификаторов подзадач не был удалён.");
        assertNotNull(taskManager.getEpic(epicId2).getSubtaskIds(),
                "Список идентификаторов подзадач не возвращаются или не возвращается пустой список " +
                        "идентификаторов подзадач.");
        assertTrue(taskManager.getEpic(epicId2).getSubtaskIds().isEmpty(),
                "Список идентификаторов подзадач не был удалён.");
    }

    @Test
    public void testDeleteAllSubtasksWithEmptyListOfSubtasks() {
        taskManager.deleteAllSubtasks();

        final List<Subtask> subtasks = taskManager.getSubtasksList();

        assertNotNull(subtasks, "Подзадачи не возвращаются или не возвращается пустой список подзадач.");
        assertTrue(subtasks.isEmpty(), "Подзадачи не были удалены.");
    }

    @Test
    public void testGetSubtaskStandardBehavior() {
        Epic epic1 = new Epic("epic1", "descEpic1");
        Epic epic2 = new Epic("epic2", "descEpic2");
        final int epicId1 = taskManager.createEpic(epic1);
        final int epicId2 = taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "descSubtask1", epicId1);
        Subtask subtask2 = new Subtask("subtask2", "descSubtask2", epicId1);
        Subtask subtask3 = new Subtask("subtask3", "descSubtask3", epicId2);
        Subtask subtask4 = new Subtask("subtask4", "descSubtask4", epicId2);
        int subtaskId1 = taskManager.createSubtask(subtask1);
        int subtaskId2 = taskManager.createSubtask(subtask2);
        int subtaskId3 = taskManager.createSubtask(subtask3);
        int subtaskId4 = taskManager.createSubtask(subtask4);

        assertNotNull(taskManager.getSubtask(subtaskId1), "Подзадача с идентификатором " + subtaskId1
                + " не найдена.");
        assertNotNull(taskManager.getSubtask(subtaskId2), "Подзадача с идентификатором " + subtaskId2
                + " не найдена.");
        assertNotNull(taskManager.getSubtask(subtaskId3), "Подзадача с идентификатором " + subtaskId3
                + " не найдена.");
        assertNotNull(taskManager.getSubtask(subtaskId4), "Подзадача с идентификатором " + subtaskId4
                + " не найдена.");
        assertEquals(subtask1, taskManager.getSubtask(subtaskId1), "Подзадачи не совпадают.");
        assertEquals(subtask2, taskManager.getSubtask(subtaskId2), "Подзадачи не совпадают.");
        assertEquals(subtask3, taskManager.getSubtask(subtaskId3), "Подзадачи не совпадают.");
        assertEquals(subtask4, taskManager.getSubtask(subtaskId4), "Подзадачи не совпадают.");
    }

    @Test
    public void testGetSubtaskWithInvalidSubtaskIDThrowsIllegalArgumentException() {
        Epic epic1 = new Epic("epic1", "descEpic1");
        Epic epic2 = new Epic("epic2", "descEpic2");
        final int epicId1 = taskManager.createEpic(epic1);
        final int epicId2 = taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("subtask1", "descSubtask1", epicId1);
        Subtask subtask2 = new Subtask("subtask2", "descSubtask2", epicId1);
        Subtask subtask3 = new Subtask("subtask3", "descSubtask3", epicId2);
        Subtask subtask4 = new Subtask("subtask4", "descSubtask4", epicId2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.getSubtask(12));
        assertEquals("Подзадачи с идентификатором 12 не существует!", exception.getMessage());
    }

    @Test
    public void testCreateSubtaskStandardBehavior() {
        Epic epic = new Epic("epic", "descEpic");
        final int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("subtask", "descSubtask", epicId);
        final int subtaskId = taskManager.createSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final Epic epic1 = taskManager.getEpic(epicId);

        assertTrue(epic1.getSubtaskIds().contains(subtaskId),
                "Данная подзадача не привязана ни к какому эпику!");

        final List<Subtask> subtasks = taskManager.getSubtasksList();
        assertNotNull(subtasks, "Подзадача не найдена.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
    }

    @Test
    public void testCreateSubtaskWithInvalidSubtaskIDThrowsIllegalArgumentException() {
        Subtask subtask = new Subtask("subtask", "descSubtask", 12);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.createSubtask(subtask));
        assertEquals("Данная подзадача не привязана ни к какому из существующих эпиков!", exception.getMessage());
    }

    @Test
    public void testUpdateSubtaskStandardBehavior() {
        Epic epic = new Epic("epic", "descEpic");
        final int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("subtask", "descSubtask", epicId);
        final int subtaskId = taskManager.createSubtask(subtask);

        subtask.setStatus(Status.DONE);

        taskManager.updateSubtask(subtask);

        final Subtask updatedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(updatedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, updatedSubtask, "Подзадачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasksList();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");

        Epic updatedEpic = taskManager.getEpic(updatedSubtask.getEpicId());
        assertEquals(Status.DONE, updatedEpic.getStatus(), "Статус эпика не обновляется.");
    }

    @Test
    public void testUpdateSubtaskWithInvalidSubtaskIDThrowsIllegalArgumentException() {
        Epic epic = new Epic("epic", "descEpic");
        final int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("subtask", "descSubtask", epicId);
        taskManager.createSubtask(subtask);

        subtask.setStatus(Status.DONE);

        Subtask subtask1 = new Subtask("subtask1", "descSubtask1", 6);
        subtask1.setId(12);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.updateSubtask(subtask1));
        assertEquals("Подзадачи с идентификатором 12 не существует!", exception.getMessage());
    }

    @Test
    public void testDeleteSubtaskStandardBehavior() {
        Epic epic = new Epic("epic", "descEpic");
        final int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("subtask", "descSubtask", epicId);
        final int subtaskId = taskManager.createSubtask(subtask);

        taskManager.deleteSubtask(subtaskId);

        final List<Subtask> subtasks = taskManager.getSubtasksByEpic(epicId);

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertTrue(subtasks.isEmpty(), "Подзадачи не были удалены.");

        final Epic epic1 = taskManager.getEpic(epicId);

        assertFalse(epic1.getSubtaskIds().contains(subtaskId),
                "Идентификатор подзадачи не удалён из списка идентификаторов эпика!");
    }

    @Test
    public void testDeleteSubtaskWithInvalidSubtaskIDThrowsIllegalArgumentException() {
        Epic epic = new Epic("epic", "descEpic");
        final int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("subtask", "descSubtask", epicId);
        taskManager.createSubtask(subtask);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.deleteSubtask(12));
        assertEquals("Подзадачи с идентификатором 12 не существует!", exception.getMessage());
    }

    @Test
    public void testGetSubtaskByEpic() {
        Epic epic1 = new Epic("epic1", "descEpic1");
        final int epicId1 = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "descSubtask1", epicId1);
        Subtask subtask2 = new Subtask("subtask2", "descSubtask2", epicId1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        List<Subtask> subtasksByEpic = taskManager.getSubtasksByEpic(epicId1);

        assertNotNull(subtasksByEpic, "Список подзадач эпика не найден.");
        assertEquals(subtask1, subtasksByEpic.get(0), "Подзадачи не совпадают.");
        assertEquals(subtask2, subtasksByEpic.get(1), "Подзадачи не совпадают.");
    }

    abstract void setTaskManager();
}
