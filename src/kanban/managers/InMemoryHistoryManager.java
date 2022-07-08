package kanban.managers;

import kanban.tasks.Task;
import kanban.util.CustomLinkedList;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> history;

    public InMemoryHistoryManager() {
        this.history = new CustomLinkedList<>();
    }
    @Override
    public void add(Task task){
        remove(task.getId());
        history.linkLast(task);
    }
    @Override
    public void remove(int id){
        history.remove(id);
    }
    @Override
    public List<Task> getHistory(){
        return history.getTasks();
    }
}
