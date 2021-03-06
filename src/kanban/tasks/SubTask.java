package kanban.tasks;

import kanban.enums.Status;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, Status status) {
        super(name, description,status);
        this.epicId = 0;

    }
    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "kanban.tasks.SubTask{ name= " +this.getName()+
                " epicId=" + epicId +
                ", id=" + id + " status= "+ this.getStatus()+
                '}';
    }
}
