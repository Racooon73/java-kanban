package kanban.tasks;

import kanban.enums.TaskType;
import kanban.managers.*;
import kanban.enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class Epic extends Task {
    private final ArrayList<SubTask> subTasks;
    private LocalDateTime endTime;
    public Epic(String name, String description) {
        super(name, description, Status.NEW,LocalDateTime.of(5000,12,31,23,59),0);
        this.subTasks = new ArrayList<>();

    }
    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }
    private void setEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }
    public void setDuration(int duration){
        this.duration = duration;
    }
    @Override
    public LocalDateTime getEndTime(){

        return this.startTime.plusMinutes(this.duration);
    }
    public void addSubTask(SubTask subTask){
        Optional<LocalDateTime> thisStartTimeOrNull = Optional.ofNullable(this.getStartTime());

        if(thisStartTimeOrNull.isEmpty()){
            this.setStartTime(LocalDateTime.of(5000,12,31,23,59));
        }

        if(subTask.getStartTime().isBefore(this.getStartTime())){
            this.setStartTime(subTask.getStartTime());
        }
        this.duration += subTask.getDuration();
        this.setEndTime(this.startTime.plusMinutes(this.duration));
        int epicId = this.getId();
        subTask.setEpicId(epicId);
        subTasks.add(subTask);
        InMemoryTaskManager.updateEpicStatus(this);
    }

    public ArrayList<SubTask> getSubTasks(){
        return subTasks;
    }
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm - dd.MM.yyyy");
        return id + "," + TaskType.EPIC + "," + name + "," + status + "," + description +","+
                startTime.format(formatter)+","+ this.getEndTime().format(formatter)+ ","+ duration +",\n";
    }
}
