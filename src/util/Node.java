package util;

import model.Task;

// Данный класс описывает узел кастомного связанного списка CustomLinkedList
public class Node {
    // Поле для данных внутри узла
    public Task element;
    // Ссылка на предыдущий узел
    public Node prev;
    // Ссылка на следующий узел
    public Node next;

    public Node(Node prev, Task element, Node next) {
        this.element = element;
        this.prev = prev;
        this.next = next;
    }
}