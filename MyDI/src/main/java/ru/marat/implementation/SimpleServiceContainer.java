package ru.marat.implementation;

import ru.marat.abstraction.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleServiceContainer implements ServiceContainer {
    @Override
    public void close() throws Exception {
        rootSession.close();
    }

    private class SessionImpl implements Session {
        private final ConcurrentMap<Class<?>, Object> sessionedInstances = new ConcurrentHashMap<>();

        private final ConcurrentStack<AutoCloseable> closeableObjects = new ConcurrentStack<>();

        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(Class<T> type) {
            var descriptor = descriptors.get(type);
            if (descriptor.getLifetime() == Lifetime.SESSION || this == rootSession) {
                return (T) sessionedInstances.computeIfAbsent(type, this::createInstanceAndAddToCloseable);
            }
            if (descriptor.getLifetime() == Lifetime.PROTOTYPE) {
                return (T) createInstanceAndAddToCloseable(type);
            }
            if (descriptor.getLifetime() == Lifetime.SINGLETON) {
                return rootSession.get(type);
            }
            throw new UnsupportedOperationException(
                    "Был добавлен новый дескриптор, который не обрабатывается здесь");
        }


        @Override
        public void close() throws Exception {
            while (!closeableObjects.isEmpty()) {
                var object = closeableObjects.getAndRemove();
                object.close();
            }
        }

        private Object createInstanceAndAddToCloseable(Class<?> type) {
            var instance = createInstance(type, this);
            if (instance instanceof AutoCloseable closeableObject) {
                closeableObjects.push(closeableObject);
            }
            return instance;
        }
    }

    private final Map<Class<?>, ServiceDescriptor> descriptors;

    private final Session rootSession = new SessionImpl();

    private final ConcurrentMap<Class<?>, Function<Session, Object>> builtActivators =
            new ConcurrentHashMap<>();

    public SimpleServiceContainer(List<ServiceDescriptor> descriptors) {
        this.descriptors = descriptors.stream().collect(Collectors.toMap(ServiceDescriptor::getAbstraction,
                Function.identity()));
    }

    @Override
    public Session createSession() {
        return new SessionImpl();
    }

    private Function<Session, Object> buildActivation(Class<?> type) {
        if (!descriptors.containsKey(type)) {
            throw new IllegalArgumentException("Тип %s не зарегистрирован".formatted(type));
        }
        ServiceDescriptor descriptor = descriptors.get(type);
        if (descriptor instanceof InstanceBasedServiceDescriptor instanceBasedServiceDescriptor) {
            return session -> instanceBasedServiceDescriptor.getInstance();
        }
        if (descriptor instanceof FactoryBasedServiceDescriptor factoryBasedServiceDescriptor) {
            return session -> factoryBasedServiceDescriptor.getFactoryMethod().apply(session);
        }
        if (descriptor instanceof TypeBasedServiceDescriptor typeBasedServiceDescriptor) {
            var implementationType = typeBasedServiceDescriptor.getImplementation();
            var constructors = implementationType.getConstructors();
            if (constructors.length != 1) {
                throw new IllegalArgumentException("Тип %s должен иметь ровно 1 конструктор".formatted(type));
            }
            var constructor = constructors[0];
            var argTypes = constructor.getParameterTypes();
            return session -> {
                var args = new Object[argTypes.length];
                for (int i = 0; i < argTypes.length; i++) {
                    args[i] = createInstance(argTypes[i], session);
                }
                try {
                    return constructor.newInstance(args);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
        }
        throw new UnsupportedOperationException(
                "Был добавлен новый дескриптор, который не обрабатывается здесь");
    }

    private Object createInstance(Class<?> type, Session session) {
        return builtActivators.computeIfAbsent(type, this::buildActivation).apply(session);
    }
}
