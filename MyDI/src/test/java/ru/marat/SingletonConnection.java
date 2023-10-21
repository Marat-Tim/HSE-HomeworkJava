package ru.marat;

public class SingletonConnection implements AutoCloseable {
    public boolean isClosed = false;

    @Override
    public void close() throws Exception {
        isClosed = true;
    }
}
