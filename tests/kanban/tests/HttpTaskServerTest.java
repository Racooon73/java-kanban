package kanban.tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kanban.enums.Status;
import kanban.net.HttpTaskServer;
import kanban.net.adapters.*;
import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import kanban.tasks.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    HttpTaskServer server = new HttpTaskServer();
    Task task;
    Epic epic;
    SubTask subTask;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(Epic.class,new EpicAdapter())
            .registerTypeAdapter(SubTask.class,new SubTaskAdapter())
            .registerTypeAdapter(Task.class, new TaskDeserializer())
            .registerTypeAdapter(SubTask.class, new SubTaskDeserializer())
            .registerTypeAdapter(Epic.class, new EpicDeserializer())
            .create();

    HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    void init() throws IOException {

        server.start();
        task = new Task("TestTask", "Test description", Status.NEW, LocalDateTime.of(2022,8,10,12,0),30);
        task.setId(1);
        epic = new Epic("TestEpic", "Test description");
        epic.setId(2);
        subTask = new SubTask("TestSubTask", "Test description", Status.NEW,LocalDateTime.of(2022,8,10,12,0),30);
        epic.addSubTask(subTask);
        subTask.setId(3);
    }
    @AfterEach
    void close(){
        server.stop();
    }

    @Test
    void createTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    void createSubTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String json = gson.toJson(subTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    void createEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    void getTaskTest() throws IOException, InterruptedException {
        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body).build();
        HttpResponse<String> response = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response2.statusCode());
        Type taskType = new TypeToken<Task>() {}.getType();
        Task task = gson.fromJson(response2.body(), taskType);
        assertNotNull(task, "Задача не возвращается");
        assertEquals(task, this.task, "Задачи не совпадают");
    }
    @Test
    void getSubTaskTest() throws IOException, InterruptedException {
        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/subtask");
        String json = gson.toJson(subTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body).build();
        HttpResponse<String> response = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response2.statusCode());
        Type taskType = new TypeToken<SubTask>() {}.getType();
        Task task = gson.fromJson(response2.body(), taskType);
        assertNotNull(task, "Задача не возвращается");
        assertEquals(task, this.subTask, "Задачи не совпадают");
    }
    @Test
    void getEpicTest() throws IOException, InterruptedException {
        HttpClient client1 = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body).build();
        HttpResponse<String> response = client1.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpClient client2 = HttpClient.newHttpClient();
        URI url2 = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response2.statusCode());
        Type taskType = new TypeToken<Epic>() {}.getType();
        Task task = gson.fromJson(response2.body(), taskType);
        assertNotNull(task, "Задача не возвращается");
        assertEquals(task, this.epic, "Задачи не совпадают");
    }

}