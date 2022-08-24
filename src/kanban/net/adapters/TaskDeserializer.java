package kanban.net.adapters;

import com.google.gson.*;
import kanban.enums.Status;
import kanban.tasks.Task;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class TaskDeserializer implements JsonDeserializer<Task> {
    @Override
    public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        Task task = new Task(obj.get("name").getAsString()
                , obj.get("description").getAsString(),
                Status.valueOf(obj.get("status").getAsString())
                , LocalDateTime.parse(obj.get("startTime").getAsString())
                , obj.get("duration").getAsInt());
        task.setId(obj.get("id").getAsInt());
        return task;
    }
}
