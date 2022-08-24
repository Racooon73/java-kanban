package kanban.net.adapters;

import com.google.gson.*;
import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import java.lang.reflect.Type;
import java.util.List;

public class EpicAdapter implements JsonSerializer<Epic> {

    @Override
    public JsonElement serialize(Epic src, Type typeOfSrc,
                                 JsonSerializationContext context) {

        JsonObject obj = new JsonObject();
        obj.addProperty("id", src.getId());
        obj.addProperty("name", src.getName());
        obj.addProperty("description", src.getDescription());
        obj.add("subtasksId",newJsonArray(src.getSubTasks()));
        obj.addProperty("status", src.getStatus().toString());
        obj.addProperty("startTime", src.getStartTime().toString());
        obj.addProperty("duration", src.getDuration());

        return obj;
    }
    public static JsonArray newJsonArray(List<SubTask> list) {
        final JsonArray jsonArray = new JsonArray();
        for(SubTask element : list)
        {
            jsonArray.add(element.getId());
        }
        return jsonArray;
    }

}
