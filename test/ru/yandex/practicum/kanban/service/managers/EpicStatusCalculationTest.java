package ru.yandex.practicum.kanban.service.managers;

import main.ru.yandex.practicum.kanban.model.Epic;
import main.ru.yandex.practicum.kanban.model.Status;
import main.ru.yandex.practicum.kanban.model.Subtask;
import main.ru.yandex.practicum.kanban.service.managers.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EpicStatusCalculationTest {
    private static final String PATH = "test/resources/epicStatusTestData.csv";
    private static FileBackedTasksManager fileBackedTasksManager;
    private static Epic epic;

    @BeforeEach
    public void beforeEach() {
        epic = new Epic("epic", "descriptionEpic");
        fileBackedTasksManager = new FileBackedTasksManager(PATH);
    }

    // Тест, который проверяет правильно ли рассчитывается статус эпика с пустым списком подзадач
    @Test
    public void testEpicStatusCalculationWithEmptySubtasks() {
        Status expectedStatus = Status.NEW;

        final int epicId = fileBackedTasksManager.createEpic(epic);
        final Epic resultEpic = fileBackedTasksManager.getEpic(epicId);
        final Status resultStatus = resultEpic.getStatus();

        assertEquals(expectedStatus, resultStatus,
                "Неправильно считается статус эпика с пустым списком подзадач");
    }

    // Тест, который проверяет правильно ли рассчитывается статус эпика, все подзадачи которого со статусом NEW
    @Test
    public void testEpicStatusCalculationWithAllNewStatusSubtasks() {
        Status expectedStatus = Status.NEW;

        final int epicId = fileBackedTasksManager.createEpic(epic);
        Subtask subtask1 = new Subtask("subtask1", "descriptionSubtask1", epicId);
        Subtask subtask2 = new Subtask("subtask2", "descriptionSubtask2", epicId);
        Subtask subtask3 = new Subtask("subtask3", "descriptionSubtask3", epicId);
        fileBackedTasksManager.createSubtask(subtask1);
        fileBackedTasksManager.createSubtask(subtask2);
        fileBackedTasksManager.createSubtask(subtask3);
        final Epic resultEpic = fileBackedTasksManager.getEpic(epicId);
        final Status resultStatus = resultEpic.getStatus();

        assertEquals(expectedStatus, resultStatus,
                "Неправильно считается статус эпика, все подзадачи которого со статусом NEW");
    }

    // Тест, который проверяет правильно ли рассчитывается статус эпика, все подзадачи которого со статусом DONE
    @Test
    public void testEpicStatusCalculationWithAllDoneStatusSubtasks() {
        Status expectedStatus = Status.DONE;

        final int epicId = fileBackedTasksManager.createEpic(epic);
        Subtask subtask1 = new Subtask("subtask1", "descriptionSubtask1", epicId);
        Subtask subtask2 = new Subtask("subtask2", "descriptionSubtask2", epicId);
        Subtask subtask3 = new Subtask("subtask3", "descriptionSubtask3", epicId);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        subtask3.setStatus(Status.DONE);
        fileBackedTasksManager.createSubtask(subtask1);
        fileBackedTasksManager.createSubtask(subtask2);
        fileBackedTasksManager.createSubtask(subtask3);
        final Epic resultEpic = fileBackedTasksManager.getEpic(epicId);
        final Status resultStatus = resultEpic.getStatus();

        assertEquals(expectedStatus, resultStatus,
                "Неправильно считается статус эпика, все подзадачи которого со статусом DONE");
    }

    // Тест, который проверяет правильно ли рассчитывается статус эпика, все подзадачи которого со статусами NEW и DONE
    @Test
    public void testEpicStatusCalculationWithAllDoneAndNewStatusSubtasks() {
        Status expectedStatus = Status.IN_PROGRESS;

        final int epicId = fileBackedTasksManager.createEpic(epic);
        Subtask subtask1 = new Subtask("subtask1", "descriptionSubtask1", epicId);
        Subtask subtask2 = new Subtask("subtask2", "descriptionSubtask2", epicId);
        Subtask subtask3 = new Subtask("subtask3", "descriptionSubtask3", epicId);
        Subtask subtask4 = new Subtask("subtask4", "descriptionSubtask4", epicId);
        subtask3.setStatus(Status.DONE);
        subtask4.setStatus(Status.DONE);
        fileBackedTasksManager.createSubtask(subtask1);
        fileBackedTasksManager.createSubtask(subtask2);
        fileBackedTasksManager.createSubtask(subtask3);
        fileBackedTasksManager.createSubtask(subtask4);
        final Epic resultEpic = fileBackedTasksManager.getEpic(epicId);
        final Status resultStatus = resultEpic.getStatus();

        assertEquals(expectedStatus, resultStatus,
                "Неправильно считается статус эпика, все подзадачи которого со статусами NEW и DONE");
    }

    // Тест, который проверяет правильно ли рассчитывается статус эпика, все подзадачи которого со статусом IN_PROGRESS
    @Test
    public void testEpicStatusCalculationWithAllInProgressStatusSubtasks() {
        Status expectedStatus = Status.IN_PROGRESS;

        final int epicId = fileBackedTasksManager.createEpic(epic);
        Subtask subtask1 = new Subtask("subtask1", "descriptionSubtask1", epicId);
        Subtask subtask2 = new Subtask("subtask2", "descriptionSubtask2", epicId);
        Subtask subtask3 = new Subtask("subtask3", "descriptionSubtask3", epicId);
        Subtask subtask4 = new Subtask("subtask4", "descriptionSubtask4", epicId);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.IN_PROGRESS);
        subtask4.setStatus(Status.IN_PROGRESS);
        fileBackedTasksManager.createSubtask(subtask1);
        fileBackedTasksManager.createSubtask(subtask2);
        fileBackedTasksManager.createSubtask(subtask3);
        fileBackedTasksManager.createSubtask(subtask4);
        final Epic resultEpic = fileBackedTasksManager.getEpic(epicId);
        final Status resultStatus = resultEpic.getStatus();

        assertEquals(expectedStatus, resultStatus,
                "Неправильно считается статус эпика, все подзадачи которого со статусом IN_PROGRESS");
    }

    // Тест, который проверяет правильно ли рассчитывается статус эпика, все подзадачи которого со статусами NEW, DONE и
    // IN_PROGRESS
    @Test
    public void testEpicStatusCalculationWithAllNewDoneAndInProgressStatusSubtasks() {
        Status expectedStatus = Status.IN_PROGRESS;

        final int epicId = fileBackedTasksManager.createEpic(epic);
        Subtask subtask1 = new Subtask("subtask1", "descriptionSubtask1", epicId);
        Subtask subtask2 = new Subtask("subtask2", "descriptionSubtask2", epicId);
        Subtask subtask3 = new Subtask("subtask3", "descriptionSubtask3", epicId);
        Subtask subtask4 = new Subtask("subtask4", "descriptionSubtask4", epicId);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.IN_PROGRESS);
        subtask4.setStatus(Status.DONE);
        fileBackedTasksManager.createSubtask(subtask1);
        fileBackedTasksManager.createSubtask(subtask2);
        fileBackedTasksManager.createSubtask(subtask3);
        fileBackedTasksManager.createSubtask(subtask4);
        final Epic resultEpic = fileBackedTasksManager.getEpic(epicId);
        final Status resultStatus = resultEpic.getStatus();

        assertEquals(expectedStatus, resultStatus,
                "Неправильно считается статус эпика, все подзадачи которого со статусами" +
                        " NEW, DONE и IN_PROGRESS");
    }
}
