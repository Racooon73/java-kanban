package kanban.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kanban.net.*;
import kanban.net.adapters.*;
import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import kanban.tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;

public class HttpTaskManager extends FileBackedTasksManager{
    private static String serverURL;
    private KVTaskClient client;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(Epic.class,new EpicAdapter())
            .registerTypeAdapter(SubTask.class,new SubTaskAdapter())
            .registerTypeAdapter(Task.class, new TaskDeserializer())
            .registerTypeAdapter(SubTask.class, new SubTaskDeserializer())
            .registerTypeAdapter(Epic.class, new EpicDeserializer())
            .registerTypeAdapter(HttpTaskManager.class, new TaskManagerAdapter())
            .registerTypeAdapter(HttpTaskManager.class, new TaskManagerDeserializer())
            .create();

    public HttpTaskManager(String serverURL) throws IOException, InterruptedException {
        this.client = new KVTaskClient(serverURL);
        HttpTaskManager.serverURL = serverURL;


    }
    public HttpTaskManager() throws IOException, InterruptedException {
        HttpTaskManager.serverURL = "http://localhost:8078";
        this.client = new KVTaskClient(serverURL);



    }
    public void setClient(KVTaskClient client){
        this.client = client;

    }
    @Override
    public void save(){
        try {
            client.put("1",gson.toJson(this));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpTaskManager load() throws IOException, InterruptedException {
        Type managerType = new TypeToken<HttpTaskManager>() {}.getType();

        return gson.fromJson(client.load("1"),managerType);
    }
}
