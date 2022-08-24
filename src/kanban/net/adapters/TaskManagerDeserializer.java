package kanban.net.adapters;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import kanban.managers.HttpTaskManager;
import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import kanban.tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;

public class TaskManagerDeserializer implements JsonDeserializer<HttpTaskManager> {
    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(Epic.class,new EpicAdapter())
            .registerTypeAdapter(SubTask.class,new SubTaskAdapter())
            .registerTypeAdapter(Task.class, new TaskDeserializer())
            .registerTypeAdapter(SubTask.class, new SubTaskDeserializer())
            .registerTypeAdapter(Epic.class, new EpicDeserializer())
            .serializeNulls()
            .create();
    @Override
    public HttpTaskManager deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Type taskType = new TypeToken<Task>() {}.getType();
        Type subTaskType = new TypeToken<SubTask>() {}.getType();
        Type epicType = new TypeToken<Epic>() {}.getType();

        HttpTaskManager manager;
        try {
            manager = new HttpTaskManager("http://localhost:8078");

            JsonArray tasks = obj.getAsJsonArray("tasks");
            for (JsonElement task : tasks) {
               manager.addTask(gson.fromJson(task.getAsString(),taskType));

            }
            JsonArray subTasks = obj.getAsJsonArray("subtasks");
            for (JsonElement subtask : subTasks) {
                manager.addTask(gson.fromJson(subtask.getAsString(),subTaskType));

            }
            JsonArray epics = obj.getAsJsonArray("epics");
            for (JsonElement epic : epics) {
                manager.addTask(gson.fromJson(epic.getAsString(),epicType));

            }
            JsonArray historyList = obj.getAsJsonArray("history");
            for (JsonElement integer : historyList) {
                if(manager.getTaskList().containsKey(integer.getAsInt())){
                    manager.getTask(integer.getAsInt());
                }
                if(manager.getSubTasksList().containsKey(integer.getAsInt())){
                    manager.getSubTask(integer.getAsInt());
                }
                if(manager.getEpicsList().containsKey(integer.getAsInt())){
                    manager.getEpic(integer.getAsInt());
                }

            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return manager;
    }
}