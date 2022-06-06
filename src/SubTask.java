public class SubTask extends Task {
 private int epicId;

    public SubTask(String name, String description, String status) {
        super(name, description,status);
        epicId = 0;

    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{ name= " +this.getName()+
                " epicId=" + epicId +
                ", id=" + id + " status= "+ this.getStatus()+
                '}';
    }
}
