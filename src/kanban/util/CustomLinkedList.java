package kanban.util;

import kanban.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomLinkedList<T extends Task> {

    private Node<T> first;
    private Node<T> last;
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
    public void remove(int id){
        if(this.nodeHashMap.containsKey(id)){
            this.removeNode(this.nodeHashMap.get(id));
        }
    }
    public void removeNode(Node<T> node) {

        Node<T> prevNode = node.getPrev();
        Node<T> nextNode = node.getNext();
        nodeHashMap.remove(node.data.getId());

        if (prevNode == null) {
            this.first = nextNode;
        } else {
            prevNode.setNext(nextNode);
            node.setPrev(null);
        }
        if (nextNode == null) {
            this.last = prevNode;
        } else {
            nextNode.setPrev(prevNode);
            node.setNext(null);
        }
    }

     private static class Node<T> {
        private final T data;
        private Node<T> next;
        private Node<T> prev;

        public Node(T data, Node<T> next, Node<T> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

         public Node<T> getNext() {
             return next;
         }

         public void setNext(Node<T> next) {
             this.next = next;
         }

         public Node<T> getPrev() {
             return prev;
         }

         public void setPrev(Node<T> prev) {
             this.prev = prev;
         }
     }
}
