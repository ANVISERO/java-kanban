package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.model.Task;

// Данный класс описывает узел кастомного связанного списка CustomLinkedList
public class Node {
    // Поле для данных внутри узла
    private Task element;
    // Ссылка на предыдущий узел
    private Node prev;
    // Ссылка на следующий узел
    private Node next;

    public Node(Node prev, Task element, Node next) {
        this.element = element;
        this.prev = prev;
        this.next = next;
    }

    public Task getElement() {
        return element;
    }

    public void setElement(Task element) {
        this.element = element;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}