package kanban.tasks;

import kanban.util.managers.*;
import kanban.util.other.Status;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        this.subTasks = new ArrayList<>();

    }

    public void addSubTask(SubTask subTask){
        int epicId = this.getId();
        subTask.setEpicId(epicId);
        subTasks.add(subTask);
        InMemoryTaskManager.epicStatus(this);
    }

    public ArrayList<SubTask> getSubTasks(){
        return subTasks;
    }
}
