package org.yakdanol.homework.model;

import java.util.List;

public class CustomLinkedList<E> {
    private int size = 0;
    private Node<E> first;
    private Node<E> last;

    public CustomLinkedList() {
        this.first = null;
        this.last = null;
    }

    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.prev = prev;
            this.next = next;
        }
    }

    public int size() {
        return size;
    }

    /**
     * Вставляет элемент в конец списка
     */
    public void add(E e) {
        final Node<E> lastElement = last;
        final Node<E> newNode = new Node<>(lastElement, e, null);
        last = newNode;

        if (lastElement == null)
            first = newNode;
        else
            lastElement.next = newNode;
        size++;
    }

    /**
     * Возвращает элемент в указанной позиции из списка
     */
    public E get(int index) {
        checkElementIndex(index);
        return node(index).item;
    }

    /**
     * Удаляет элемент в указанной позиции в этом списке. Сдвигает все
     * последующие элементы влево (вычитает единицу из их индексов).
     * Возвращает элемент, который был удален из списка.
     */
    public E remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    /**
     * Проверяет содержание указанного элемента в списке
     */
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * Добавляет все элементы из указанной коллекции в конец списка в том
     * порядке, в котором они возвращаются итератором указанной коллекции.
     */
    public void addAll(List<E> elements) {
        for (E element : elements) {
            add(element);
        }
    }

    public void printList() {
        Node<E> current = first;
        while (current != null) {
            System.out.print(current.item + " ");
            current = current.next;
        }
        System.out.println();
    }

    /**
     * Возвращает (ненулевой) узел с указанным индексом элемента
     */
    Node<E> node(int index) {
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    /**
     * Отсоединяет non-null Node x.
     */
    E unlink(Node<E> x) {
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        return element;
    }

    /**
     * Возвращает индекс первого вхождения указанного элемента
     * в списке или -1, если список не содержит элемента
     */
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null)
                    return index;
                index++;
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item))
                    return index;
                index++;
            }
        }
        return -1;
    }

    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * Указывает, является ли аргумент индексом существующего элемента
     */
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }
}
