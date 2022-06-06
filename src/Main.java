public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        Task task = new Task("Task1"," ","NEW");
        Task task2 = new Task("Task2"," ","IN_PROGRESS");

        Epic epic1 = new Epic("Epic1","1 subtask ","");// 1 подзадача
        SubTask subTask1 = new SubTask("Sub1"," ","DONE");

        Epic epic2 = new Epic("Epic2","2 subtasks ","");// 2 подзадачи
        SubTask subTask2 = new SubTask("Sub2"," ","NEW");
        SubTask subTask3 = new SubTask("Sub3"," ","DONE");

        manager.addTask(task);
        manager.addTask(task2);
        epic1.addSubTask(subTask1);
        epic2.addSubTask(subTask2);
        epic2.addSubTask(subTask3);
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicsList());
        System.out.println(manager.getSubTasksList());

        subTask2.setStatus("DONE");
        manager.updateSubTask(subTask2);

        task.setName("Updated task");
        manager.updateTask(task);

        task2.setStatus("DONE");
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

    }

}
