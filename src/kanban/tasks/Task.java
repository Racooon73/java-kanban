package kanban.tasks;

import kanban.enums.Status;
import kanban.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    protected String name;
    protected final String description;
    protected int id = -1;
    protected Status status;
    protected int duration;
    protected LocalDateTime startTime;

    public Task(String name, String description, Status status,LocalDateTime startTime,int duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }
    public LocalDateTime getEndTime(){
        return this.startTime.plus(Duration.ofMinutes(this.duration));
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name) &&
                Objects.equals(description, task.description) && status.equals(task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm - dd.MM.yyyy");

        return id + "," + TaskType.TASK + "," + name + "," + status + "," + description +","+
                startTime.format(formatter) +","+ this.getEndTime().format(formatter) +","+ duration +",\n";
    }

    public String getDescription() {
        return description;
    }
}
