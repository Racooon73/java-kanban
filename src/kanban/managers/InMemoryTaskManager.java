package kanban.managers;

import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import kanban.tasks.Task;
import kanban.enums.Status;

import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer,Task> tasks;
    private final HashMap<Integer,SubTask> subTasks;
    private final HashMap<Integer, Epic> epics;
    private final HistoryManager historyManager;
    final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private int id;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.id = 0;
        this.historyManager = ManagerProvider.getDefaultHistory();
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
        id++;
        if(task.getId() == -1){
        task.setId(id);
        tasks.put(id,task);
        }else{
            tasks.put(task.getId(),task);
        }
        prioritizedTasks.add(task);
    }
    @Override
    public void addSubTask(SubTask subTask) {
        id++;
        if(subTask.getId() == -1){
        subTask.setId(id);
        subTasks.put(id,subTask);
        }else{
            subTasks.put(subTask.getId(),subTask);
        }
        prioritizedTasks.add(subTask);
    }
    @Override
    public void addEpic(Epic epic) {
        id++;
        if(epic.getId() == -1){
        epic.setId(id);
        for (SubTask subTask : epic.getSubTasks()) {
            this.addSubTask(subTask);
            subTask.setEpicId(epic.getId());
        }

        }else{
            for (SubTask subTask : epic.getSubTasks()) {
                this.addSubTask(subTask);
                subTask.setEpicId(epic.getId());
            }
        }
        InMemoryTaskManager.updateEpicStatus(epic);
        epics.put(epic.getId(), epic);

    }
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }
    @Override
    public void updateSubTask(SubTask subTask) {

        subTasks.put(subTask.getId(), subTask);
        InMemoryTaskManager.updateEpicStatus(epics.get(subTask.getEpicId()));
    }
    @Override
    public void removeTask(Task task) {
        tasks.remove(task.getId());
        historyManager.remove(task.getId());
        prioritizedTasks.remove(task);
    }
    @Override
    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask.getId());
        historyManager.remove(subTask.getId());
        prioritizedTasks.remove(subTask);
    }
    @Override
    public void removeEpic(Epic epic) {
        for (SubTask subTask : epic.getSubTasks()) {
            removeSubTask(subTask);
        }
        epics.remove(epic.getId());
        historyManager.remove(epic.getId());

    }
    public TreeSet<Task> getPrioritizedTasks(){
        return prioritizedTasks;
    }
    public boolean validation(){
        Task before = getPrioritizedTasks().first();


        for (Task task: getPrioritizedTasks()) {
            LocalDateTime beforeEndTime = before.getEndTime();
            LocalDateTime taskStartTime = task.getStartTime();

            if(beforeEndTime.isAfter(taskStartTime) && before != task){
                return false;
            }
            before = task;
        }
        return true;
    }
    public static void updateEpicStatus(Epic epic){
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
