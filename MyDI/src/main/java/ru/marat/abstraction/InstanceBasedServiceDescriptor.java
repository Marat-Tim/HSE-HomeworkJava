package ru.marat.abstraction;

public class InstanceBasedServiceDescriptor extends ServiceDescriptor {
    private final Object instance;

    public InstanceBasedServiceDescriptor(Class<?> abstraction, Object instance, Lifetime lifetime) {
        super(abstraction, lifetime);
        this.instance = instance;
    }

    public Object getInstance() {
        return instance;
    }
}
