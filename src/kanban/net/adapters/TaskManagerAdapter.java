package kanban.net.adapters;

import com.google.gson.*;
import kanban.managers.HttpTaskManager;
import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import kanban.tasks.Task;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class TaskManagerAdapter implements JsonSerializer<HttpTaskManager> {
    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(Epic.class,new EpicAdapter())
            .registerTypeAdapter(SubTask.class,new SubTaskAdapter())
            .registerTypeAdapter(Task.class, new TaskDeserializer())
            .registerTypeAdapter(SubTask.class, new SubTaskDeserializer())
            .registerTypeAdapter(Epic.class, new EpicDeserializer())
            .create();
    @Override
    public JsonElement serialize(HttpTaskManager src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.add("tasks", newJsonArray(src.getTaskList()));
        obj.add("subtasks", newJsonArray(src.getSubTasksList()));
        obj.add("epics", newJsonArray(src.getEpicsList()));
        obj.add("history", newJsonHistoryArray(src.getHistory()));

        return obj;
    }
    public static <T extends Task> JsonArray newJsonArray(Map<Integer,T> list) {
        final JsonArray jsonArray = new JsonArray();

        for(Task element : list.values())
        {
            jsonArray.add(gson.toJson(element));
        }
        return jsonArray;
    }
    public static<T extends Task> JsonArray newJsonHistoryArray(List<T> list) {
        final JsonArray jsonArray = new JsonArray();
        for(Task element : list)
        {
            jsonArray.add(element.getId());
        }
        return jsonArray;
    }
}
