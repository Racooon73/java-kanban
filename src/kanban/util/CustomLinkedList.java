package kanban.util;

import kanban.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomLinkedList<T extends Task> {

    Node<T> first;
    Node<T> last;
    public final HashMap<Integer, Node<T>> nodeHashMap = new HashMap<>();

    public void linkLast(T last) {
        Node<T> lastNode = this.last;
        Node<T> newNode = new Node<>(last, null, lastNode);

        this.last = newNode;
        if (lastNode == null) {
            this.first = newNode;
        } else {
            lastNode.next = newNode;
        }
        this.nodeHashMap.put(newNode.data.getId(), newNode);


    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        Node<T> node;

        for (node = this.first; node != null; node = node.next) {
            tasks.add(node.data);
        }
        return tasks;
    }

    public void removeNode(Node<T> node) {

        Node<T> prevNode = node.prev;
        Node<T> nextNode = node.next;
        nodeHashMap.remove(node.data.getId());

        if (prevNode == null) {
            this.first = nextNode;
        } else {
            prevNode.next = nextNode;
            node.prev = null;
        }
        if (nextNode == null) {
            this.last = prevNode;
        } else {
            nextNode.prev = prevNode;
            node.next = null;
        }
    }

     public static class Node<T> {
        private final T data;
        private Node<T> next;
        private Node<T> prev;

        public Node(T data, Node<T> next, Node<T> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
