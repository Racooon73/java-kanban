package kanban.net.adapters;

import com.google.gson.*;
import kanban.tasks.SubTask;
import java.lang.reflect.Type;


public class SubTaskAdapter implements JsonSerializer<SubTask> {

    @Override
    public JsonElement serialize(SubTask src, Type typeOfSrc,
                                 JsonSerializationContext context) {

        JsonObject obj = new JsonObject();
        obj.addProperty("id", src.getId());
        obj.addProperty("name", src.getName());
        obj.addProperty("description", src.getDescription());
        obj.addProperty("status", src.getStatus().toString());
        obj.addProperty("startTime", src.getStartTime().toString());
        obj.addProperty("duration", src.getDuration());
        obj.addProperty("epicId", src.getEpicId());


        return obj;
    }
}
