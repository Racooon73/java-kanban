import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer,Task> tasks;
    private final HashMap<Integer,SubTask> subTasks;
    private final HashMap<Integer,Epic> epics;
    private int ids;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.ids = 0;
    }
    public HashMap<Integer,Task> getTaskList(){
        return tasks;
    }

    public HashMap<Integer, SubTask> getSubTasksList() {
        return subTasks;
    }

    public HashMap<Integer, Epic> getEpicsList() {
        return epics;
    }

    public void addTask(Task task) {
        ids++;
        task.setId(ids);
        tasks.put(ids,task);
    }

    public void addSubTask(SubTask subTask) {
        ids++;
        subTask.setId(ids);
        subTasks.put(ids,subTask);
    }

    public void addEpic(Epic epic) {
        ids++;
        epic.setId(ids);
        for (SubTask subTask : epic.getSubTasks()) {
            this.addSubTask(subTask);
            subTask.setEpicId(epic.getId());

        }
        TaskManager.epicStatus(epic);
        epics.put(epic.getId(), epic);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        epicStatus(epics.get(subTask.getEpicId()));
    }

    public void removeTask(Task task) {
        tasks.remove(task.getId());
    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask.getId());
    }

    public void removeEpic(Epic epic) {
        for (SubTask subTask : epic.getSubTasks()) {
            removeSubTask(subTask);
        }
        epics.remove(epic.getId());
    }

    public static void epicStatus(Epic epic){
        int news = 0;
        int done = 0;
        ArrayList<SubTask> subTaskList = epic.getSubTasks();
        if(subTaskList == null){
            epic.setStatus("NEW");
            return;
        }
        for (SubTask subTask : subTaskList) {
            if (subTask.getStatus().equals("NEW"))
                news++;
            if (subTask.getStatus().equals("DONE"))
                done++;
        }

        if(news == subTaskList.size())
            epic.setStatus("NEW");
        else if(done == subTaskList.size())
            epic.setStatus("DONE");
        else
            epic.setStatus("IN_PROGRESS");


    }
}
