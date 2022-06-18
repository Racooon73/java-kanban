package kanban.util.managers;

import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import kanban.tasks.Task;
import kanban.util.other.Status;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer,Task> tasks;
    private final HashMap<Integer,SubTask> subTasks;
    private final HashMap<Integer, Epic> epics;
    private final HistoryManager historyManager;

    private int ids;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.ids = 0;
        this.historyManager = Managers.getDefaultHistory();
    }
    @Override
    public HashMap<Integer,Task> getTaskList(){
        return tasks;
    }
    @Override
    public HashMap<Integer, SubTask> getSubTasksList() {
        return subTasks;
    }
    @Override
    public HashMap<Integer, Epic> getEpicsList() {
        return epics;
    }
    @Override
    public Task getTask(int id){
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }
    @Override
    public SubTask getSubTask(int id){
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }
    @Override
    public Epic getEpic(int id){
        historyManager.add(epics.get(id));
        return epics.get(id);
    }
    @Override
    public void addTask(Task task) {
        ids++;
        task.setId(ids);
        tasks.put(ids,task);
    }
    @Override
    public void addSubTask(SubTask subTask) {
        ids++;
        subTask.setId(ids);
        subTasks.put(ids,subTask);
    }
    @Override
    public void addEpic(Epic epic) {
        ids++;
        epic.setId(ids);
        for (SubTask subTask : epic.getSubTasks()) {
            this.addSubTask(subTask);
            subTask.setEpicId(epic.getId());

        }
        InMemoryTaskManager.epicStatus(epic);
        epics.put(epic.getId(), epic);
    }
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }
    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        InMemoryTaskManager.epicStatus(epics.get(subTask.getEpicId()));
    }
    @Override
    public void removeTask(Task task) {
        tasks.remove(task.getId());
    }
    @Override
    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask.getId());
    }
    @Override
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
            epic.setStatus(Status.NEW);
            return;
        }
        for (SubTask subTask : subTaskList) {
            if (subTask.getStatus() == Status.NEW)
                news++;
            if (subTask.getStatus() == Status.DONE)
                done++;
        }

        if(news == subTaskList.size())
            epic.setStatus(Status.NEW);
        else if(done == subTaskList.size())
            epic.setStatus(Status.DONE);
        else
            epic.setStatus(Status.IN_PROGRESS);

    }
    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

}
