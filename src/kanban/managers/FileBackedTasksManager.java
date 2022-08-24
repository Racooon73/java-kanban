package kanban.managers;

import kanban.enums.Status;
import kanban.enums.TaskType;
import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import kanban.tasks.Task;
import kanban.util.ManagerSaveException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm - dd.MM.yyyy");
    private static final  Path PATH_SAVE = Paths.get("src\\kanban\\resources\\save.csv");

    public void save() {
        try(Writer fileWriter = new FileWriter(PATH_SAVE.toString())) {

            if (!Files.exists(Paths.get(PATH_SAVE.toString()))) {
                throw new ManagerSaveException();
            }
            fileWriter.write("id,type,name,status,description,starttime,endtime,duration,epic\n");
            if (super.getTaskList().isEmpty()){
                fileWriter.write("");
            }else{
            for(Task task :super.getTaskList().values()){
                fileWriter.write(task.toString());
            }
            }
            if(super.getSubTasksList().isEmpty()) {
                fileWriter.write("");
            }else{
            for(SubTask task :super.getSubTasksList().values()){
                fileWriter.write(task.toString());
            }
            }
            if(super.getEpicsList().isEmpty()){
                fileWriter.write("");
            }else{
            for(Epic task :super.getEpicsList().values()){
                fileWriter.write(task.toString());
            }
            }

            fileWriter.write("\n");
            for (int i = 0; i < super.getHistory().size(); i++) {
                if(i+1 != super.getHistory().size()){
                fileWriter.write(super.getHistory().get(i).getId() + ",");
                }else {
                    fileWriter.write(String.valueOf(super.getHistory().get(i).getId()));
                }
            }

        }catch (ManagerSaveException | IOException e){
            e.printStackTrace();
        }
    }

    static public FileBackedTasksManager load(Path PATH_LOAD){
        FileBackedTasksManager manager = new FileBackedTasksManager();
        try {

            if(!Files.exists(Paths.get(PATH_LOAD.toString()))){
                throw new ManagerSaveException();
            }
            String[]content = Files.readString(PATH_LOAD).split("\n");
            if(content.length == 1){
                throw new ManagerSaveException();
            }
            int i = 1;
            while (!((content[i].equals(""))||(content[i].equals("\r")))){
                if(TaskType.valueOf(content[i].split(",")[1]) == TaskType.TASK){
                    manager.addTask(taskFromString(content[i]));
                }
                if(TaskType.valueOf(content[i].split(",")[1]) == TaskType.SUBTASK){
                    SubTask subTask = subTaskFromString(content[i]);
                    manager.addSubTask(subTask);
                }
                if(TaskType.valueOf(content[i].split(",")[1]) == TaskType.EPIC){
                    manager.addEpic(epicFromString(content[i]));
                }
                i++;
            }
            i++;
            for(SubTask subTask: manager.getSubTasksList().values()){
                manager.getEpic(subTask.getEpicId()).addSubTask(subTask);
            }
           List<Integer> historyList = fromString(content[i]);
            for (Integer integer : historyList) {
                if(manager.getTaskList().containsKey(integer)){
                    manager.getTask(integer);
                }
                if(manager.getSubTasksList().containsKey(integer)){
                    manager.getSubTask(integer);
                }
                if(manager.getEpicsList().containsKey(integer)){
                    manager.getEpic(integer);
                }

            }
        }catch (ManagerSaveException | IOException e){

            return manager;
        }
        return manager;
    }
    static Task taskFromString(String value){
        String[] split = value.split(",");
        Task task = new Task(split[2],split[4], Status.valueOf(split[3]),LocalDateTime.parse(split[5],FORMATTER) ,Integer.parseInt(split[7]));
        task.setId(Integer.parseInt(split[0]));
        return task;
    }
    static SubTask subTaskFromString(String value){
        String[] split = value.split(",");
        SubTask task = new SubTask(split[2],split[4], Status.valueOf(split[3]),LocalDateTime.parse(split[5],FORMATTER) ,Integer.parseInt(split[7]));
        task.setId(Integer.parseInt(split[0]));
        task.setEpicId(Integer.parseInt(split[8].split("\r")[0]));
        return task;
    }
    static Epic epicFromString(String value){
        String[] split = value.split(",");
        Epic task = new Epic(split[2],split[4]);
        task.setId(Integer.parseInt(split[0]));
        task.setStartTime(LocalDateTime.parse(split[5],FORMATTER));
        task.setDuration(Integer.parseInt(split[7]));

        return task;
    }
    static List<Integer> fromString(String value){
        List<Integer> ids = new ArrayList<>();
        String[] split = value.split(",");
        for (String s : split) {
            ids.add(Integer.parseInt(s));
        }
        return ids;
    }
    @Override
    public void addSubTask(SubTask subtask) {
        super.addSubTask(subtask);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void removeTask(Task task) {
        super.removeTask(task);
        save();
    }

    @Override
    public void removeSubTask(SubTask subTask) {
        super.removeSubTask(subTask);
        save();
    }

    @Override
    public void removeEpic(Epic epic) {
        super.removeEpic(epic);
        save();
    }
    @Override
    public Task getTask(int id){
        Task get = super.getTask(id);
        save();
        return get;

    }
    @Override
    public SubTask getSubTask(int id){
        SubTask get = super.getSubTask(id);
        save();
        return get;
    }
    @Override
    public Epic getEpic(int id){
        Epic get = super.getEpic(id);
        save();
        return get;
    }
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }
    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }
}