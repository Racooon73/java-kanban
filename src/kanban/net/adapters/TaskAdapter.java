package kanban.net.adapters;

import com.google.gson.*;
import kanban.enums.Status;
import kanban.tasks.Task;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class TaskAdapter implements JsonSerializer<Task> {

    @Override
    public JsonElement serialize(Task src, Type typeOfSrc,
                                 JsonSerializationContext context) {

        JsonObject obj = new JsonObject();
        obj.addProperty("id", src.getId());
        obj.addProperty("name", src.getName());
        obj.addProperty("description", src.getDescription());
        obj.addProperty("status", src.getStatus().toString());
        obj.addProperty("startTime", src.getStartTime().toString());
        obj.addProperty("duration", src.getDuration());

        return obj;
    }

}
