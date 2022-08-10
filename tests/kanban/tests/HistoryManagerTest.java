package kanban.tests;

import kanban.enums.Status;
import kanban.managers.InMemoryHistoryManager;
import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import kanban.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import kanban.managers.HistoryManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    HistoryManager historyManager = new InMemoryHistoryManager();
    Task task;
    Epic epic;
    SubTask subTask;
    @BeforeEach
    void beforeEach(){

        task = new Task("TestTask", "Test description", Status.NEW, LocalDateTime.of(2022,8,10,12,0),30);
        task.setId(1);
        epic = new Epic("TestEpic", "Test description");
        epic.setId(2);
        subTask = new SubTask("TestSubTask", "Test description", Status.NEW,LocalDateTime.of(2022,8,10,12,0),30);
        epic.addSubTask(subTask);
        subTask.setId(3);
    }
    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(1, history.size(), "История пустая.");
    }
    @Test
    void addDoubleTask() {
        historyManager.add(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(1, history.size(), "Дублирование задач");
    }
    @Test
    void removeFromHead(){
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.remove(task.getId());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(2, history.size(), "Задача не удалена");
    }
    @Test
    void removeFromMiddle(){
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.remove(epic.getId());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(2, history.size(), "Задача не удалена");
    }
    @Test
    void removeFromTail(){

        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.add(task);
        historyManager.remove(task.getId());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(2, history.size(), "Задача не удалена");
    }
}
