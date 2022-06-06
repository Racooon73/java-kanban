import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(String name, String description, String status) {
        super(name, description,status);
        subTasks = new ArrayList<>();

    }

    public void addSubTask(SubTask subTask){
        int epicId = this.getId();
        subTask.setEpicId(epicId);
        subTasks.add(subTask);
        TaskManager.epicStatus(this);
    }

    public void removeSubTask(SubTask subTask){
        subTasks.remove(subTask);
    }

    public ArrayList<SubTask> getSubTasks(){
        return subTasks;
    }
}
