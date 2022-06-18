package kanban;

import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import kanban.tasks.Task;
import kanban.util.managers.Managers;
import kanban.util.managers.TaskManager;
import kanban.util.other.Status;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Task task = new Task("Task1"," ", Status.NEW);
        Task task2 = new Task("Task2"," ",Status.IN_PROGRESS);

        Epic epic1 = new Epic("Epic1","1 subtask ");// 1 подзадача
        SubTask subTask1 = new SubTask("Sub1"," ",Status.DONE);

        Epic epic2 = new Epic("Epic2","2 subtasks ");// 2 подзадачи
        SubTask subTask2 = new SubTask("Sub2"," ",Status.NEW);
        SubTask subTask3 = new SubTask("Sub3"," ",Status.DONE);

        manager.addTask(task);
        manager.getTask(1);
        manager.addTask(task2);
        manager.getTask(2);
        epic1.addSubTask(subTask1);
        epic2.addSubTask(subTask2);
        epic2.addSubTask(subTask3);

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.getEpic(3);
        manager.getSubTask(4);
        manager.getEpic(5);
        manager.getSubTask(6);
        manager.getSubTask(7);

        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicsList());
        System.out.println(manager.getSubTasksList());

        subTask2.setStatus(Status.DONE);
        manager.updateSubTask(subTask2);

        task.setName("Updated task");
        manager.updateTask(task);

        task2.setStatus(Status.DONE);
        manager.updateTask(task2);
        System.out.println("______________");
        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicsList());
        System.out.println(manager.getSubTasksList());

        manager.removeTask(task2);
        manager.removeEpic(epic2);

        System.out.println("______________");
        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicsList());
        System.out.println(manager.getSubTasksList());

        System.out.println("______________");
        System.out.println(manager.getHistory());
        manager.getSubTask(4);
        manager.getSubTask(4);
        manager.getSubTask(4);
        manager.getSubTask(4);
        manager.getSubTask(4);
        manager.getSubTask(4);

        System.out.println(manager.getHistory());


    }

}
