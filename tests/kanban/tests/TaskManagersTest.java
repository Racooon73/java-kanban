package kanban.tests;

import kanban.enums.Status;
import kanban.managers.FileBackedTasksManager;
import kanban.managers.HttpTaskManager;
import kanban.managers.InMemoryTaskManager;
import kanban.managers.TaskManager;
import kanban.net.KVServer;
import kanban.net.KVTaskClient;
import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import kanban.tasks.Task;
import kanban.util.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagersTest <T extends TaskManager>{

    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;

    @BeforeEach
    void beforeEach(){

        task = new Task("TestTask", "Test description",Status.NEW, LocalDateTime.of(2022,8,10,12,0),30);
        epic = new Epic("TestEpic", "Test description");
        subTask = new SubTask("TestSubTask", "Test description", Status.NEW,LocalDateTime.of(2022,8,10,12,30),30);
        epic.addSubTask(subTask);
    }
    @Test
    void addNewTask() {
        taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final Map<Integer, Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(task.getId()), "Задачи не совпадают.");
        assertEquals(task.getEndTime(),LocalDateTime.of(2022,8,10,12,30),"Неверно рассчитывается время завершения");
    }
    @Test
    void addNewEpic() {
        taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpic(epic.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final Map<Integer, Epic> epics = taskManager.getEpicsList();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(epic.getId()), "Эпики не совпадают.");
        assertEquals(epic.getEndTime(),LocalDateTime.of(2022,8,10,13,0),"Неверно рассчитывается время завершения");
    }
    @Test
    void addNewSubTask() {
        taskManager.addSubTask(subTask);
        final SubTask SavedSubTask = taskManager.getSubTask(subTask.getId());

        assertNotNull(SavedSubTask, "Подзадача не найдена.");

        final Map<Integer, SubTask> subTasks = taskManager.getSubTasksList();

        assertNotNull(subTasks, "Подзадачи на возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
        assertEquals(subTask, subTasks.get(subTask.getId()), "Подзадачи не совпадают.");
        assertEquals(subTask.getEpicId(),epic.getId(),"Id эпика подзадачи не равно id эпика");
        assertEquals(subTask.getEndTime(),LocalDateTime.of(2022,8,10,13,0),
                "Неверно рассчитывается время завершения");
    }
    @Test
    void removeTask() {
        taskManager.addTask(task);
        int id = task.getId();

        taskManager.removeTask(task);

        assertThrows(NullPointerException.class,()->taskManager.getTask(id),"Задача не удалена");
    }
    @Test
    void removeSubTask() {
        taskManager.addSubTask(subTask);
        int id = subTask.getId();

        taskManager.removeSubTask(subTask);
        assertThrows(NullPointerException.class,()->taskManager.getSubTask(id),"Задача не удалена");
    }
    @Test
    void removeEpic() {
        taskManager.addEpic(epic);
        int id = epic.getId();

        taskManager.removeEpic(epic);

        assertThrows(NullPointerException.class,()->taskManager.getEpic(id),"Задача не удалена");
    }
    @Test
    void updateTask() {
        taskManager.addTask(task);
        task.setName("Updated task");

        taskManager.updateTask(task);
        assertEquals(taskManager.getTask(task.getId()).getName(),"Updated task","Задача не обновилась");

    }
    @Test
    void updateSubTask() {
        taskManager.addEpic(epic);
        subTask.setName("Updated subtask");
        epic.addSubTask(subTask);
        taskManager.updateSubTask(subTask);
        assertEquals(taskManager.getSubTask(subTask.getId()).getName(),"Updated subtask",
                "Подзадача не обновилась");

    }

}


 class InMemoryTaskManagerTest extends TaskManagersTest<InMemoryTaskManager>{

     @BeforeEach
     public void init(){
     taskManager = new InMemoryTaskManager();
         task = new Task("TestTask", "Test description",Status.NEW, LocalDateTime.of(2022,8,10,12,0),30);
         epic = new Epic("TestEpic", "Test description");
         subTask = new SubTask("TestSubTask", "Test description", Status.NEW,LocalDateTime.of(2022,8,10,12,30),30);
         epic.addSubTask(subTask);
     }
     @Test
     void validationTest(){
         taskManager.addTask(task);
         taskManager.addEpic(epic);
         assertEquals(taskManager.validation(),true,"Задачи не верно проходят валидацию");
     }
     @Test
     void inValidationTest(){
         Task inValidTask = new Task("A","B",Status.NEW,LocalDateTime.of(2022,8,10,12,0),50) ;
         taskManager.addTask(inValidTask);
         taskManager.addTask(task);
         taskManager.addEpic(epic);
         assertEquals(taskManager.validation(),false,"Задачи не верно проходят валидацию");
     }

 }
 class FileBackedManagerTest extends TaskManagersTest<FileBackedTasksManager>{

     @BeforeEach
     public void init(){
         taskManager = new FileBackedTasksManager();
     }
     @Test
     void loadFromFileHistoryTest(){
         List<Task> saveHistory = taskManager.getHistory();
         taskManager.save();
         FileBackedTasksManager taskManager2 =
                 FileBackedTasksManager.load(Paths.get("src\\kanban\\resources\\save.csv"));

          assertEquals(saveHistory,taskManager2.getHistory(),"Неверная загрузка из пустого файла");
     }
     @Test
     void loadFromFileOneEpicTest(){

         Epic epic1 = new Epic("Epic1","0 subtasks ");
         taskManager.addEpic(epic1);
         Epic saveEpic = taskManager.getEpic(epic1.getId());
         List<Task> saveHistory = taskManager.getHistory();
         FileBackedTasksManager taskManager2 =
                 FileBackedTasksManager.load(Paths.get("src\\kanban\\resources\\save.csv"));

         assertEquals(saveEpic,taskManager2.getEpicsList().get(1),"Неверная загрузка из файла");
         assertEquals(saveHistory,taskManager2.getHistory(),"Неверная загрузка из файла");
     }


    }
class HttpTaskManagerTest extends TaskManagersTest<HttpTaskManager>{
    KVServer kvserver;
    KVTaskClient client;
    @BeforeEach
    public void init() throws IOException, InterruptedException {
        kvserver = new KVServer();
         kvserver.start();
        taskManager = Managers.getDefault();
        client = new KVTaskClient("http://localhost:8078");
        taskManager.setClient(client);
    }
    @AfterEach
    public void close(){
        kvserver.stop();
    }
    @Test
    void loadFromServerHistoryTest() throws IOException, InterruptedException {
        List<Task> saveHistory = taskManager.getHistory();
        taskManager.save();

        taskManager = taskManager.load();
        assertEquals(saveHistory,taskManager.getHistory(),"Неверная загрузка из пустого сервера");
    }
    @Test
    void loadFromServerOneEpicTest() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Epic1","0 subtasks ");
        taskManager.addEpic(epic1);
        Epic saveEpic = taskManager.getEpic(epic1.getId());
        List<Task> saveHistory = taskManager.getHistory();


        taskManager.load();

        assertEquals(saveEpic,taskManager.getEpicsList().get(1),"Неверная загрузка с сервера");
        assertEquals(saveHistory,taskManager.getHistory(),"Неверная загрузка с сервера");
    }


}



