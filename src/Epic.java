import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<SubTask> subTasks;

    public Epic(String name, String description, String status) {
        super(name, description,status);
        this.subTasks = new ArrayList<>();

    }

    public void addSubTask(SubTask subTask){
        int epicId = this.getId();
        subTask.setEpicId(epicId);
        subTasks.add(subTask);
        TaskManager.epicStatus(this);
    }

    public ArrayList<SubTask> getSubTasks(){
        return subTasks;
    }
}
