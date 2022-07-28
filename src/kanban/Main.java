package kanban;

import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import kanban.tasks.Task;
import kanban.util.Managers;
import kanban.enums.Status;
import kanban.managers.*;

import java.nio.file.Paths;


public class Main {

    public static void main(String[] args) {

        FileBackedTasksManager manager = Managers.getFileBacked();

        Task task = new Task("Task1"," ", Status.NEW);
        Task task2 = new Task("Task2"," ",Status.IN_PROGRESS);

        Epic epic1 = new Epic("Epic1","3 subtasks ");// 3 подзадачи
        SubTask subTask1 = new SubTask("Sub1"," ",Status.DONE);
        SubTask subTask2 = new SubTask("Sub2"," ",Status.NEW);
        SubTask subTask3 = new SubTask("Sub3"," ",Status.DONE);

        Epic epic2 = new Epic("Epic2","0 subtasks ");// 0 подзадач

        manager.addTask(task);
        manager.getTask(1);
        manager.addTask(task2);
        manager.getTask(2);
        manager.getTask(2);
        manager.getTask(1);
        manager.getTask(1);
        manager.getTask(2);

        epic1.addSubTask(subTask1);
        epic1.addSubTask(subTask2);
        epic1.addSubTask(subTask3);

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        manager.getEpic(3);
        manager.getSubTask(4);
        manager.getEpic(7);
        manager.getSubTask(5);
        manager.getSubTask(6);
        manager.getEpic(7);

        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(Paths.get("src\\kanban\\resources\\save.csv"));

        System.out.println(manager2.getHistory());


    }

}
