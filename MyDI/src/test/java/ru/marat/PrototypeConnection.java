package ru.marat;

public class PrototypeConnection implements AutoCloseable {
    public boolean isClosed = false;

    @Override
    public void close() throws Exception {
        isClosed = true;
    }
}
