package kanban.managers;

import kanban.tasks.Task;
import kanban.util.Node;

import java.util.ArrayList;
import java.util.HashMap;
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
        if(history.nodeHashMap.containsKey(id)){
            history.removeNode(history.nodeHashMap.get(id));
        }
    }
    @Override
    public List<Task> getHistory(){
        return history.getTasks();
    }
}
class CustomLinkedList<T extends Task> {

    Node<T> first;
    Node<T> last;
    final HashMap<Integer, Node<T>> nodeHashMap = new HashMap<>();

    public void linkLast(T last){
        Node<T> l = this.last;
        Node<T> newNode = new Node<>(last, null,l);

        this.last = newNode;
        if (l == null) {
            this.first = newNode;
        } else {
            l.next = newNode;
        }
        this.nodeHashMap.put(newNode.data.getId(), newNode);


    }
    public List<Task> getTasks(){
        List<Task> tasks = new ArrayList<>();
        Node<T> e;

        for (e = this.first; e != null; e = e.next) {
            tasks.add(e.data);
        }
        return tasks;
    }
    public void removeNode(Node<T> node){

        Node<T> p = node.prev;
        Node<T> n = node.next;
        nodeHashMap.remove(node.data.getId());

        if (p == null) {
            this.first = n;
        } else {
            p.next = n;
            node.prev = null;
        }
        if (n == null) {
            this.last = p;
        } else {
            n.prev = p;
            node.next = null;
        }
    }
}