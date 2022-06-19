package kanban.managers;

import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import kanban.tasks.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    HashMap<Integer,Task> getTaskList();

    HashMap<Integer, SubTask> getSubTasksList();

    HashMap<Integer, Epic> getEpicsList();

    void addTask(Task task);

    void addSubTask(SubTask subTask);

    void addEpic(Epic epic);

    Task getTask(int id);

    SubTask getSubTask(int id);

    Epic getEpic(int id);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void removeTask(Task task);

    void removeSubTask(SubTask subTask);

    void removeEpic(Epic epic);

    List<Task> getHistory();

}
