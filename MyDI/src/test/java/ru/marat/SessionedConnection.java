package ru.marat;

public class SessionedConnection implements AutoCloseable {
    public boolean isClosed = false;

    @Override
    public void close() throws Exception {
        isClosed = true;
    }
}
