package kanban.net;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import kanban.managers.FileBackedTasksManager;
import kanban.net.adapters.*;
import kanban.tasks.Epic;
import kanban.tasks.SubTask;
import kanban.tasks.Task;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(Epic.class,new EpicAdapter())
            .registerTypeAdapter(SubTask.class,new SubTaskAdapter())
            .registerTypeAdapter(Task.class, new TaskDeserializer())
            .registerTypeAdapter(SubTask.class, new SubTaskDeserializer())
            .registerTypeAdapter(Epic.class, new EpicDeserializer())
            .create();
    final static int PORT = 8080;
    FileBackedTasksManager manager;
    private final HttpServer server;

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::tasks);
        manager = new FileBackedTasksManager();
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println(" http://localhost:" + PORT + "/");

        server.start();
    }
    public void stop(){
        server.stop(1);
    }
    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text,int statusCode) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(statusCode, resp.length);
        h.getResponseBody().write(resp);
    }
    private int parsePathId(String idString) {
        try {
            return Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    private void tasks(HttpExchange httpExchange) {
        String response;
        int statusCode;
        String path = httpExchange.getRequestURI().getPath();
        String uri = httpExchange.getRequestURI().toString();
        String method = httpExchange.getRequestMethod();


        try (httpExchange) {
            switch (method) {
                case "GET":
                    if (path.equals("/tasks")) {

                        response = gson.toJson(manager.getPrioritizedTasks());
                        statusCode = 200;
                        sendText(httpExchange, response, statusCode);
                        return;
                    }
                    if (path.equals("/tasks/history")) {
                        Integer[] ids = manager.getHistory()
                                .stream()
                                .map(Task::getId)
                                .toArray(Integer[]::new);
                        response = gson.toJson(ids);
                        statusCode = 200;
                        sendText(httpExchange, response, statusCode);
                        return;
                    }
                    if (path.equals("/tasks/task")) {

                        response = gson.toJson(manager.getTaskList());
                        statusCode = 200;
                        sendText(httpExchange, response, statusCode);
                        return;
                    }
                    if (path.equals("/tasks/epic")) {

                        response = gson.toJson(manager.getEpicsList());
                        statusCode = 200;
                        sendText(httpExchange, response, statusCode);
                        return;
                    }
                    if (path.equals("/tasks/subtask")) {

                        response = gson.toJson(manager.getSubTasksList());
                        statusCode = 200;
                        sendText(httpExchange, response, statusCode);
                        return;
                    }
                    if (Pattern.matches("^/tasks/task/\\?id=\\d+$", uri)) {
                        String idString = uri.replaceFirst("/tasks/task/\\?id=", "");
                        int id = parsePathId(idString);
                        if (manager.getTaskList().containsKey(id)) {
                            response = gson.toJson(manager.getTask(id));
                            statusCode = 200;
                            sendText(httpExchange, response, statusCode);
                        } else {
                            response = "Задача не найдена";
                            statusCode = 400;
                            sendText(httpExchange, response, statusCode);
                        }
                        return;
                    }
                    if (Pattern.matches("^/tasks/subtask/\\?id=\\d+$", uri)) {
                        String idString = uri.replaceFirst("/tasks/subtask/\\?id=", "");
                        int id = parsePathId(idString);
                        if (manager.getSubTasksList().containsKey(id)) {
                            response = gson.toJson(manager.getSubTask(id));
                            statusCode = 200;
                            sendText(httpExchange, response, statusCode);
                        } else {
                            response = "Задача не найдена";
                            statusCode = 400;
                            sendText(httpExchange, response, statusCode);
                        }
                        return;
                    }
                    if (Pattern.matches("^/tasks/epic/\\?id=\\d+$", uri)) {
                        String idString = uri.replaceFirst("/tasks/epic/\\?id=", "");
                        int id = parsePathId(idString);
                        if (manager.getEpicsList().containsKey(id)) {
                            response = gson.toJson(manager.getEpic(id));
                            statusCode = 200;
                            sendText(httpExchange, response, statusCode);
                        } else {
                            response = "Задача не найдена";
                            statusCode = 400;
                            sendText(httpExchange, response, statusCode);
                        }
                        return;
                    }

                    break;
                case "POST":

                    String body = readText(httpExchange);

                    if (path.equals("/tasks/task")) {
                        Type taskType = new TypeToken<Task>() {}.getType();
                        Task task = gson.fromJson(body, taskType);

                        if (!manager.getTaskList().containsKey(task.getId())) {
                            manager.addTask(task);
                        } else {
                            manager.updateTask(task);
                        }

                        statusCode = 200;
                        sendText(httpExchange, "Задача создана", statusCode);
                    }
                    if (path.equals("/tasks/epic")) {
                        Type taskType = new TypeToken<Epic>() {}.getType();
                        Epic task = gson.fromJson(body, taskType);

                        if (!manager.getEpicsList().containsKey(task.getId())) {
                            manager.addEpic(task);
                        }

                        statusCode = 200;
                        sendText(httpExchange, "Задача создана", statusCode);
                    }
                    if (path.equals("/tasks/subtask")) {
                        Type taskType = new TypeToken<SubTask>() {}.getType();
                        SubTask task = gson.fromJson(body, taskType);

                        if (!manager.getSubTasksList().containsKey(task.getId())) {
                            manager.addSubTask(task);
                        } else {
                            manager.updateSubTask(task);
                        }

                        statusCode = 200;
                        sendText(httpExchange, "Задача создана", statusCode);
                    }

                    break;
                case "DELETE":
                    if (Pattern.matches("^/tasks/task/\\?id=\\d+$", uri)) {
                        String idString = uri.replaceFirst("/tasks/task/\\?id=", "");
                        int id = parsePathId(idString);
                        if (manager.getTaskList().containsKey(id)) {
                            manager.removeTask(manager.getTask(id));
                            statusCode = 200;
                            response = "Задача удалена";
                            sendText(httpExchange, response, statusCode);
                        } else {
                            response = "Задача не найдена";
                            statusCode = 400;
                            sendText(httpExchange, response, statusCode);
                        }
                        return;
                    }
                    if (Pattern.matches("^/tasks/subtask/\\?id=\\d+$", uri)) {
                        String idString = uri.replaceFirst("/tasks/subtask/\\?id=", "");
                        int id = parsePathId(idString);
                        if (manager.getSubTasksList().containsKey(id)) {
                            manager.removeSubTask(manager.getSubTask(id));
                            statusCode = 200;
                            response = "Задача удалена";
                            sendText(httpExchange, response, statusCode);
                        } else {
                            response = "Задача не найдена";
                            statusCode = 400;
                            sendText(httpExchange, response, statusCode);
                        }
                        return;
                    }
                    if (Pattern.matches("^/tasks/epic/\\?id=\\d+$", uri)) {
                        String idString = uri.replaceFirst("/tasks/epic/\\?id=", "");
                        int id = parsePathId(idString);
                        if (manager.getEpicsList().containsKey(id)) {
                            manager.removeEpic(manager.getEpic(id));
                            statusCode = 200;
                            response = "Задача удалена";
                            sendText(httpExchange, response, statusCode);
                        } else {
                            response = "Задача не найдена";
                            statusCode = 400;
                            sendText(httpExchange, response, statusCode);
                        }
                        return;
                    }
                    break;
                default:
                    response = "Некорректный метод!";
                    System.out.println(response);
                    httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception exception) {
            System.out.println("Ошибка при обработке запроса");
        }

    }

}
