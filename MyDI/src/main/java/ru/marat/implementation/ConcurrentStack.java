package ru.marat.implementation;

import java.util.concurrent.ConcurrentLinkedDeque;

public class ConcurrentStack<T> {
    private final ConcurrentLinkedDeque<T> concurrentDeque = new ConcurrentLinkedDeque<>();

    public ConcurrentStack() {
    }

    public void push(T el) {
        concurrentDeque.addFirst(el);
    }

    public T getAndRemove() {
        return concurrentDeque.removeFirst();
    }

    public boolean isEmpty() {
        return concurrentDeque.isEmpty();
    }
}
