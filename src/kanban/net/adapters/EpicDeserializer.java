package kanban.net.adapters;

import com.google.gson.*;
import kanban.tasks.Epic;

import java.lang.reflect.Type;

public class EpicDeserializer implements JsonDeserializer<Epic> {
    @Override
    public Epic deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        Epic task = new Epic(obj.get("name").getAsString()
                , obj.get("description").getAsString()
        );
        task.setId(obj.get("id").getAsInt());

        return task;
    }
}
