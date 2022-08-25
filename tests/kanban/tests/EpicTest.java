package kanban.tests;

import kanban.enums.Status;

import kanban.managers.InMemoryTaskManager;
import kanban.managers.TaskManager;
import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    TaskManager taskManager = new InMemoryTaskManager();
    Epic epic1;
    SubTask subTask1;
    SubTask subTask2;
    @BeforeEach
    void beforeEach(){
        epic1 = new Epic("Epic1"," subtasks ");
        subTask1 = new SubTask("Sub1"," ",Status.NEW, LocalDateTime.of(2022,8,10,12,0),30);
        subTask1.setId(1);
        subTask2 = new SubTask("Sub2"," ",Status.NEW,LocalDateTime.of(2022,8,10,12,30),30);
        subTask2.setId(2);
    }
    @Test
    void addNewEpic() {
        taskManager.addEpic(epic1);
        assertThrows(IndexOutOfBoundsException.class, () -> epic1.getSubTasks().get(0),"Подзадача существует в пустом эпике");
        assertEquals(epic1.getStatus(),Status.NEW,"Неверно обновляется статус эпика");
    }
    @Test
    void addEpicWithSubTasksNew(){

        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);
        taskManager.addEpic(epic1);
        assertNotNull(epic1.getSubTasks().get(0),"Подзадачи на возвращаются.");
        assertNotNull(epic1.getSubTasks().get(1),"Подзадачи на возвращаются.");
        assertEquals(epic1.getSubTasks().size(),2,"Неверное количество подзадач");
        assertEquals(subTask1.getEpicId(),epic1.getId(),"Id эпика подзадачи не равно id эпика");
        assertEquals(epic1.getStatus(),Status.NEW,"Неверно обновляется статус эпика");
    }
    @Test
    void addEpicWithSubTasksDone(){
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);
        taskManager.addEpic(epic1);
        assertNotNull(epic1.getSubTasks().get(0),"Подзадачи на возвращаются.");
        assertNotNull(epic1.getSubTasks().get(1),"Подзадачи на возвращаются.");
        assertEquals(epic1.getSubTasks().size(),2,"Неверное количество подзадач");
        assertEquals(subTask1.getEpicId(),epic1.getId(),"Id эпика подзадачи не равно id эпика");
        assertEquals(epic1.getStatus(),Status.DONE,"Неверно обновляется статус эпика");
    }
    @Test
    void addEpicWithSubTasksNewAndDone(){
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.NEW);
        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);
        taskManager.addEpic(epic1);
        assertNotNull(epic1.getSubTasks().get(0),"Подзадачи на возвращаются.");
        assertNotNull(epic1.getSubTasks().get(1),"Подзадачи на возвращаются.");
        assertEquals(epic1.getSubTasks().size(),2,"Неверное количество подзадач");
        assertEquals(subTask1.getEpicId(),epic1.getId(),"Id эпика подзадачи не равно id эпика");
        assertEquals(epic1.getStatus(),Status.IN_PROGRESS,"Неверно обновляется статус эпика");
    }
    @Test
    void addEpicWithSubTasksInProgress(){
        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);
        taskManager.addEpic(epic1);
        assertNotNull(epic1.getSubTasks().get(0),"Подзадачи на возвращаются.");
        assertNotNull(epic1.getSubTasks().get(1),"Подзадачи на возвращаются.");
        assertEquals(epic1.getSubTasks().size(),2,"Неверное количество подзадач");
        assertEquals(subTask1.getEpicId(),epic1.getId(),"Id эпика подзадачи не равно id эпика");
        assertEquals(epic1.getStatus(),Status.IN_PROGRESS,"Неверно обновляется статус эпика");
    }


}