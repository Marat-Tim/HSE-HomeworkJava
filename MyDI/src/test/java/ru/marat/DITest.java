package ru.marat;

import lombok.SneakyThrows;
import org.junit.Test;
import ru.marat.abstraction.ServiceContainer;
import ru.marat.abstraction.ServiceContainerBuilder;
import ru.marat.abstraction.Session;
import ru.marat.implementation.ServiceContainerSimpleBuilder;

import java.util.Random;
import java.util.random.RandomGenerator;

import static org.junit.Assert.*;

public class DITest {
    @Test
    @SneakyThrows
    public void testAutoClosable() {
        ServiceContainerBuilder builder = new ServiceContainerSimpleBuilder();
        SingletonConnection singleton;
        try (ServiceContainer container = builder
                .addSingleton(SingletonConnection.class, SingletonConnection.class)
                .addSessioned(SessionedConnection.class, SessionedConnection.class)
                .addPrototype(PrototypeConnection.class, PrototypeConnection.class)
                .build()) {
            SessionedConnection sessioned1;
            PrototypeConnection p1, p2;
            try (Session session = container.createSession()) {
                singleton = session.get(SingletonConnection.class);
                sessioned1 = session.get(SessionedConnection.class);
                p1 = session.get(PrototypeConnection.class);
                p2 = session.get(PrototypeConnection.class);
            }
            assertTrue(p1.isClosed);
            assertTrue(p2.isClosed);
            assertTrue(sessioned1.isClosed);
            assertFalse(singleton.isClosed);

            SessionedConnection sessioned2;
            try (Session session = container.createSession()) {
                sessioned2 = session.get(SessionedConnection.class);
            }
            assertTrue(sessioned2.isClosed);
        }
        assertTrue(singleton.isClosed);
    }

    @Test
    public void simpleTest() {
        ServiceContainerBuilder builder = new ServiceContainerSimpleBuilder();
        ServiceContainer container = builder
                .addSessioned(Service.class, ServiceImpl.class)
                .addSessioned(Controller.class, Controller.class)
                .addSingleton(RandomGenerator.class, RandomGenerator.getDefault())
                .addPrototype(Random.class, session ->
                        new Random(session.get(RandomGenerator.class).nextInt()))
                .build();

        Session session = container.createSession();
        var service1 = session.get(Service.class);
        var service2 = session.get(Service.class);
        assertEquals(service1, service2);
        var singleton1 = session.get(RandomGenerator.class);
        var p1 = session.get(Random.class);
        var p2 = session.get(Random.class);
        var p3 = session.get(Random.class);
        assertNotEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(p2, p3);
        Session session1 = container.createSession();
        var service3 = session1.get(Service.class);
        assertNotEquals(service1, service3);
        var singleton2 = session1.get(RandomGenerator.class);
        assertEquals(singleton1, singleton2);
        var p4 = session1.get(Random.class);
        var p5 = session1.get(Random.class);
        assertNotEquals(p4, p5);
        assertNotEquals(p4, p1);
    }
}
