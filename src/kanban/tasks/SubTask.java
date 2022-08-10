package kanban.tasks;

import kanban.enums.Status;
import kanban.enums.TaskType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, Status status, LocalDateTime startTime, int duration) {
        super(name, description,status,startTime,duration);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm - dd.MM.yyyy");
        return id + "," + TaskType.SUBTASK + "," + name + "," + status + "," + description + "," +
                startTime.format(formatter) +","+ this.getEndTime().format(formatter)+ ","
                + duration +","+ epicId + "\n";
    }
}
