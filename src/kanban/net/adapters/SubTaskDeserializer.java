package kanban.net.adapters;

import com.google.gson.*;
import kanban.enums.Status;
import kanban.tasks.SubTask;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class SubTaskDeserializer implements JsonDeserializer<SubTask> {
    @Override
    public SubTask deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        SubTask task = new SubTask(obj.get("name").getAsString()
                , obj.get("description").getAsString(),
                Status.valueOf(obj.get("status").getAsString())
                , LocalDateTime.parse(obj.get("startTime").getAsString())
                , obj.get("duration").getAsInt());
        task.setId(obj.get("id").getAsInt());
        task.setEpicId(obj.get("epicId").getAsInt());
        return task;
    }
}
