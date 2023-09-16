package ru.marat;

import org.junit.Test;
import ru.marat.abstraction.ServiceContainer;
import ru.marat.abstraction.ServiceContainerBuilder;
import ru.marat.abstraction.Session;
import ru.marat.implementation.ServiceContainerSimpleBuilder;

import java.util.Random;
import java.util.random.RandomGenerator;

public class DITest {
    @Test
    public void test() {
        ServiceContainerBuilder builder = new ServiceContainerSimpleBuilder();
        ServiceContainer container = builder
                .addSessioned(Service.class, ServiceImpl.class)
                .addPrototype(Random.class, scope -> new Random(System.nanoTime()))
                .addSingleton(RandomGenerator.class, RandomGenerator.getDefault())
                .build();
        Session session = container.createSession();
        var service1 = session.get(Service.class);
        var service2 = session.get(Service.class);
        var singleton1 = session.get(RandomGenerator.class);
        var p1 = session.get(Random.class);
        var p2 = session.get(Random.class);
        var p3 = session.get(Random.class);
        Session session1 = container.createSession();
        var service3 = session1.get(Service.class);
        var singleton2 = session1.get(RandomGenerator.class);
        var p4 = session1.get(Random.class);
        var p5 = session1.get(Random.class);
        System.out.println();
    }
}
