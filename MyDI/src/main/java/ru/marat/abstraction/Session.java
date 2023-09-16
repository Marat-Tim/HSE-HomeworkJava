package ru.marat.abstraction;

import java.io.Closeable;
import java.nio.channels.AsynchronousByteChannel;

public interface Session extends AutoCloseable {
    <T> T get(Class<T> type);
}
