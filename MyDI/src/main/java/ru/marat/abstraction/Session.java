package ru.marat.abstraction;

public interface Session extends AutoCloseable {
    <T> T get(Class<T> type);
}
