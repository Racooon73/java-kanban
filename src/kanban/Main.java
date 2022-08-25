package kanban;


import kanban.net.HttpTaskServer;
import kanban.net.KVServer;
import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import kanban.tasks.Task;
import kanban.managers.ManagerProvider;
import kanban.enums.Status;
import kanban.managers.*;

import java.io.IOException;
import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvserver = new KVServer();
        kvserver.start();
        HttpTaskManager manager = ManagerProvider.getDefault();

        Task task = new Task("Task1"," ", Status.NEW, LocalDateTime.of(2022,8,10,12,45),30);
        Task task2 = new Task("Task2"," ",Status.IN_PROGRESS,LocalDateTime.of(2022,8,10,12,30),15);

        Epic epic1 = new Epic("Epic1","3 subtasks ");// 3 подзадачи
        SubTask subTask1 = new SubTask("Sub1"," ",Status.DONE,LocalDateTime.of(2022,8,10,12,0),30);
        SubTask subTask2 = new SubTask("Sub2"," ",Status.NEW,LocalDateTime.of(2022,8,10,12,0),30);
        SubTask subTask3 = new SubTask("Sub3"," ",Status.DONE,LocalDateTime.of(2022,8,10,12,0),30);

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






        //manager.addTask(new Task("TestTask", "Test description",Status.NEW, LocalDateTime.of(2022,8,10,12,0),30));

        manager.load();

        System.out.println(manager.getTask(1));
        System.out.println(manager.getHistory());

        HttpTaskServer server2 = new HttpTaskServer();
        server2.start();

    }

}
