package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Это менеджер, который управляет историей просмотров
public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Long, Node> nodeMap; // key - id, value - Node
    private final CustomLinkedList customLinkedList;
    private static final int CAPACITY = 10;
    private static final byte OLDEST_INDEX_IN_HISTORY = 0;

    public InMemoryHistoryManager() {
        this.nodeMap = new HashMap<>();
        this.customLinkedList = new CustomLinkedList();
    }

    // Добавляет задачу в историю задач
    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (nodeMap.containsKey(task.getId())) {
            remove(task.getId());
        }

        customLinkedList.linkLast(task);
    }

    // Удаляет задачу из просмотра по id
    @Override
    public void remove(long id) {
        customLinkedList.removeNode(nodeMap.get(id));
    }

    // Возвращает список просмотренных задач
    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    // Данный класс описывает кастомную реализацию связанного списка
    private class CustomLinkedList {
        // Указатель на первый элемент списка
        private Node head;
        // Указатель на последний элемент списка
        private Node tail;

        // Размер связанного списка
        private long size = 0;

        // Добавляет задачу в конец списка
        public void linkLast(Task task) {
            final Node oldTail = tail;
            final Node newNode = new Node(oldTail, task, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.setNext(newNode);
            }
            nodeMap.put(task.getId(), newNode);
            size++;
            if (size > CAPACITY) {
                removeNode(head);
            }
        }

        // Собирает все задачи из списка и возвращает ArrayList
        public List<Task> getTasks() {
            List<Task> history = new ArrayList<>();
            Node currentNode = head;

            if (currentNode == null) {
                System.out.println("Список пуст!");
                return new ArrayList<>();
            }

            while (currentNode != null) {
                if (history.size() == CAPACITY) {
                    history.remove(OLDEST_INDEX_IN_HISTORY);
                }

                history.add(currentNode.getElement());
                currentNode = currentNode.getNext();
            }
            return history;
        }

        // Вырезает узел переданный в параметр метода узел списка
        public void removeNode(Node node) {
            final Node prev = node.getPrev();
            final Node next = node.getNext();

            if (prev == null) {
                head = next;
            } else {
                prev.setNext(next);
                node.setPrev(null);
            }

            if (next == null) {
                tail = prev;
            } else {
                next.setPrev(prev);
                node.setNext(null);
            }
            nodeMap.remove(node.getElement().getId());

            node.setElement(null);
            size--;
        }
    }
}
