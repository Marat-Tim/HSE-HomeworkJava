package ru.marat.abstraction;

public interface ServiceContainer extends AutoCloseable {
    Session createSession();
}
